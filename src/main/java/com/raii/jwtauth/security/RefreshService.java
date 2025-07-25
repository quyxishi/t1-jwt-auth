package com.raii.jwtauth.security;

import com.raii.jwtauth.security.props.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshService {
    private static final String PREFIX = "refresh_token:";

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, String> redisTemplate;

    public void storeRefreshToken(String login, String refreshToken) {
        final var key = PREFIX + login;
        redisTemplate.opsForValue().set(key, refreshToken);
        redisTemplate.expire(key, jwtProperties.getRefreshExpiry(), TimeUnit.SECONDS);
    }

    public boolean isActiveRefreshToken(String login, String providedToken) {
        final var storedToken = redisTemplate.opsForValue().get(PREFIX + login);
        return storedToken != null && storedToken.equals(providedToken);
    }

    public void revokeRefreshToken(String login) {
        redisTemplate.delete(PREFIX + login);
    }
}
