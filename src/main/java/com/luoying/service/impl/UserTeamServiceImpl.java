package com.luoying.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.model.domain.UserTeam;
import com.luoying.service.UserTeamService;
import com.luoying.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 落樱的悔恨
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2023-09-23 18:41:01
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




