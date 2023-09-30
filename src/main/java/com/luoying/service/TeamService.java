package com.luoying.service;

import com.luoying.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luoying.model.request.*;
import com.luoying.model.vo.TeamUserVO;
import com.luoying.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 落樱的悔恨
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2023-09-23 18:41:01
 */
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     *
     * @param team
     * @return
     */
    long addTeam(Team team, UserVO loginUser);

    /**
     * 搜索队伍
     *
     * @param teamQueryRequest
     * @param request
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQueryRequest teamQueryRequest, HttpServletRequest request, boolean isAdmin);

    /**
     * 修改队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, UserVO loginUser);

    /**
     * 加入队伍
     *
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, UserVO loginUser);

    /**
     * 退出队伍
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, UserVO loginUser);

    /**
     * 队长解散队伍
     *
     * @param teamDeleteRequest
     * @param loginUser
     * @return
     */
    boolean deleteTeam(TeamDeleteRequest teamDeleteRequest, UserVO loginUser);

}
