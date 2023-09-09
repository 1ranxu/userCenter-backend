package com.luoying.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoying.common.ErrorCode;
import com.luoying.common.Result;
import com.luoying.constant.UserConstant;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.User;
import com.luoying.model.request.UserLoginRequest;
import com.luoying.model.request.UserRegisterRequest;
import com.luoying.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static com.luoying.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author 落樱的悔恨
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户注册请求对象空值");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String authCode = userRegisterRequest.getAuthCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, authCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户注册请求对象属性空值");
        }
        long userId = userService.userRegister(userAccount, userPassword, checkPassword, authCode);
        return Result.success(userId);
    }

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.JDBC_ERROR, "用户登录请求对象空值");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.JDBC_ERROR, "用户登录请求对象属性空值");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return Result.success(user);
    }


    @PostMapping("/loginout")
    public Result userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "HttpServletRequest请求空值");
        }
        int result = userService.userLogout(request);
        return Result.success(result);
    }

    @GetMapping("/current")
    public Result getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "用户未登录");
        }
        Long userId = currentUser.getId();
        //todo校验用户是否合法
        User user = userService.getById(userId);

        User safetyUser = userService.getSafetyUser(user);
        return Result.success(safetyUser);
    }

    @GetMapping("/query")
    public Result userListQuery(String username, HttpServletRequest request) {
        // 1 鉴权，仅管理员可查询
        if (!isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        // 2 查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like(User::getUsername, username);
        }
        List<User> userList = userService.list(wrapper);
        List<User> collect = userList.stream().map(userService::getSafetyUser).collect(Collectors.toList());
        return Result.success(collect);
    }

    @PostMapping("/delete")
    public Result userDelete(@RequestBody User user, HttpServletRequest request) {
        // 1 鉴权，仅管理员可删除
        if (!isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        //2 删除
        if (user.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除用户的id不能为小于等于0");
        }
        boolean result = userService.removeById(user.getId());
        return Result.success(result);
    }

    @PostMapping("/update")
    public Result userUpdate(@RequestBody User user, HttpServletRequest request) {
        // 1 鉴权，仅管理员可更新
        if (!isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        //2 获取更新用户
        if (user.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新用户的id不能为小于等于0");
        }
        User originUser = userService.getById(user.getId());
        //判断用户是否存在
        if (originUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        //更新，前端传过来的数据有就更新，没有就保持默认
        boolean result = userService.updateById(user);
        return Result.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}
