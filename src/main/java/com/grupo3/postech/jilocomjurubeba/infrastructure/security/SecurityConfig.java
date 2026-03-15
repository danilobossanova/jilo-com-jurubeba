package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final UserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;

  public SecurityConfig(
      JwtAuthFilter jwtAuthFilter,
      UserDetailsService userDetailsService,
      ObjectMapper objectMapper) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.userDetailsService = userDetailsService;
    this.objectMapper = objectMapper;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(
            csrf ->
                csrf.ignoringRequestMatchers(
                    "/auth/**",
                    "/usuarios",
                    "/usuarios/**",
                    "/tipos-usuario/**",
                    "/cardapios/**",
                    "/restaurantes/**"))
        .cors(Customizer.withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD)
                    .permitAll()
                    .requestMatchers("/error")
                    .permitAll()
                    .requestMatchers("/v1/health")
                    .permitAll()
                    .requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers("/api-docs", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/login")
                    .permitAll()
                    .requestMatchers("/auth/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/cardapios/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/restaurantes/*/cardapio/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/restaurantes/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/usuarios")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/cardapios/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers(HttpMethod.PUT, "/cardapios/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers(HttpMethod.PATCH, "/cardapios/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers(HttpMethod.DELETE, "/cardapios/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers(HttpMethod.POST, "/restaurantes/*/cardapio/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers(HttpMethod.PUT, "/restaurantes/*/cardapio/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers(HttpMethod.PATCH, "/restaurantes/*/cardapio/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers(HttpMethod.DELETE, "/restaurantes/*/cardapio/**")
                    .hasAnyRole("DONO_RESTAURANTE", "MASTER")
                    .requestMatchers("/usuarios/**", "/tipos-usuario/**")
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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider p = new DaoAuthenticationProvider();
    p.setUserDetailsService(userDetailsService);
    p.setPasswordEncoder(passwordEncoder());
    return p;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(daoAuthenticationProvider());
  }

  @Bean
  public AuthenticationEntryPoint problemAuthEntryPoint() {
    return (request, response, authException) ->
        writeProblem(
            response,
            request,
            HttpStatus.UNAUTHORIZED,
            "Unauthorized",
            "Token ausente, inválido ou expirado",
            "https://jilo.com/problems/unauthorized");
  }

  @Bean
  public AccessDeniedHandler problemAccessDeniedHandler() {
    return (request, response, accessDeniedException) ->
        writeProblem(
            response,
            request,
            HttpStatus.FORBIDDEN,
            "Forbidden",
            "Você não tem permissão para acessar este recurso",
            "https://jilo.com/problems/forbidden");
  }

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
