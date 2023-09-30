package com.luoying.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoying.common.ErrorCode;
import com.luoying.constant.UserConstant;
import com.luoying.exception.BusinessException;
import com.luoying.mapper.UserMapper;
import com.luoying.model.domain.User;
import com.luoying.model.request.UserQueryRequest;
import com.luoying.model.vo.UserListVO;
import com.luoying.model.vo.UserVO;
import com.luoying.service.UserService;
import com.luoying.utils.AlgorithmUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.luoying.constant.RedisConstant.RECOMMEND_USUERS_KEY;
import static com.luoying.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author 落樱的悔恨
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-08-24 20:17:28
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    //盐值混淆密码
    private static final String SALT = "luoying";

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String authCode) {
        // 1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, authCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户注册请求对象属性空值");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码或二次密码长度小于8位");
        }
        if (authCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "权限编号长度大于5位");
        }
        //密码和二次密码是否相同
        if (!StringUtils.equals(userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //账户不能包含特殊字符
        String regex = "^[a-zA-Z0-9]{4,20}$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号含有特殊字符");
        }
        // 账户不能重复
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号重复");
        }
        // 付费用户编号不能重复
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAuthCode, authCode);
        count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "权限编号重复");
        }
        // 2 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3 向数据库插入数据
        User user1 = new User();
        user1.setUserAccount(userAccount);
        user1.setUserPassword(encryptPassword);
        user1.setAuthCode(authCode);
        boolean result = this.save(user1);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库保存注册用户失败");
        }
        return user1.getId();
    }

    @Override
    public UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户登录请求对象属性空值");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码长度小于8位");
        }
        //账户不能包含特殊字符
        String regex = "^[a-zA-Z0-9]{4,20}$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号含有特殊字符");
        }
        // 2 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 判断用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount);
        wrapper.eq(User::getUserPassword, encryptPassword);
        User user = userMapper.selectOne(wrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed,for not exists user!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 3 用户信息（脱敏）
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        //4 存储到session
        request.getSession().setAttribute(USER_LOGIN_STATE, userVO);
        // 5 返回
        return userVO;
    }

    @Override
    public UserListVO userListQuery(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        // 鉴权，仅管理员可查询
        if (!this.isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        //构造分页条件
        User user = BeanUtil.copyProperties(userQueryRequest, User.class);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.like(StringUtils.isNotBlank(user.getUsername()), User::getUsername, user.getUsername());
        wrapper.like(StringUtils.isNotBlank(user.getUserAccount()), User::getUserAccount, user.getUserAccount());
        wrapper.like(StringUtils.isNotBlank(user.getPhone()), User::getPhone, user.getPhone());
        wrapper.like(StringUtils.isNotBlank(user.getEmail()), User::getEmail, user.getEmail());
        wrapper.like(StringUtils.isNotBlank(user.getAuthCode()), User::getAuthCode, user.getAuthCode());
        wrapper.like(StringUtils.isNotBlank(user.getProfile()), User::getProfile, user.getProfile());
        wrapper.like(user.getGender() != null, User::getGender, user.getGender());
        wrapper.like(user.getUserRole() != null, User::getUserRole, user.getUserRole());
        IPage<User> page = new Page<>(userQueryRequest.getPage(), userQueryRequest.getPageSize());
        this.page(page, wrapper);
        //脱敏
        List userDTOList = page.getRecords().stream().map(user1 -> {
            return BeanUtil.copyProperties(user1, UserVO.class);
        }).collect(Collectors.toList());
        //将查询到的数据封装到UserListVO
        UserListVO userListVO = new UserListVO();
        userListVO.setUserList(userDTOList);
        userListVO.setTotal(page.getTotal());
        //返回
        return userListVO;
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


    /**
     * 使用标签搜索用户(内存过滤）
     *
     * @param tagList 用户传入的标签
     * @return
     */
    @Override
    public List<UserVO> queryUsersByTagsByMemory(List<String> tagList) {
        //判空
        if (CollectionUtil.isEmpty(tagList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签不能为空");
        }
        //内存查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //查询所有用户
        List<User> users = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return users.stream().filter(user -> {
            Set<String> tempTagSet = gson.fromJson(user.getTags(), new TypeToken<Set<String>>() {
            }.getType());
            //有些用户的tags字段可能为空，要先判空，否则报控空指针异常
            tempTagSet = Optional.ofNullable(tempTagSet).orElse(new HashSet<>());
            //过滤
            for (String tag : tagList) {
                if (!tempTagSet.contains(tag))
                    return false;
            }
            return true;
        }).map(user -> BeanUtil.copyProperties(user, UserVO.class)).collect(Collectors.toList());

    }

    /**
     * 使用标签搜索用户（SQL查询版）
     *
     * @param tagList 用户传入的标签
     * @return
     */
    @Deprecated
    public List<UserVO> queryUsersByTagsBySQL(List<String> tagList) {
        //判空
        if (CollectionUtil.isEmpty(tagList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签不能为空");
        }

        //SQL模糊查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        for (String tag : tagList) {
            queryWrapper.like(StringUtils.isNotBlank(tag), User::getTags, tag);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        //脱敏
        return users.stream().map(user -> BeanUtil.copyProperties(user, UserVO.class)).collect(Collectors.toList());
    }

    @Override
    public int updateUser(User user, UserVO loginUser) {
        Long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //如果不是管理员，又不是修改自己的信息，就报错
        if (!isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User dbUser = userMapper.selectById(userId);
        if (dbUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public UserVO getLoginUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "HttpServletRequest空值");
        }

        UserVO loginUser = (UserVO) request.getSession().getAttribute(USER_LOGIN_STATE);

        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "未登录");
        }
        return loginUser;
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        UserVO loginUser = (UserVO) request.getSession().getAttribute(USER_LOGIN_STATE);
        return loginUser != null && loginUser.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    @Override
    public boolean isAdmin(UserVO loginUser) {
        return loginUser != null && loginUser.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    @Override
    public List<UserVO> usersRecommend(long currentPage, long pageSize, HttpServletRequest request) {
        //获取当前登录用户
        UserVO loginUser = this.getLoginUser(request);
        //拼接key
        String key = RECOMMEND_USUERS_KEY + loginUser.getId();
        //读取缓存
        String userDTOListJson = stringRedisTemplate.opsForValue().get(key);
        //反序列化
        List userDTOList = JSON.parseObject(userDTOListJson, List.class);
        //如果有缓存直接读缓存
        if (userDTOList != null && !userDTOList.isEmpty()) {
            return userDTOList;
        }
        //如果有没缓存查数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        Page<User> page = this.page(new Page<User>(currentPage, pageSize), wrapper);

        userDTOList = page.getRecords().stream().map(user1 -> {
            return BeanUtil.copyProperties(user1, UserVO.class);
        }).collect(Collectors.toList());
        //将查询到的数据添加到缓存
        userDTOListJson = JSON.toJSONString(userDTOList);
        stringRedisTemplate.opsForValue().set(key, userDTOListJson, 3, TimeUnit.MINUTES);
        return userDTOList;
    }

    @Override
    public List<UserVO> usersMatch(long num, UserVO loginUser) {
        //获取当前用户的标签列表
        String loginUserTags = loginUser.getTags();
        Gson gson = new Gson();
        //把当前用户的标签列表转换成List集合
        List<String> loginUserTagList = gson.fromJson(loginUserTags, new TypeToken<List<String>>() {
        }.getType());

        //查询所有标签字段不为空的用户，且只查用id和tags字段
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.select(User::getId, User::getTags);
        userWrapper.isNotNull(User::getTags);
        List<User> users = this.list(userWrapper);
        //用户==>相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        //遍历查到的每个用户
        for (User user : users) {
            //获取用户的标签
            String userTags = user.getTags();
            //过滤掉无标签的用户和当前用户
            if (StringUtils.isBlank(userTags) || user.getId().equals(loginUser.getId())) {
                continue;
            }
            //把用户的标签列表转换成List集合
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            //使用编辑距离算法，计算出每个用户的标签和当前用户的标签的编辑距离
            long distance = AlgorithmUtil.minDistance1(loginUserTagList, userTagList);
            //存到list集合中
            list.add(new Pair<>(user, distance));
        }
        //按编辑距离升序排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((p1, p2) -> (int) (p1.getValue() - p2.getValue()))
                .limit(num).collect(Collectors.toList());
        //从topUserPairList取出userId
        List<Long> userIdList = topUserPairList.stream()
                .map(pair -> pair.getKey().getId())
                .collect(Collectors.toList());
        userWrapper = new LambdaQueryWrapper<>();
        userWrapper.in(User::getId,userIdList);
        //根据userId查询用户，然后返回
        Map<Long, List<UserVO>> userIdUserMap = this.list(userWrapper).stream()
                .map(user -> BeanUtil.copyProperties(user, UserVO.class))
                .collect(Collectors.groupingBy(UserVO::getId));
        List<UserVO> finalUserList=new ArrayList<>();
        for (Long userId : userIdList) {
            finalUserList.add(userIdUserMap.get(userId).get(0));
        }
        return finalUserList;
    }
}







