package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import com.grupo3.postech.jilocomjurubeba.infrastructure.security.AuthRequest;
import com.grupo3.postech.jilocomjurubeba.infrastructure.security.AuthResponse;
import com.grupo3.postech.jilocomjurubeba.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                    )
                );

            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (org.springframework.security.core.AuthenticationException ex) {
            return ResponseEntity.status(401).build();
        }
    }
}
