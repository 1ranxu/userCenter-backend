package com.luoying.service;

import com.luoying.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoying.model.domain.User;
import com.luoying.model.dto.UserDTO;

/**
* @author 落樱的悔恨
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2023-09-23 18:41:01
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     * @param team
     * @return
     */
    long addTeam(Team team, UserDTO loginUser);
}
