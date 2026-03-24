package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.AutenticacaoGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.CriptografiaSenhaGateway;

/**
 * Configuracao de seguranca para testes.
 *
 * <p>Desabilita toda autenticacao/autorizacao e fornece beans mock para que os testes de integracao
 * possam acessar os endpoints sem necessidade de token JWT.
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    public AuthenticationManager testAuthenticationManager() {
        return authentication -> authentication;
    }

    @Bean
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CriptografiaSenhaGateway testCriptografiaSenhaGateway() {
        return senha -> "$2a$10$testHash";
    }

    @Bean
    public AutenticacaoGateway testAutenticacaoGateway() {
        return (email, senha) -> "test-jwt-token";
    }
}
