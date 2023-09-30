package com.luoying.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoying.model.domain.User;
import com.luoying.model.vo.UserVO;
import com.luoying.model.request.UserQueryRequest;
import com.luoying.model.vo.UserListVO;

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
    UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 管理员查询用户
     * @param userQueryRequest
     * @param request
     * @return
     */
    UserListVO userListQuery(UserQueryRequest userQueryRequest, HttpServletRequest request);
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
    List<UserVO> queryUsersByTagsByMemory(List<String> tagList);
    List<UserVO> queryUsersByTagsBySQL(List<String> tagList);

    /**更新用户信息
     *
     * @param user
     * @return
     */
    int updateUser(User user, UserVO loginUser);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    UserVO getLoginUser(HttpServletRequest request);

    /**
     * 判断当前登录用户是否是管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);
    boolean isAdmin(UserVO loginUser);

    /**主页推荐
     *
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    List<UserVO> usersRecommend(long currentPage, long pageSize, HttpServletRequest request);

    /**
     * 获取与当前用户相似度最高的用户
     * @param num
     * @param loginUser
     * @return
     */
    List<UserVO> usersMatch(long num, UserVO loginUser);

}
