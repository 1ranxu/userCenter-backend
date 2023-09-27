package com.luoying.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class TeamJoinRequest {
    private Long teamId;

    private String password;

}
