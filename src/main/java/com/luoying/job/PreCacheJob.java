package com.luoying.job;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoying.model.domain.User;
import com.luoying.model.vo.UserVO;
import com.luoying.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.luoying.constant.RedisConstant.*;

/**
 * 缓存预热任务
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;
    //重点用户
    List<Long> mainUserList = Arrays.asList(2l);

    //每天执行
    @Scheduled(cron = "0 37 16 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock(PRECACHEJOB_DOCACHE_LOCK);
        try {
            // 将waittime设置为0，那么意味着不会等待任何时间，直接尝试获取锁，如果获取到了锁，那么方法会返回true，否则返回false。
            // 只有一个线程能获取锁
            if (lock.tryLock(0,-1, TimeUnit.SECONDS)) {
                log.info("getLock");
                for (Long userId : mainUserList) {
                    //拼接key
                    String key = RECOMMEND_USUERS_KEY + userId;
                    //缓存20条
                    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
                    Page<User> page = userService.page(new Page<User>(1, 20), wrapper);

                    List<UserVO> userVOList = page.getRecords().stream().map(user1 -> {
                        return BeanUtil.copyProperties(user1, UserVO.class);
                    }).collect(Collectors.toList());
                    //将查询到的数据添加到缓存
                    String userDTOListJson = JSONUtil.toJsonStr(userVOList);
                    stringRedisTemplate.opsForValue().set(key, userDTOListJson, PRECACHEJOB_DOCACHE_LOCK_TTL, TimeUnit.MINUTES);
                }
                //只能释放自己加的锁

            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            if (lock.isHeldByCurrentThread()){
                log.info("getLock");
                lock.unlock();
            }
        }

    }

}
