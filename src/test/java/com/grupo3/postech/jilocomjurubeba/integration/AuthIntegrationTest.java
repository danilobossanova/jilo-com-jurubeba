package com.grupo3.postech.jilocomjurubeba.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.grupo3.postech.jilocomjurubeba.infrastructure.security.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes de Integração para Autenticação.
 *
 * <p>Valida o fluxo completo de login: HTTP Request → Controller → AuthManager → JWT Service →
 * Token
 *
 * <p>Cobre: - Login com credenciais válidas - Login com credenciais inválidas - Geração de token
 * JWT - Acesso a endpoints protegidos com token - Acesso negado sem token
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Sql(
    scripts = {
      "classpath:sql/integration/reset.sql",
      "classpath:data.sql",
      "classpath:sql/integration/auth-seed.sql"
    },
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthIntegrationTest {

  @Autowired private MockMvc mockMvc;

  private static final String LOGIN_URL = "/auth/login";

  @Test
  @DisplayName("POST /auth/login com credenciais válidas deve retornar token")
  void deveRetornarTokenComCredenciaisValidas() throws Exception {
    String json =
        """
                {
                  "email": "admin@teste.com",
                  "senha": "123456"
                }
                """;

    mockMvc
        .perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token", notNullValue()))
        .andExpect(jsonPath("$.token", not(emptyString())));
  }

  @Test
  @DisplayName("POST /auth/login com email inexistente deve retornar 401")
  void deveRetornar401ComEmailInexistente() throws Exception {
    String json =
        """
                {
                  "email": "naoexiste@teste.com",
                  "senha": "123456"
                }
                """;

    mockMvc
        .perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("POST /auth/login com senha incorreta deve retornar 401")
  void deveRetornar401ComSenhaIncorreta() throws Exception {
    String json =
        """
                {
                  "email": "admin@teste.com",
                  "senha": "senhaErrada123"
                }
                """;

    mockMvc
        .perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("POST /auth/login com JSON inválido deve retornar 400")
  void deveRetornar400ComJsonInvalido() throws Exception {
    String json = "{ json inválido }";

    mockMvc
        .perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Com @WithMockUser deve ter acesso a endpoint protegido")
  @WithMockUser(username = "admin@teste.com", roles = "ADMIN")
  void deveTerAcessoComMockUser() throws Exception {
    mockMvc
        .perform(get("/tipos-usuario").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
