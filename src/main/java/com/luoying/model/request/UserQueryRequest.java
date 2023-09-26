package com.luoying.model.request;

import com.luoying.common.PageRequest;
import lombok.Data;
@Data
public class UserQueryRequest extends PageRequest {

    private Long id;

    private String userAccount;

    private String username;

    private String avatarUrl;

    private Integer gender;

    private String phone;

    private String email;

    private Integer userStatus;

    private Integer userRole;

    private String authCode;

    private String tags;

    private String profile;
}
