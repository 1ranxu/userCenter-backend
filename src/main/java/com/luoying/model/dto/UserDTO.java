package com.luoying.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {
    private Long id;

    private String userAccount;

    private String username;

    private String avatarUrl;

    private Integer gender;

    private String phone;

    private String email;

    private Integer userStatus;

    private Date createTime;

    private Integer userRole;

    private String authCode;

    private String tags;

    private String profile;

    private static final long serialVersionUID = 1L;
}
