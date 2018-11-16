package com.oose.group6.backend.beans.manager.impl;

import com.oose.group6.backend.beans.manager.SessionManager;
import com.oose.group6.backend.beans.model.HttpSession;
import com.oose.group6.backend.common.FusionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis implementation of session manager
 */
@Component
public class RedisSessionManager implements SessionManager {

    private RedisTemplate<String, Long> redis;

    @Autowired
    public void setRedis(@Qualifier("redisTemplate") RedisTemplate redis) {
        this.redis = redis;
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }

    public HttpSession createSession(long userId) {
        String sessionId = UUID.randomUUID().toString().replace("-", "");
        HttpSession session = new HttpSession(userId, sessionId);
        redis.boundValueOps(sessionId).set(userId, FusionConfig.SESSION_EXPIRE_HR, TimeUnit.HOURS);
        return session;
    }

    public HttpSession getSession(String sessionId) {
        if (sessionId == null || sessionId.length() == 0) {
            return null;
        }
        Long userId = redis.boundValueOps(sessionId).get();
        return new HttpSession(userId, sessionId);
    }

    public void deleteSession(String sessionId) {

        redis.delete(sessionId);
    }
}
