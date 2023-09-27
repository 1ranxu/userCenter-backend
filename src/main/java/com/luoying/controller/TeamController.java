package com.luoying.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoying.common.ErrorCode;
import com.luoying.common.Result;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.Team;
import com.luoying.model.request.TeamAddRequest;
import com.luoying.model.request.TeamJoinRequest;
import com.luoying.model.request.TeamQueryRequest;
import com.luoying.model.request.TeamUpdateRequest;
import com.luoying.model.vo.TeamUserVO;
import com.luoying.model.vo.UserVO;
import com.luoying.service.TeamService;
import com.luoying.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 队伍接口
 */
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8000"}, allowCredentials = "true")
@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;


    @PostMapping("/add")
    public Result addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"teamAddRequest空值");
        }
        Team team = BeanUtil.copyProperties(teamAddRequest, Team.class);
        UserVO loginUser = userService.getLoginUser(request);
        long teamId = teamService.addTeam(team, loginUser);
        return Result.success(teamId);
    }


    @PostMapping("/delete")
    public Result deleteTeam(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败");
        }
        return Result.success(true);
    }


    @PostMapping("/update")
    public Result updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"teamUpdateRequest空值");
        }
        UserVO loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest,loginUser);

        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍失败");
        }
        return Result.success(true);
    }


    @GetMapping("/get")
    public Result getTeamById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id空值");
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        return Result.success(team);
    }

    @GetMapping("/list")
    public Result getTeamList(TeamQueryRequest teamQueryRequest,HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.listTeams(teamQueryRequest,isAdmin);
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
    public Result joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,HttpServletRequest request){
        if (teamJoinRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"teamJoinRequest空值");
        }
        UserVO loginUser = userService.getLoginUser(request);

        boolean result=teamService.joinTeam(teamJoinRequest,loginUser);

        if (!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入队伍失败");
        }

        return Result.success(true);
    }
}
