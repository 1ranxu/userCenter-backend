package com.luoying.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.common.ErrorCode;
import com.luoying.exception.BusinessException;
import com.luoying.mapper.TeamMapper;
import com.luoying.model.domain.Team;
import com.luoying.model.domain.User;
import com.luoying.model.domain.UserTeam;
import com.luoying.model.enums.TeamStatusEnum;
import com.luoying.model.request.*;
import com.luoying.model.vo.TeamUserVO;
import com.luoying.model.vo.UserVO;
import com.luoying.service.TeamService;
import com.luoying.service.UserService;
import com.luoying.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.luoying.constant.RedisConstant.JOINTEAM_DOJOIN_LOCK;
import static com.luoying.constant.RedisConstant.PRECACHEJOB_DOCACHE_LOCK;

/**
 * @author 落樱的悔恨
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2023-09-23 18:41:01
 */
@Service
@Slf4j
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {
    private static final String SALT = "yingluo";

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, UserVO loginUser) {
        //1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "team空值");
        }
        // 2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "未登录");
        }
        // 3. 校验信息
        //    1. 队伍人数 > 1 且 <= 10
        Integer maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum <= 1 || maxNum > 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //    2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //    3. 描述 <= 512
        String description = team.getDescription();
        if (description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        //    4. status 是否公开（int）不传默认为 0（公开）
        Integer status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //    5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum) && (StringUtils.isBlank(password) || password.length() > 32)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码设置不满足要求");
        }
        //    6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        Long teamId = team.getId();
        // 只有一个线程能获取锁
        RLock lock = redissonClient.getLock(JOINTEAM_DOJOIN_LOCK+":"+teamId);
        //    7. 校验用户最多创建 5 个队伍
        try {
            while (true){
                if (lock.tryLock(0,-1, TimeUnit.SECONDS)) {
                    log.info("getLock");
                    LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
                    Long userId = loginUser.getId();
                    queryWrapper.eq(Team::getUserId, userId);
                    long hasTeamNum = this.count(queryWrapper);
                    if (hasTeamNum >= 5) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "单个用户最多创建5个用户");
                    }
                    // 4. 插入队伍信息到队伍表
                    team.setUserId(userId);
                    boolean result = this.save(team);

                    if (!result || teamId == null) {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
                    }
                    // 5. 插入用户 -队伍关系到关系表
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    result = userTeamService.save(userTeam);

                    if (!result) {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建用户-队伍失败");
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            if (lock.isHeldByCurrentThread()){
                log.info("getLock");
                lock.unlock();
            }
        }
        return teamId;
    }


    @Override
    public List<TeamUserVO> listTeams(TeamQueryRequest teamQueryRequest, HttpServletRequest request, boolean isAdmin) {
        LambdaQueryWrapper<Team> teamWrapper = new LambdaQueryWrapper<>();
        //组合队伍查询条件
        if (teamQueryRequest != null) {
            //根据队伍id查询
            Long id = teamQueryRequest.getId();
            teamWrapper.eq(id != null && id > 0, Team::getId, id);
            //根据id列表查询
            List<Long> ids = teamQueryRequest.getIds();
            teamWrapper.in(CollectionUtil.isNotEmpty(ids),Team::getId,ids);
            //根据搜索内容查询
            String searchText = teamQueryRequest.getSearchText();
            teamWrapper
                    .like(StringUtils.isNotBlank(searchText), Team::getName, searchText)
                    .or()
                    .like(StringUtils.isNotBlank(searchText), Team::getDescription, searchText);
            //根据队伍名查询
            String teamName = teamQueryRequest.getName();
            teamWrapper.like(StringUtils.isNotBlank(teamName), Team::getName, teamName);
            //根据队伍描述查询
            String description = teamQueryRequest.getDescription();
            teamWrapper.like(StringUtils.isNotBlank(description), Team::getDescription, description);
            //根据队伍最大人数查询
            Integer maxNum = teamQueryRequest.getMaxNum();
            teamWrapper.eq(maxNum != null && maxNum > 0 && maxNum <= 20, Team::getMaxNum, maxNum);

            //根据创建人id查询
            Long userId = teamQueryRequest.getUserId();
            teamWrapper.eq(userId != null && userId > 0, Team::getUserId, userId);
            //不展示已过期队伍
            teamWrapper.and(qw -> qw.isNull(Team::getExpireTime).or().gt(Team::getExpireTime, new Date()));
            //根据队伍状态查询
            Integer status = teamQueryRequest.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                //如果队伍状态没传，公开加密都可以查
                teamWrapper.in(Team::getStatus, TeamStatusEnum.PUBLIC.getValue(),TeamStatusEnum.SECRET.getValue());
            }else {
                //如果队伍状态传了,就根据队伍状态查
                teamWrapper.eq(Team::getStatus, statusEnum.getValue());
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
                throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
            }

        }
        //查询队伍
        List<Team> teamList = this.list(teamWrapper);
        if (CollectionUtil.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        //关联查询用户信息
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        // 关联查询创建人的用户信息
        for (Team team : teamList) {
            TeamUserVO teamUserVO = BeanUtil.copyProperties(team, TeamUserVO.class);
            User user = userService.getById(team.getUserId());
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            teamUserVO.setCreateUser(userVO);
            teamUserVOList.add(teamUserVO);
        }
        //获取所有队伍id
        final List<Long> teamIdList = teamUserVOList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        //判断当前用户是否已加入队伍
        LambdaQueryWrapper<UserTeam> userTeamQueryWrapper = new LambdaQueryWrapper<>();
        try {
            UserVO loginUser = userService.getLoginUser(request);
            userTeamQueryWrapper.eq(UserTeam::getUserId, loginUser.getId());
            userTeamQueryWrapper.in(UserTeam::getTeamId, teamIdList);
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            // 已加入的队伍 id 集合
            Set<Long> hasJoinTeamIdSet = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamUserVOList.forEach(team -> {
                boolean hasJoin = hasJoinTeamIdSet.contains(team.getId());
                team.setHasJoin(hasJoin);
            });
        } catch (Exception e) {
        }
        //查询已加入队伍的所有用户
        userTeamQueryWrapper = new LambdaQueryWrapper<>();
        userTeamQueryWrapper.in(UserTeam::getTeamId, teamIdList);
        List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
        // 队伍 id => 加入这个队伍的用户列表
        Map<Long, List<UserTeam>> teamIdUserTeamMap = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        //给每个队伍设置队伍人数
        //getOrDefault是map的一个方法，如果这个键teamId的vlaue为null没有队员，就给个空集合
        teamUserVOList.forEach(team -> team.setHasJoinNum(teamIdUserTeamMap.getOrDefault(team.getId(), new ArrayList<>()).size()));
        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, UserVO loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamUpdateRequest空值");
        }
        //查询队伍是否存在
        Long id = teamUpdateRequest.getId();
        Team dbTeam = getTeamById(id);
        //只有管理员或者创建人才能修改
        if (loginUser.getId() != dbTeam.getUserId() && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        //用户如果修改的是公开队伍，密码不管有没有，都设置为空
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (TeamStatusEnum.PUBLIC.equals(statusEnum)) {
            teamUpdateRequest.setPassword("");
        }
        //如果修改的是加密队伍，密码不能为空
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
            }
        }
        return this.updateById(BeanUtil.copyProperties(teamUpdateRequest, Team.class));
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, UserVO loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamJoinRequest空值");
        }
        //加入的队伍必须要存在
        Long teamId = teamJoinRequest.getTeamId();
        Team team = getTeamById(teamId);

        //只能加入未过期的队伍
        if (team.getExpireTime() != null && new Date().after(team.getExpireTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        //不能加入私有队伍
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(team.getStatus());
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能加入私有队伍");
        }
        //加入加密队伍，密码必须匹配
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            String password = teamJoinRequest.getPassword();
            if (StringUtils.isBlank(password) || !StringUtils.equals(team.getPassword(), password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码错误");
            }
        }
        //该用户已加入队伍的数量
        Long userId = loginUser.getId();
        LambdaQueryWrapper<UserTeam> userTeamWrapper = new LambdaQueryWrapper<>();
        userTeamWrapper.eq(userId != null && userId > 0, UserTeam::getUserId, userId);
        long count = userTeamService.count(userTeamWrapper);
        if (count >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多同时加入5个队伍");
        }
        //只能加入未满的队伍
        count = countTeamUserByTeamId(teamId);
        if (count == team.getMaxNum()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数已满");
        }
        //不能重复加入已加入的队伍
        userTeamWrapper = new LambdaQueryWrapper<>();
        userTeamWrapper.eq(userId != null && userId > 0, UserTeam::getUserId, userId);
        userTeamWrapper.eq(teamId != null && teamId > 0, UserTeam::getTeamId, teamId);
        count = userTeamService.count(userTeamWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复加入已加入的队伍");
        }
        //新增用户-关系表信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, UserVO loginUser) {
        //1. 校验请求参数
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamQuitRequest空值");
        }
        // 2. 校验队伍是否存在
        Long teamId = teamQuitRequest.getTeamId();
        Team team = getTeamById(teamId);
        // 3. 校验我是否已加入队伍
        Long userId = loginUser.getId();
        LambdaQueryWrapper<UserTeam> userTeamWrapper = new LambdaQueryWrapper<>();
        userTeamWrapper.eq(userId != null && userId > 0, UserTeam::getUserId, userId);
        userTeamWrapper.eq(UserTeam::getTeamId, teamId);
        long count = userTeamService.count(userTeamWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "非队伍成员");
        }
        //查询队伍人数
        count = countTeamUserByTeamId(teamId);
        //4.退出队伍
        if (count == 1) {
            //4.1.退出队伍只剩一人，队伍解散
            //删除队伍
            this.removeById(teamId);
        } else {
            //4.2. 退出队伍还有其他人
            //如果是队长退出队伍，权限转移给第二早加入的用户 —— 先来后到
            if (team.getUserId().equals(userId)) {
                //取 id 最小的 2 条数据
                userTeamWrapper = new LambdaQueryWrapper<>();
                userTeamWrapper.eq(UserTeam::getTeamId, teamId);
                userTeamWrapper.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamWrapper);
                if (CollectionUtil.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                //取出第二早加入队伍的用户
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                //更新当前队伍的队长
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队长失败");
                }
            }
        }
        //删除当前用户的用户-队伍关系，不管是一个人，队长，非队长，只要调用这个方法都要移除用户-队伍关系
        userTeamWrapper = new LambdaQueryWrapper<>();
        userTeamWrapper.eq(UserTeam::getTeamId, teamId);
        userTeamWrapper.eq(userId != null && userId > 0, UserTeam::getUserId, userId);
        return userTeamService.remove(userTeamWrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(TeamDeleteRequest teamDeleteRequest, UserVO loginUser) {
        //1. 校验请求参数
        if (teamDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "teamDeleteRequestko空值");
        }
        // 2. 校验队伍是否存在
        Long teamId = teamDeleteRequest.getTeamId();
        Team team = getTeamById(teamId);
        // 3. 校验你是不是队伍的队长
        if (!team.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非队长无权解散队伍");
        }
        // 4. 移除所有加入队伍的关联信息
        LambdaQueryWrapper<UserTeam> userTeamWrapper = new LambdaQueryWrapper<>();
        userTeamWrapper.eq(UserTeam::getTeamId, teamId);
        boolean result = userTeamService.remove(userTeamWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "移除所有加入队伍的关联信息失败");
        }
        // 5. 删除队伍
        result = this.removeById(teamId);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败");
        }
        return true;
    }

    /**
     * 根据队伍id查询用户关系表，获取队伍人数
     *
     * @param teamId
     * @return
     */
    private long countTeamUserByTeamId(Long teamId) {
        LambdaQueryWrapper<UserTeam> userTeamWrapper = new LambdaQueryWrapper<>();
        userTeamWrapper.eq(UserTeam::getTeamId, teamId);
        return userTeamService.count(userTeamWrapper);
    }

    /**
     * 根据teamId查询team
     *
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        return team;
    }
}




