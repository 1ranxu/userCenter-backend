package com.luoying.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.common.ErrorCode;
import com.luoying.constant.UserConstant;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.User;
import com.luoying.model.dto.UserDTO;
import com.luoying.service.UserService;
import com.luoying.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.luoying.constant.RedisConstants.LOGIN_USER_KEY;
import static com.luoying.constant.RedisConstants.LOGIN_USER_TTL;

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
            throw new BusinessException(ErrorCode.JDBC_ERROR, "数据库保存注册用户失败");
        }
        return user1.getId();
    }

    @Override
    public UserDTO userLogin(String userAccount, String userPassword) {
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

        //4 保存用户信息到 redis中
        // 4.1.随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        //把token设置到userDTO,也返回给前端
        userDTO.setToken(token);
        // 4.2.将User对象转为HashMap存储
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            if (fieldValue==null){
                                return "";
                            }
                            return fieldValue.toString();
                        }));
        // 4.3.存储
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 4.4.设置token有效期
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
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
        String tokenKey = LOGIN_USER_KEY + request.getHeader("authorization");
        Set<Object> keys = stringRedisTemplate.opsForHash().keys(tokenKey);
        //移除登录态
        for (Object key : keys) {
            stringRedisTemplate.opsForHash().delete(tokenKey,key);
        }
        return 1;
    }
}




