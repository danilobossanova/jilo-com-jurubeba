package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.grupo3.postech.jilocomjurubeba.infrastructure.security.AuthRequest;
import com.grupo3.postech.jilocomjurubeba.infrastructure.security.AuthResponse;
import com.grupo3.postech.jilocomjurubeba.infrastructure.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock private AuthenticationManager authenticationManager;

  @Mock private JwtService jwtService;

  @InjectMocks private AuthController authController;

  @Test
  @DisplayName("Deve retornar 200 e token quando login for bem-sucedido")
  void deveRetornarTokenQuandoLoginValido() {

    AuthRequest request = new AuthRequest("email@teste.com", "123456");

    Authentication authenticationMock = mock(Authentication.class);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authenticationMock);

    when(jwtService.generateToken(authenticationMock)).thenReturn("TOKEN_VALIDO");

    ResponseEntity<AuthResponse> response = authController.login(request);

    assertThat(response.getStatusCode().value()).isEqualTo(200);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().token()).isEqualTo("TOKEN_VALIDO");

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtService).generateToken(authenticationMock);
  }

  @Test
  @DisplayName("Deve retornar 401 quando credenciais forem inválidas")
  void deveRetornar401QuandoCredenciaisInvalidas() {

    AuthRequest request = new AuthRequest("email@teste.com", "senhaErrada");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Credenciais inválidas"));

    ResponseEntity<AuthResponse> response = authController.login(request);

    assertThat(response.getStatusCode().value()).isEqualTo(401);
    assertThat(response.getBody()).isNull();

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verifyNoInteractions(jwtService);
  }
}
