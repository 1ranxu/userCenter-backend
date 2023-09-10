package com.luoying.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoying.model.domain.User;
import com.luoying.model.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 落樱的悔恨
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-08-24 20:17:28
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 用户二次密码
     * @param authCode      付费用户编号
     * @return 注册用户Id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String authCode);

    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 用户信息（脱敏）
     */
    UserDTO userLogin(String userAccount, String userPassword, HttpServletResponse response);


    int userLogout(HttpServletRequest request);

}
