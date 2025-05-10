package com.casestudy.BetCaseStudy.infrastructure.locking;

import com.casestudy.BetCaseStudy.domain.exception.BetCaseStudyLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public void executeWithLock(final String key, final Duration timeout, final Runnable task) {
        String lockValue = UUID.randomUUID().toString();
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, lockValue, timeout);

        if (Boolean.FALSE.equals(acquired)) {
            throw new BetCaseStudyLockException("Could not acquire Redis lock for key: " + key);
        }

        try {
            task.run();
        } finally {
            releaseLock(key, lockValue);
        }
    }

    private void releaseLock(final String key, final String lockValue) {
        final String luaScript = """
            if redis.call('get', KEYS[1]) == ARGV[1] then
                return redis.call('del', KEYS[1])
            else
                return 0
            end
            """;

        redisTemplate.execute(
                RedisScript.of(luaScript, Long.class),
                Collections.singletonList(key),
                lockValue
        );
    }
}

