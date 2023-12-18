package com.community.amkorea.global.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
  private final RedisTemplate<String, Object> redisTemplate;

  public String getData(String key) {
    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    return (String) valueOperations.get(key);
  }

  public void setDataExpire(String key, String value, Long expiredTime) {
    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    valueOperations.set(key, value, Duration.ofMillis(expiredTime));
  }

  public boolean existData(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public void deleteData(String key) {
    redisTemplate.delete(key);
  }

}
