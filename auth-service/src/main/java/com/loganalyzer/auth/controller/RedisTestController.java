package com.loganalyzer.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test() {

        redisTemplate.opsForValue()
                .set("test-key", "working");

        return redisTemplate.opsForValue()
                .get("test-key");
    }
}