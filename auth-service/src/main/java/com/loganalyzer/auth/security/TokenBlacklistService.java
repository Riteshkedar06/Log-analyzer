package com.loganalyzer.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;



@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;

    public void blacklistToken(
            String tokenId,
            long expirationMillis
    ) {

        redisTemplate.opsForValue()
                .set(
                        tokenId,
                        "blacklisted",
                        expirationMillis,
                        TimeUnit.MILLISECONDS
                );
    }

    public boolean isBlacklisted(
            String tokenId
    ) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(tokenId)
        );
    }
}