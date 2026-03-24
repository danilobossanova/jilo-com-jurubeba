package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de autenticacao JWT que intercepta requisicoes HTTP.
 *
 * <p>Extende {@link OncePerRequestFilter} para garantir execucao unica por requisicao. Extrai o
 * token JWT do header {@code Authorization} (formato Bearer), valida-o via {@link JwtService} e
 * popula o {@link SecurityContextHolder} com a autenticacao do usuario.
 *
 * <p>Endpoints publicos (health, swagger, auth) sao ignorados pelo filtro. Tokens invalidos ou
 * expirados resultam na limpeza do contexto de seguranca, delegando ao {@link SecurityConfig} a
 * resposta 401.
 *
 * <p>Ativado para todos os profiles exceto test.
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
@Component
@Profile("!test")
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Construtor com injecao de dependencia do servico JWT e do servico de detalhes do usuario.
     *
     * @param jwtService servico de geracao e validacao de tokens JWT
     * @param userDetailsService servico de carregamento de detalhes do usuario
     */
    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Determina se o filtro deve ser ignorado para a requisicao atual.
     *
     * <p>Retorna {@code true} para endpoints publicos como autenticacao, health check, swagger e
     * documentacao da API, evitando processamento JWT desnecessario.
     *
     * @param request requisicao HTTP
     * @return {@code true} se o filtro deve ser ignorado
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/auth/")
                || path.equals("/v1/health")
                || path.equals("/actuator/health")
                || path.equals("/api-docs")
                || path.startsWith("/api-docs/")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html");
    }

    /**
     * Executa a logica de filtragem JWT para cada requisicao.
     *
     * <p>Extrai o token do header {@code Authorization} (prefixo "Bearer "), valida-o e popula o
     * contexto de seguranca com a autenticacao do usuario. Se o token for invalido, malformado ou
     * expirado, limpa o contexto e delega ao SecurityConfig a resposta 401.
     *
     * <p>O {@code filterChain.doFilter()} e sempre chamado fora do try/catch para garantir que a
     * cadeia de filtros continue mesmo em caso de erro no processamento JWT.
     *
     * @param request requisicao HTTP
     * @param response resposta HTTP
     * @param filterChain cadeia de filtros para delegacao
     * @throws ServletException se houver erro no processamento do servlet
     * @throws IOException se houver erro de entrada/saida
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

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

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // token chegou mas é inválido: limpa contexto e deixa SecurityConfig devolver
                    // 401
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
