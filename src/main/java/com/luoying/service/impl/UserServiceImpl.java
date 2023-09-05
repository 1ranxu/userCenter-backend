package com.luoying.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.common.ErrorCode;
import com.luoying.constant.UserConstant;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.User;
import com.luoying.service.UserService;
import com.luoying.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.JDBC_ERROR, "用户登录请求对象属性空值");
            //    todo 修改为只定义异常
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
        User safetyUser = getSafetyUser(user);
        // 4 记录用户的登录状态
        HttpSession session = request.getSession();
        session.setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
        // 5 返回
        return safetyUser;
    }

    /**
     * 用户拓明
     *
     * @param user
     * @return
     */
    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户空值");
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setAuthCode(user.getAuthCode());
        return safetyUser;
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
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }
}




