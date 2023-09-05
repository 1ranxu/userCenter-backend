package com.luoying.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 落樱的悔恨
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -1847968683181293735L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String authCode;
}
