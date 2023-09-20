package com.luoying.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoying.common.ErrorCode;
import com.luoying.exception.BusinessException;
import com.luoying.mapper.UserMapper;
import com.luoying.model.domain.User;
import com.luoying.model.dto.UserDTO;
import com.luoying.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            throw new BusinessException(ErrorCode.JDBC_ERROR, "数据库保存注册用户失败");
        }
        return user1.getId();
    }

    @Override
    public UserDTO userLogin(String userAccount, String userPassword,HttpServletRequest request) {
        // 1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.JDBC_ERROR, "用户登录请求对象属性空值");
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
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        //4 存储到session
        request.getSession().setAttribute(USER_LOGIN_STATE,userDTO);
        // 5 返回
        return userDTO;
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
    public List<UserDTO> queryUsersByTagsByMemory(List<String> tagList) {
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
        }).map(user -> BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());

    }

    /**
     * 使用标签搜索用户（SQL查询版）
     *
     * @param tagList 用户传入的标签
     * @return
     */
    @Deprecated
    public List<UserDTO> queryUsersByTagsBySQL(List<String> tagList) {
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
        return users.stream().map(user -> BeanUtil.copyProperties(user, UserDTO.class)).collect(Collectors.toList());
    }
}







