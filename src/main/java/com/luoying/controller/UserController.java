package com.luoying.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoying.common.ErrorCode;
import com.luoying.common.Result;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.User;
import com.luoying.model.dto.UserDTO;
import com.luoying.model.request.UserLoginRequest;
import com.luoying.model.request.UserRegisterRequest;
import com.luoying.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.luoying.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author 落樱的悔恨
 */
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8000"}, allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
        UserDTO userDTO = userService.userLogin(userAccount, userPassword, request);
        return Result.success(userDTO);
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
        //获取登录用户
        UserDTO loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "用户未登录");
        }
        Long loginUserId = loginUser.getId();
        //查询最新的用户信息
        User user = userService.getById(loginUserId);
        //  用户信息（脱敏）
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return Result.success(userDTO);
    }

    @PostMapping("/query")
    public Result userListQuery(@RequestBody User user, HttpServletRequest request) {
        // 1 鉴权，仅管理员可查询
        if (!userService.isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        // 2 查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(user.getUsername()), User::getUsername, user.getUsername());
        wrapper.like(StringUtils.isNotBlank(user.getUserAccount()), User::getUserAccount, user.getUserAccount());
        wrapper.like(StringUtils.isNotBlank(user.getPhone()), User::getPhone, user.getPhone());
        wrapper.like(StringUtils.isNotBlank(user.getEmail()), User::getEmail, user.getEmail());
        wrapper.like(StringUtils.isNotBlank(user.getAuthCode()), User::getAuthCode, user.getAuthCode());
        wrapper.like(StringUtils.isNotBlank(user.getProfile()), User::getProfile, user.getProfile());
        wrapper.like(user.getGender() != null, User::getGender, user.getGender());
        wrapper.like(user.getUserRole() != null, User::getUserRole, user.getUserRole());

        List<User> userList = userService.list(wrapper);
        List<UserDTO> collect = userList.stream().map(user1 -> {
            return BeanUtil.copyProperties(user1, UserDTO.class);
        }).collect(Collectors.toList());
        return Result.success(collect);
    }


    @GetMapping("/recommend")
    public Result usersRecommend(long currentPage, long pageSize, HttpServletRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        Page<User> page = userService.page(new Page<User>(currentPage, pageSize), wrapper);

        List<UserDTO> userDTOList = page.getRecords().stream().map(user1 -> {
            return BeanUtil.copyProperties(user1, UserDTO.class);
        }).collect(Collectors.toList());
        return Result.success(userDTOList);
    }

    @PostMapping("/delete")
    public Result userDelete(@RequestBody User user, HttpServletRequest request) {
        // 1 鉴权，仅管理员可删除
        if (!userService.isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH, "用户无权限");
        //2 删除
        if (user.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除用户的id不能为小于等于0");
        }
        boolean result = userService.removeById(user.getId());
        return Result.success(result);
    }

    @PostMapping("/update")
    public Result userUpdate(@RequestBody User user, HttpServletRequest request) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "修改用户为空");
        }
        //todo如果用户没有传任何要更新的值，直接抛异常
        UserDTO loginUser = userService.getLoginUser(request);
        //更新，前端传过来的数据有就更新，没有就保持默认
        int result = userService.updateUser(user, loginUser);
        return Result.success(result);
    }

    @GetMapping("/searchByTags")
    public Result searchByTags(@RequestParam(required = false) List<String> tags) {
        if (CollectionUtil.isEmpty(tags)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签为空");
        }
        return Result.success(userService.queryUsersByTagsByMemory(tags));
    }
}
