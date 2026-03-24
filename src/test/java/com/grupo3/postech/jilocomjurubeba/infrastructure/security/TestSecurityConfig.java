package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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
 * <p>Sobrescreve os beans de seguranca de producao com versoes simplificadas que permitem acesso
 * irrestrito aos endpoints durante os testes de integracao. Usa {@code @Primary} para garantir que
 * estes beans tem prioridade sobre os de producao.
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    @Primary
    public AuthenticationManager testAuthenticationManager() {
        return authentication -> authentication;
    }

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public CriptografiaSenhaGateway testCriptografiaSenhaGateway() {
        return senha -> "$2a$10$testHash";
    }

    @Bean
    @Primary
    public AutenticacaoGateway testAutenticacaoGateway() {
        return (email, senha) -> "test-jwt-token";
    }
}
