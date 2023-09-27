package com.luoying.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class TeamUpdateRequest {

    private Long id;

    private String name;

    private String description;

    private Integer maxNum;

    private Date expireTime;

    private Integer status;

    private String password;
}
