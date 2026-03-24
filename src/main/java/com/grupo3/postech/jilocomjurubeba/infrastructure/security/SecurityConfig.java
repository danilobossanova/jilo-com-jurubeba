package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuracao de seguranca da aplicacao para ambiente de producao.
 *
 * <p>Define as regras de acesso aos endpoints da API com autenticacao JWT. Ativado para todos os
 * profiles exceto test.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Profile("!test")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    /**
     * Construtor com injecao de dependencia dos componentes de seguranca.
     *
     * @param jwtAuthFilter filtro de autenticacao JWT
     * @param userDetailsService servico de carregamento de detalhes do usuario
     * @param objectMapper serializador JSON para respostas de erro RFC 7807
     */
    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            UserDetailsService userDetailsService,
            ObjectMapper objectMapper) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    /**
     * Configura a cadeia de filtros de seguranca HTTP.
     *
     * <p>Define as seguintes regras:
     *
     * <ul>
     *   <li>CSRF desabilitado (API stateless)
     *   <li>Sessao STATELESS (sem cookies de sessao)
     *   <li>Endpoints publicos: health, actuator, swagger, login, GET de restaurantes e cardapios,
     *       criacao de usuario
     *   <li>Endpoints de cardapio (POST/PUT/PATCH/DELETE) restritos a DONO_RESTAURANTE e MASTER
     *   <li>Endpoints de usuarios e tipos de usuario restritos a DONO_RESTAURANTE e MASTER
     *   <li>Demais endpoints requerem autenticacao
     *   <li>Filtro JWT executado antes do UsernamePasswordAuthenticationFilter
     * </ul>
     *
     * @param http builder de configuracao de seguranca HTTP
     * @return cadeia de filtros de seguranca configurada
     * @throws Exception se houver erro na configuracao
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth.dispatcherTypeMatchers(
                                                DispatcherType.ERROR, DispatcherType.FORWARD)
                                        .permitAll()
                                        .requestMatchers("/error")
                                        .permitAll()
                                        .requestMatchers("/v1/health")
                                        .permitAll()
                                        .requestMatchers("/actuator/health")
                                        .permitAll()
                                        .requestMatchers(
                                                "/api-docs",
                                                "/api-docs/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.POST, "/v1/auth/login")
                                        .permitAll()
                                        .requestMatchers("/v1/auth/**")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/v1/cardapios/**")
                                        .permitAll()
                                        .requestMatchers(
                                                HttpMethod.GET, "/v1/restaurantes/*/cardapio/**")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/v1/restaurantes/**")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.POST, "/v1/usuarios")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.POST, "/v1/cardapios/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers(HttpMethod.PUT, "/v1/cardapios/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers(HttpMethod.PATCH, "/v1/cardapios/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers(HttpMethod.DELETE, "/v1/cardapios/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers(
                                                HttpMethod.POST, "/v1/restaurantes/*/cardapio/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers(
                                                HttpMethod.PUT, "/v1/restaurantes/*/cardapio/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers(
                                                HttpMethod.PATCH, "/v1/restaurantes/*/cardapio/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers(
                                                HttpMethod.DELETE, "/v1/restaurantes/*/cardapio/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .requestMatchers("/v1/usuarios/**", "/v1/tipos-usuario/**")
                                        .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                                        .anyRequest()
                                        .authenticated())
                .exceptionHandling(
                        ex ->
                                ex.authenticationEntryPoint(problemAuthEntryPoint())
                                        .accessDeniedHandler(problemAccessDeniedHandler()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Cria o bean de codificador de senhas usando BCrypt.
     *
     * @return instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cria o provider de autenticacao baseado em DAO (banco de dados).
     *
     * <p>Configura o {@link UserDetailsService} e o {@link PasswordEncoder} para autenticacao via
     * credenciais armazenadas no banco.
     *
     * @return provider de autenticacao configurado
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    /**
     * Cria o gerenciador de autenticacao com o provider DAO.
     *
     * @return instancia de {@link AuthenticationManager} configurada
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }

    /**
     * Cria o entry point para erros de autenticacao (HTTP 401).
     *
     * <p>Retorna uma resposta no formato RFC 7807 ({@link ProblemDetail}) quando o token JWT esta
     * ausente, invalido ou expirado.
     *
     * @return entry point de autenticacao com resposta ProblemDetail
     */
    @Bean
    public AuthenticationEntryPoint problemAuthEntryPoint() {
        return (request, response, authException) ->
                writeProblem(
                        response,
                        request,
                        HttpStatus.UNAUTHORIZED,
                        "Unauthorized",
                        "Token ausente, invalido ou expirado",
                        "https://jilo.com/problems/unauthorized");
    }

    /**
     * Cria o handler para erros de acesso negado (HTTP 403).
     *
     * <p>Retorna uma resposta no formato RFC 7807 ({@link ProblemDetail}) quando o usuario
     * autenticado nao possui permissao para acessar o recurso.
     *
     * @return handler de acesso negado com resposta ProblemDetail
     */
    @Bean
    public AccessDeniedHandler problemAccessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                writeProblem(
                        response,
                        request,
                        HttpStatus.FORBIDDEN,
                        "Forbidden",
                        "Voce nao tem permissao para acessar este recurso",
                        "https://jilo.com/problems/forbidden");
    }

    /**
     * Escreve uma resposta de erro no formato RFC 7807 (ProblemDetail) no response HTTP.
     *
     * <p>Utilizado internamente pelos handlers de autenticacao e autorizacao para padronizar
     * respostas de erro de seguranca.
     *
     * @param response resposta HTTP onde o ProblemDetail sera escrito
     * @param request requisicao HTTP para extrair o URI da instancia
     * @param status codigo de status HTTP (ex: 401, 403)
     * @param title titulo do erro
     * @param detail descricao detalhada do erro
     * @param type URI do tipo de erro
     * @throws IOException se houver erro ao escrever no response
     */
    private void writeProblem(
            HttpServletResponse response,
            HttpServletRequest request,
            HttpStatus status,
            String title,
            String detail,
            String type)
            throws IOException {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create(type));
        pd.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE + ";charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), pd);
    }
}
