package com.clouddevopshub.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {
    
    @Value("${spring.application.name:spring-boot-demo}")
    private String applicationName;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", applicationName);
        response.put("message", "Welcome to CloudDevOpsHub Spring Boot Demo!");
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", applicationName);
        health.put("version", appVersion);
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", applicationName);
        info.put("version", appVersion);
        info.put("java.version", System.getProperty("java.version"));
        info.put("description", "Spring Boot Demo for CI/CD Pipeline by CloudDevOpsHub");
        return ResponseEntity.ok(info);
    }
}
