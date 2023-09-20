package com.luoying.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoying.model.domain.User;
import com.luoying.model.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    UserDTO userLogin(String userAccount, String userPassword,HttpServletRequest request);

    /**
     * 退出登录
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     *使用标签搜索用户
     * @param tagList 用户传入的标签
     * @return
     */
    List<UserDTO> queryUsersByTagsByMemory(List<String> tagList);
    List<UserDTO> queryUsersByTagsBySQL(List<String> tagList);

}
