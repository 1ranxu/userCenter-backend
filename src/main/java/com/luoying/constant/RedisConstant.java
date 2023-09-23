package com.luoying.constant;

public interface RedisConstant {
    String RECOMMEND_USUERS_KEY = "yingluo:user:recommend:";
    Long RECOMMEND_USUERS_KEY_TTL = 3L;

    String PRECACHEJOB_DOCACHE_LOCK = "yingluo:preCacheJob:docache:lock";
}
