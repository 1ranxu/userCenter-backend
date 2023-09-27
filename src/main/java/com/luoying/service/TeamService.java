package com.luoying.service;

import com.luoying.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoying.model.request.TeamJoinRequest;
import com.luoying.model.request.TeamQueryRequest;
import com.luoying.model.request.TeamUpdateRequest;
import com.luoying.model.vo.TeamUserVO;
import com.luoying.model.vo.UserVO;

import java.util.List;

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
    long addTeam(Team team, UserVO loginUser);

    /**
     * 搜索队伍
     * @param teamQueryRequest
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQueryRequest teamQueryRequest, boolean isAdmin);

    /**
     * 修改队伍
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, UserVO loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, UserVO loginUser);

}
