package com.raizumi.component.permission.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "raizumi.permission")
public class Security {

    private String module;

    private CacheType cacheType = CacheType.CONTEXT;

    private RedisConfiguration redisConfiguration;

    public  String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public CacheType getCacheType() {
        return cacheType;
    }


    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }

    public RedisConfiguration getRedisConfiguration() {
        return redisConfiguration;
    }

    public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }


    public enum  CacheType {

        CONTEXT,

        REDIS;
    }
}
