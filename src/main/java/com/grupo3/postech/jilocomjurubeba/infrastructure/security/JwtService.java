package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${security.jwt.secret}")
  private String secret;

  @Value("${security.jwt.expiration-minutes:120}")
  private long expirationMinutes;

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

  public String extractSubject(String token) {
    try {
      return parser().parseSignedClaims(token).getPayload().getSubject();
    } catch (JwtException e) {
      return null;
    }
  }

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

  private JwtParser parser() {
    return Jwts.parser().verifyWith(getKey()).build();
  }

  private SecretKey getKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
