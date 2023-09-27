package com.luoying.utils;

import com.luoying.model.vo.UserVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.luoying.constant.UserConstant.USER_LOGIN_STATE;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserVO userVO = (UserVO) request.getSession().getAttribute(USER_LOGIN_STATE);
        // 1.判断是否需要拦截
        if (userVO == null) {
            // 用户未登录，需要拦截，设置状态码
            response.setStatus(401);
            // 拦截
            return false;
        }
        // 有用户，则放行
        return true;
    }
}