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
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes de Integração para Restaurante.
 *
 * <p>Valida o fluxo completo: HTTP Request → Controller → UseCase → Repository → Database
 *
 * <p>Cobre: - Listar restaurantes - Buscar restaurante por ID - Criar restaurante - Atualizar
 * restaurante - Deletar restaurante - Validações e erros - Relacionamento com donos
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(
    scripts = {
      "classpath:sql/integration/reset.sql",
      "classpath:data.sql",
      "classpath:sql/integration/restaurante-seed.sql"
    },
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RestauranteIntegrationTest {

  @Autowired private MockMvc mockMvc;

  private static final String BASE_URL = "/restaurantes";

  @Test
  @DisplayName("GET /restaurantes deve retornar lista de todos os restaurantes")
  @WithMockUser
  void deveListarTodosRestaurantes() throws Exception {
    mockMvc
        .perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
        .andExpect(jsonPath("$[0].id", notNullValue()))
        .andExpect(jsonPath("$[0].nome", notNullValue()))
        .andExpect(jsonPath("$[0].endereco", notNullValue()))
        .andExpect(jsonPath("$[0].typeCozinha", notNullValue()));
  }

  @Test
  @DisplayName("GET /restaurantes deve conter dados corretos dos restaurantes")
  @WithMockUser
  void deveRetornarDadosCorretos() throws Exception {
    mockMvc
        .perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome", is("PIZZARIA DO SILVA")))
        .andExpect(jsonPath("$[0].typeCozinha", is("ITALIANA")))
        .andExpect(jsonPath("$[1].nome", is("CHURRASCARIA SANTOS")))
        .andExpect(jsonPath("$[1].typeCozinha", is("BRASILEIRA")));
  }

  @Test
  @DisplayName("GET /restaurantes/{id} deve retornar restaurante existente")
  @WithMockUser
  void deveBuscarRestaurantePorId() throws Exception {
    mockMvc
        .perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.nome", is("PIZZARIA DO SILVA")))
        .andExpect(jsonPath("$.endereco", is("Rua A, 100")))
        .andExpect(jsonPath("$.typeCozinha", is("ITALIANA")))
        .andExpect(jsonPath("$.horaAbertura", is("10:00:00")))
        .andExpect(jsonPath("$.horaFechamento", is("23:00:00")))
        .andExpect(jsonPath("$.ativo", is(true)));
  }

  @Test
  @DisplayName("GET /restaurantes/{id} deve retornar 404 para ID inexistente")
  @WithMockUser
  void deveRetornar404ParaIdInexistente() throws Exception {
    mockMvc
        .perform(get(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /restaurantes/{id} deve incluir informações do dono")
  @WithMockUser
  void deveBuscarComInformacoesDoDono() throws Exception {
    mockMvc
        .perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.donoId", is(1)));
  }

  @Test
  @DisplayName("POST /restaurantes deve criar novo restaurante")
  @WithMockUser
  @Sql(statements = "ALTER TABLE restaurante ALTER COLUMN id RESTART WITH 100")
  void deveCriarNovoRestaurante() throws Exception {
    String json =
        """
                {
                  "nome": "Restaurante Nova Era",
                  "endereco": "Avenida Paulista, 1000",
                  "typeCozinha": "MEXICANA",
                  "horaAbertura": "09:00",
                  "horaFechamento": "23:30",
                  "donoId": 1
                }
                """;

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.nome", is("RESTAURANTE NOVA ERA")))
        .andExpect(jsonPath("$.endereco", is("Avenida Paulista, 1000")))
        .andExpect(jsonPath("$.typeCozinha", is("MEXICANA")))
        .andExpect(jsonPath("$.donoId", is(1)))
        .andExpect(jsonPath("$.ativo", is(true)));
  }

  @Test
  @DisplayName("POST /restaurantes com header Location deve ter ID no path")
  @WithMockUser
  @Sql(statements = "ALTER TABLE restaurante ALTER COLUMN id RESTART WITH 100")
  void deveRetornarLocationComId() throws Exception {
    String json =
        """
                {
                  "nome": "Bar do João",
                  "endereco": "Rua das Flores, 50",
                  "typeCozinha": "BRASILEIRA",
                  "horaAbertura": "18:00",
                  "horaFechamento": "02:00",
                  "donoId": 2
                }
                """;

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", containsString("/restaurantes/100")));
  }

  @Test
  @DisplayName("POST /restaurantes deve retornar 422 quando nome vazio")
  @WithMockUser
  void deveRetornar422QuandoNomeVazio() throws Exception {
    String json =
        """
                {
                  "nome": "",
                  "endereco": "Endereço válido",
                  "typeCozinha": "ITALIANA",
                  "horaAbertura": "10:00",
                  "horaFechamento": "22:00",
                  "donoId": 1
                }
                """;

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  @DisplayName("POST /restaurantes deve retornar 404 quando dono não existe")
  @WithMockUser
  void deveRetornar404QuandoDonoInexistente() throws Exception {
    String json =
        """
                {
                  "nome": "Restaurante Sem Dono",
                  "endereco": "Rua X",
                  "typeCozinha": "ITALIANA",
                  "horaAbertura": "10:00",
                  "horaFechamento": "22:00",
                  "donoId": 9999
                }
                """;

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound());
  }

  // ========================================================
  // ATUALIZAR RESTAURANTE
  // ========================================================

  @Test
  @DisplayName("PUT /restaurantes/{id} deve atualizar restaurante existente")
  @WithMockUser
  void deveAtualizarRestaurante() throws Exception {
    String json =
        """
                {
                  "nome": "Pizzaria do Silva - Atualizada",
                  "endereco": "Rua A, 101",
                  "typeCozinha": "ITALIANA",
                  "horaAbertura": "09:00",
                  "horaFechamento": "23:30",
                  "donoId": 1
                }
                """;

    mockMvc
        .perform(put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.nome", is("PIZZARIA DO SILVA - ATUALIZADA")))
        .andExpect(jsonPath("$.endereco", is("Rua A, 101")))
        .andExpect(jsonPath("$.horaAbertura", is("09:00:00")))
        .andExpect(jsonPath("$.horaFechamento", is("23:30:00")));
  }

  @Test
  @DisplayName("PUT /restaurantes/{id} deve usar ID do path como fonte de verdade")
  @WithMockUser
  void deveUsarIdDoPath() throws Exception {
    String json =
        """
                {
                  "nome": "Churrascaria Atualizada",
                  "endereco": "Rua B, 201",
                  "typeCozinha": "BRASILEIRA",
                  "horaAbertura": "11:30",
                  "horaFechamento": "00:30",
                  "donoId": 2
                }
                """;

    mockMvc
        .perform(put(BASE_URL + "/2").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.nome", is("CHURRASCARIA ATUALIZADA")));
  }

  @Test
  @DisplayName("PUT /restaurantes/{id} deve retornar 404 para ID inexistente")
  @WithMockUser
  void deveRetornar404AoAtualizarIdInexistente() throws Exception {
    String json =
        """
                {
                  "nome": "Restaurante Fantasma",
                  "endereco": "Rua Inexistente",
                  "typeCozinha": "ITALIANA",
                  "horaAbertura": "10:00",
                  "horaFechamento": "22:00",
                  "donoId": 1
                }
                """;

    mockMvc
        .perform(put(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isNotFound());
  }

  // ========================================================
  // DELETAR RESTAURANTE
  // ========================================================

  @Test
  @DisplayName("DELETE /restaurantes/{id} deve marcar restaurante como inativo")
  @WithMockUser
  void deveDeletarRestaurante() throws Exception {
    mockMvc
        .perform(delete(BASE_URL + "/3").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // Exclusão lógica: registro continua acessível, mas inativo
    mockMvc
        .perform(get(BASE_URL + "/3").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ativo", is(false)));
  }

  @Test
  @DisplayName("DELETE /restaurantes/{id} deve retornar 404 para ID inexistente")
  @WithMockUser
  void deveRetornar404AoDeletarIdInexistente() throws Exception {
    mockMvc
        .perform(delete(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  // ========================================================
  // TESTES DE FLUXO COMPLETO
  // ========================================================

  @Test
  @DisplayName("Fluxo Completo: Criar → Buscar → Atualizar → Deletar restaurante")
  @WithMockUser
  @Sql(statements = "ALTER TABLE restaurante ALTER COLUMN id RESTART WITH 100")
  void deveExecutarFluxoCompleto() throws Exception {
    // 1. Criar
    String criar =
        """
                {
                  "nome": "Novo Restaurante Flow",
                  "endereco": "Rua Flow, 123",
                  "typeCozinha": "ARABE",
                  "horaAbertura": "08:00",
                  "horaFechamento": "20:00",
                  "donoId": 1
                }
                """;

    mockMvc
        .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(criar))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()));

    Long novoId = 100L;

    // 2. Buscar
    mockMvc
        .perform(get(BASE_URL + "/" + novoId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("NOVO RESTAURANTE FLOW")));

    // 3. Atualizar
    String atualizar =
        """
                {
                  "nome": "Novo Restaurante Flow - Atualizado",
                  "endereco": "Rua Flow, 456",
                  "typeCozinha": "ARABE",
                  "horaAbertura": "07:00",
                  "horaFechamento": "21:00",
                  "donoId": 1
                }
                """;

    mockMvc
        .perform(
            put(BASE_URL + "/" + novoId).contentType(MediaType.APPLICATION_JSON).content(atualizar))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("NOVO RESTAURANTE FLOW - ATUALIZADO")))
        .andExpect(jsonPath("$.endereco", is("Rua Flow, 456")));

    // 4. Deletar
    mockMvc
        .perform(delete(BASE_URL + "/" + novoId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // 5. Verificar exclusão lógica
    mockMvc
        .perform(get(BASE_URL + "/" + novoId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ativo", is(false)));
  }

  @Test
  @DisplayName("Vários restaurantes do mesmo dono")
  @WithMockUser
  void deveListarMultiplosRestaurantesDoMesmoDono() throws Exception {
    mockMvc
        .perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.donoId == 1)]", hasSize(1)))
        .andExpect(jsonPath("$[?(@.donoId == 2)]", hasSize(1)))
        .andExpect(jsonPath("$[?(@.donoId == 3)]", hasSize(1)));
  }
}
