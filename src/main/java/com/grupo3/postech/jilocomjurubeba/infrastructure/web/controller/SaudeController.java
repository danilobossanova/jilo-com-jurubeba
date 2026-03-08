package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class SaudeController {

    @GetMapping("/health")
    public Map<String, Object> health() {

        return Map.of(
            "status", "UP",
            "versao", "1.0.0",
            "timestamp", Instant.now().toString()
        );
    }
}
