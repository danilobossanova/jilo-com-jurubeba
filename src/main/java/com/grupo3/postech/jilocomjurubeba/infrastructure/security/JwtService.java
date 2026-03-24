package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Servico responsavel pela geracao, validacao e extracao de informacoes de tokens JWT.
 *
 * <p>Utiliza a biblioteca JJWT para assinar tokens com HMAC-SHA256 e a chave secreta configurada
 * via propriedade {@code security.jwt.secret}. O tempo de expiracao e configuravel via {@code
 * security.jwt.expiration-minutes} (padrao: 120 minutos).
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
@Service
@Profile("!test")
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-minutes:120}")
    private long expirationMinutes;

    /**
     * Gera um token JWT a partir dos detalhes do usuario.
     *
     * <p>O subject do token e o username do {@link UserDetails}. O token e assinado com HMAC-SHA256
     * e tem validade definida por {@code security.jwt.expiration-minutes}.
     *
     * @param userDetails detalhes do usuario autenticado
     * @return token JWT assinado como String compacta
     */
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMinutes * 60_000);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(exp)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Gera um token JWT a partir de um objeto {@link Authentication}.
     *
     * <p>Se o principal for uma instancia de {@link UserDetails}, delega para {@link
     * #generateToken(UserDetails)}. Caso contrario, usa o {@code name} da autenticacao como subject
     * do token (fallback).
     *
     * @param authentication objeto de autenticacao do Spring Security
     * @return token JWT assinado como String compacta
     */
    public String generateToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return generateToken(userDetails);
        }

        // fallback: usa o name (geralmente o email/username)
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMinutes * 60_000);

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiration(exp)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extrai o subject (username/email) de um token JWT.
     *
     * @param token token JWT a ser analisado
     * @return subject contido no token, ou {@code null} se o token for invalido ou expirado
     */
    public String extractSubject(String token) {
        try {
            return parser().parseSignedClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Valida se um token JWT e valido para o usuario informado.
     *
     * <p>Verifica se o subject do token corresponde ao username do {@link UserDetails} e se o token
     * nao esta expirado. Retorna {@code false} para tokens malformados ou com assinatura invalida.
     *
     * @param token token JWT a ser validado
     * @param userDetails detalhes do usuario para comparacao
     * @return {@code true} se o token e valido e pertence ao usuario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            var claims = parser().parseSignedClaims(token).getPayload();
            String subject = claims.getSubject();
            Date exp = claims.getExpiration();

            return subject != null
                    && subject.equals(userDetails.getUsername())
                    && exp != null
                    && exp.after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Cria uma instancia do parser JWT configurada com a chave de verificacao.
     *
     * @return parser JWT configurado
     */
    private JwtParser parser() {
        return Jwts.parser().verifyWith(getKey()).build();
    }

    /**
     * Gera a chave secreta HMAC a partir da propriedade {@code security.jwt.secret}.
     *
     * <p>Converte a string secreta para bytes UTF-8 e cria uma {@link SecretKey} compativel com
     * HMAC-SHA256.
     *
     * @return chave secreta para assinatura e verificacao de tokens
     */
    private SecretKey getKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
