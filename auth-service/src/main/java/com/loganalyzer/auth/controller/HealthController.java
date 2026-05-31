package com.loganalyzer.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class HealthController {

    @GetMapping("/health")
    public String health(){
        return "Auth Service Application is running on port 8081";
    }

    @GetMapping("/test")
    public String test() {
        return "Authenticated";
    }
}
