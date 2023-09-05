package com.luoying.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 落樱的悔恨
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -2043028023742840036L;
    private String userAccount;
    private String userPassword;
}
