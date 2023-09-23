package com.luoying.job;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoying.model.domain.User;
import com.luoying.model.dto.UserDTO;
import com.luoying.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.luoying.constant.RedisConstant.RECOMMEND_USUERS_KEY;

/**
 * 缓存预热任务
 */
@Component
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //重点用户
    List<Long> mainUserList = Arrays.asList(2l);

    //每天执行
    @Scheduled(cron = "0 11 13 * * *")
    public void doCacheRecommendUser() {
        for (Long userId : mainUserList) {
            //拼接key
            String key = RECOMMEND_USUERS_KEY + userId;
            //缓存20条
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
            Page<User> page = userService.page(new Page<User>(1, 20), wrapper);

            List<UserDTO> userDTOList = page.getRecords().stream().map(user1 -> {
                return BeanUtil.copyProperties(user1, UserDTO.class);
            }).collect(Collectors.toList());
            //将查询到的数据添加到缓存
            String userDTOListJson = JSONUtil.toJsonStr(userDTOList);
            stringRedisTemplate.opsForValue().set(key, userDTOListJson, 3, TimeUnit.MINUTES);
        }
    }

}
