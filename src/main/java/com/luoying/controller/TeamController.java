package com.luoying.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoying.common.ErrorCode;
import com.luoying.common.Result;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.Team;
import com.luoying.model.domain.UserTeam;
import com.luoying.model.request.*;
import com.luoying.model.vo.TeamUserVO;
import com.luoying.model.vo.UserVO;
import com.luoying.service.TeamService;
import com.luoying.service.UserService;
import com.luoying.service.UserTeamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 队伍接口
 */
@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/add")
    public Result addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamAddRequest空值");
        }
        Team team = BeanUtil.copyProperties(teamAddRequest, Team.class);
        UserVO loginUser = userService.getLoginUser(request);
        long teamId = teamService.addTeam(team, loginUser);
        return Result.success(teamId);
    }


    @PostMapping("/update")
    public Result updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamUpdateRequest空值");
        }
        UserVO loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);

        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍失败");
        }
        return Result.success(true);
    }


    @GetMapping("/get")
    public Result getTeamById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id空值");
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        return Result.success(team);
    }

    @GetMapping("/list")
    public Result getTeamList(TeamQueryRequest teamQueryRequest, HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.listTeams(teamQueryRequest, isAdmin);
        return Result.success(teamList);
    }

    @GetMapping("/list/page")
    public Result getTeamListByPage(TeamQueryRequest teamQueryRequest) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        Team newTeam = BeanUtil.copyProperties(teamQueryRequest, Team.class);
        IPage<Team> page = new Page<>(teamQueryRequest.getPage(), teamQueryRequest.getPageSize());
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>(newTeam);
        teamService.page(page, queryWrapper);
        return Result.success(page.getRecords());
    }

    @PostMapping("/join")
    public Result joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamJoinRequest空值");
        }
        UserVO loginUser = userService.getLoginUser(request);

        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);

        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "加入队伍失败");
        }

        return Result.success(true);
    }


    @PostMapping("/quit")
    public Result quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamQuitRequest空值");
        }
        UserVO loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "退出队伍失败");
        }
        return Result.success(true);
    }

    @PostMapping("/delete")
    public Result deleteTeam(@RequestBody TeamDeleteRequest teamDeleteRequest, HttpServletRequest request) {
        if (teamDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamDeleteRequestko空值");
        }
        UserVO loginUser = userService.getLoginUser(request);
        boolean result = teamService.deleteTeam(teamDeleteRequest,loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队长解散队伍失败");
        }
        return Result.success(true);
    }

    /**
     * 获取我创建的队伍
     * @param teamQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/mine/create")
    public Result getMyCreateTeamList(TeamQueryRequest teamQueryRequest, HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        //获取当前登录用户
        UserVO loginUser = userService.getLoginUser(request);
        //设置登录用户id
        teamQueryRequest.setUserId(loginUser.getId());
        //根据登录用户id查询该用户创建的队伍
        List<TeamUserVO> teamList = teamService.listTeams(teamQueryRequest, true);
        return Result.success(teamList);
    }

    /**
     * 获取我加入的队伍
     * @param teamQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/mine/join")
    public Result getMyJoinTeamList(TeamQueryRequest teamQueryRequest, HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        //获取当前登录用户
        UserVO loginUser = userService.getLoginUser(request);
        //根据登录用户id查询该用户的用户-队伍关系
        LambdaQueryWrapper<UserTeam> userTeamWrapper=new LambdaQueryWrapper<>();
        userTeamWrapper.eq(UserTeam::getUserId,loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(userTeamWrapper);
        //把用户-队伍关系根据队伍id分组，得到队伍-用户的映射map
        Map<Long, List<UserTeam>> listMap = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        //从map中取出的keySet就是当前用户加入的队伍id列表
        ArrayList<Long> teamIdList = new ArrayList<>(listMap.keySet());
        //设置队伍id列表
        teamQueryRequest.setIds(teamIdList);
        //根据队伍id列表查询当前用户加入的队伍
        List<TeamUserVO> teamList = teamService.listTeams(teamQueryRequest, true);
        return Result.success(teamList);
    }

}
