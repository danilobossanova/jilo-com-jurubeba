package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        // AuthenticationManager vazio — não autentica nada, só satisfaz a dependência
        return authentication -> {
            throw new UnsupportedOperationException("AuthenticationManager stub — não usado em testes");
        };
    }
}
