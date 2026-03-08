package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

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
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(
    statements = {
      "DELETE FROM cardapio;",
      "DELETE FROM restaurante;",
      "DELETE FROM usuario;",
      "DELETE FROM tipo_usuario;",
      "ALTER TABLE cardapio ALTER COLUMN id RESTART WITH 1;",
      "ALTER TABLE restaurante ALTER COLUMN id RESTART WITH 1;",
      "ALTER TABLE usuario ALTER COLUMN id RESTART WITH 1;",
      "ALTER TABLE tipo_usuario ALTER COLUMN id RESTART WITH 1;",
      "INSERT INTO tipo_usuario (id, nome, descricao, ativo) VALUES (1, 'MASTER', 'Administrador', TRUE);",
      "INSERT INTO tipo_usuario (id, nome, descricao, ativo) VALUES (2, 'CLIENTE', 'Cliente', TRUE);",
      "INSERT INTO tipo_usuario (id, nome, descricao, ativo) VALUES (3, 'DONO_RESTAURANTE', 'Dono', TRUE);",
      "INSERT INTO usuario (id, nome, cpf, email, telefone, tipo_usuario_id, ativo, senha_hash) "
          + "VALUES (1, 'Dono Teste', '11111111111', 'dono@teste.com', '11999999999', 3, TRUE, '123');",
      "INSERT INTO restaurante (id, nome, endereco, type_cozinha, hora_abertura, hora_fechamento, dono_id, ativo) "
          + "VALUES (1, 'RESTAURANTE INICIAL', 'Rua X', 'BRASILEIRA', '10:00', '22:00', 1, TRUE);"
    },
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RestauranteControllerTest {

  @Autowired private MockMvc mockMvc;

  private static final String BASE_URL = "/restaurantes";

  // ---------------------------------------------------------
  // LISTAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("GET /restaurantes deve retornar lista (mesmo vazia)")
  @WithMockUser
  void deveListarRestaurantes() throws Exception {
    mockMvc
        .perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  // ---------------------------------------------------------
  // BUSCAR POR ID
  // ---------------------------------------------------------

  @Test
  @DisplayName("GET /restaurantes/{id} deve retornar restaurante existente")
  @WithMockUser
  void deveBuscarPorId() throws Exception {
    mockMvc
        .perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.nome", notNullValue()));
  }

  @Test
  @DisplayName("GET /restaurantes/{id} deve retornar 404 para id inexistente")
  @WithMockUser
  void deveRetornar404ParaIdInexistente() throws Exception {
    mockMvc
        .perform(get(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  // ---------------------------------------------------------
  // CRIAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("POST /restaurantes deve criar restaurante")
  @WithMockUser
  @Sql(statements = "ALTER TABLE restaurante ALTER COLUMN id RESTART WITH 100")
  void deveCriarRestaurante() throws Exception {

    String json =
        """
        {
          "nome": "Restaurante Teste Criacao",
          "endereco": "Rua A, 123",
          "typeCozinha": "BRASILEIRA",
          "horaAbertura": "10:00",
          "horaFechamento": "22:00",
          "donoId": 1
        }
        """;

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.nome", equalToIgnoringCase("Restaurante Teste Criacao")))
        .andExpect(jsonPath("$.ativo", is(true)));
  }

  @Test
  @DisplayName("POST /restaurantes deve retornar 422 quando nome vazio")
  @WithMockUser
  void deveRetornar422QuandoNomeVazio() throws Exception {

    String json =
        """
        {
          "nome": "",
          "endereco": "Rua A, 123",
          "typeCozinha": "BRASILEIRA",
          "horaAbertura": "10:00",
          "horaFechamento": "22:00",
          "donoId": 1
        }
        """;

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnprocessableEntity());
  }

  // ---------------------------------------------------------
  // ATUALIZAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("PUT /restaurantes/{id} deve atualizar restaurante existente")
  @WithMockUser
  void deveAtualizarRestaurante() throws Exception {

    String json =
        """
        {
          "nome": "Restaurante Atualizado",
          "endereco": "Rua Nova, 456",
          "typeCozinha": "ITALIANA",
          "horaAbertura": "09:00",
          "horaFechamento": "23:00",
          "donoId": 1
        }
        """;

    mockMvc
        .perform(put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("RESTAURANTE ATUALIZADO")))
        .andExpect(jsonPath("$.endereco", is("Rua Nova, 456")));
  }

  // ---------------------------------------------------------
  // DELETAR
  // ---------------------------------------------------------

  @Test
  @DisplayName("DELETE /restaurantes/{id} deve desativar restaurante")
  @WithMockUser
  void deveDeletarRestaurante() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/1")).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /restaurantes/{id} deve retornar 404 para id inexistente")
  @WithMockUser
  void deveRetornar404AoDeletarIdInexistente() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/9999")).andExpect(status().isNotFound());
  }
}
