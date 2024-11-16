package br.com.devtt.enterprise.infrastructure.adapters.gateway.cache;

import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
@Qualifier("RedisCacheGateway")
@Slf4j
public class RedisCacheGatewayImpl implements CacheGateway {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void put(String key, Object value) {
        if (value == null) {
            log.warn("[REDIS::PUT] Trying to put null value in cache with key '{}'", key);
            return;
        }

        log.info("[REDIS::PUT] Putting key '{}' with value '{}' in cache", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(String key, Object value, Long ttl) {
        if (value == null) {
            log.warn("[REDIS::PUT] Trying to put null value in cache with key '{}' and ttl '{}'", key, ttl);
            return;
        }

        if (ttl == null) {
            log.warn("[REDIS::PUT] Trying to put key '{}' with value '{}' in cache with null ttl", key, value);
            return;
        }

        log.info("[REDIS::PUT] Putting key '{}' with value '{}' in cache with ttl '{}'", key, value, ttl);
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public Object get(String key) {
        var value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            log.info("[REDIS::MISS] Key '{}' not found in cache", key);
        } else {
            log.info("[REDIS::HIT] Key '{}' found in cache with value '{}'", key, value);
        }

        return value;
    }

    @Override
    public void delete(String key) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.warn("[REDIS::CLEAR] Key '{}' not found in cache", key);
            return;
        }

        log.info("[REDIS::CLEAR] Deleting key '{}' from cache", key);
        redisTemplate.delete(key);
    }

    @Override
    public void deleteAllFrom(String pattern) {
        Set<String> keys = redisTemplate.keys("*" + pattern + "*");

        if (keys == null || keys.isEmpty()) {
            log.warn("[REDIS::CLEAR] No keys found in cache with pattern '{}'", pattern);
            return;
        }

        log.info("[REDIS::CLEAR] Deleting all keys with pattern '{}' from cache", pattern);
        redisTemplate.delete(keys);
    }
}