package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/auth/")
            || path.equals("/actuator/health")
            || path.startsWith("/v3/api-docs/")
            || path.startsWith("/swagger-ui")
            || path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Sem Bearer -> segue (SecurityConfig decide se precisa auth)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();

        // Bearer vazio -> segue
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Só o bloco de JWT fica protegido por try/catch
        try {
            String username = jwtService.extractSubject(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                        );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // token chegou mas é inválido: limpa contexto e deixa SecurityConfig devolver 401
                    SecurityContextHolder.clearContext();
                }
            }
        } catch (Exception ex) {
            // token malformado/expirado: limpa contexto e deixa SecurityConfig devolver 401
            SecurityContextHolder.clearContext();
        }

        // ✅ SEMPRE chama o chain fora do try/catch
        filterChain.doFilter(request, response);
    }
}
