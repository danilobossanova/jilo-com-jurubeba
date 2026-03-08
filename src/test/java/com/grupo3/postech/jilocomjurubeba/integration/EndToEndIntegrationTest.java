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
 * Testes de Integração End-to-End.
 *
 * <p>Simula fluxos de usuário reais: - Criar restaurante com cardápio completo - Atualizar
 * múltiplos cardápios - Listar restaurantes com seus cardápios - Transações complexas entre
 * entidades
 *
 * <p>Foca em: Relacionamentos entre entidades, integridade de dados, fluxos realistas
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
      "classpath:sql/integration/endtoend-seed.sql"
    },
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EndToEndIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("Fluxo: Criar restaurante → Adicionar cardápios → Listar tudo")
  @WithMockUser
  @Sql(
      statements = {
        "ALTER TABLE restaurante ALTER COLUMN id RESTART WITH 100;",
        "ALTER TABLE cardapio ALTER COLUMN id RESTART WITH 100;"
      })
  void deveExecutarFluxoCriacaoRestauranteComCardapio() throws Exception {

    String restauranteJson =
        """
                {
                  "nome": "Restaurante Novo E2E",
                  "endereco": "Rua E2E, 999",
                  "typeCozinha": "ARABE",
                  "horaAbertura": "12:00",
                  "horaFechamento": "23:00",
                  "donoId": 1
                }
                """;

    mockMvc
        .perform(
            post("/restaurantes").contentType(MediaType.APPLICATION_JSON).content(restauranteJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(100)))
        .andExpect(jsonPath("$.nome", is("RESTAURANTE NOVO E2E")));

    String cardapio1 =
        """
                {
                  "nome": "Bife à Parmegiana",
                  "descricao": "Bife suculento",
                  "preco": 65.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/bife.jpg",
                  "restauranteId": 100
                }
                """;

    mockMvc
        .perform(post("/cardapios").contentType(MediaType.APPLICATION_JSON).content(cardapio1))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(100)))
        .andExpect(jsonPath("$.restauranteId", is(100)));

    String cardapio2 =
        """
                {
                  "nome": "Chopp Artesanal",
                  "descricao": "Bebida especial",
                  "preco": 12.00,
                  "apenasNoLocal": true,
                  "caminhoFoto": "/chopp.jpg",
                  "restauranteId": 100
                }
                """;

    mockMvc
        .perform(post("/cardapios").contentType(MediaType.APPLICATION_JSON).content(cardapio2))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(101)));

    mockMvc
        .perform(get("/restaurantes/100/cardapio").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].restauranteId", is(100)))
        .andExpect(jsonPath("$[1].restauranteId", is(100)));

    mockMvc
        .perform(get("/restaurantes/100").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("RESTAURANTE NOVO E2E")))
        .andExpect(jsonPath("$.typeCozinha", is("ARABE")));
  }

  // ========================================================
  // FLUXO 2: ATUALIZAR MÚLTIPLOS CARDÁPIOS
  // ========================================================

  @Test
  @DisplayName("Fluxo: Listar cardápios → Atualizar preços → Verificar mudanças")
  @WithMockUser
  void deveAtualizarMultiplosCardapios() throws Exception {
    // 1. Listar cardápios originais
    mockMvc
        .perform(get("/cardapios").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].preco", is(15.0)))
        .andExpect(jsonPath("$[1].preco", is(45.0)));

    // 2. Atualizar primeiro cardápio
    String atualizar1 =
        """
                {
                  "nome": "Entrada 1 Premium",
                  "descricao": "Entrada premium agora",
                  "preco": 25.00,
                  "apenasNoLocal": true,
                  "caminhoFoto": "/entrada-premium.jpg",
                  "restauranteId": 1
                }
                """;

    mockMvc
        .perform(put("/cardapios/1").contentType(MediaType.APPLICATION_JSON).content(atualizar1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.preco", is(25.0)))
        .andExpect(jsonPath("$.apenasNoLocal", is(true)));

    // 3. Atualizar segundo cardápio
    String atualizar2 =
        """
                {
                  "nome": "Prato Principal 1 Premium",
                  "descricao": "Agora com trufa",
                  "preco": 65.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/prato-premium.jpg",
                  "restauranteId": 1
                }
                """;

    mockMvc
        .perform(put("/cardapios/2").contentType(MediaType.APPLICATION_JSON).content(atualizar2))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.preco", is(65.0)));

    // 4. Verificar que ambas mudaram
    mockMvc
        .perform(get("/cardapios/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.preco", is(25.0)));

    mockMvc
        .perform(get("/cardapios/2").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.preco", is(65.0)));
  }

  // ========================================================
  // FLUXO 3: OPERAÇÕES CASCATA
  // ========================================================

  @Test
  @DisplayName("Fluxo: Deletar cardápios do restaurante → Restaurante ainda existe")
  @WithMockUser
  void deveDeletearCardapiosComRestauranteIntacto() throws Exception {
    // 1. Verificar restaurante existe
    mockMvc
        .perform(get("/restaurantes/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("RESTAURANTE TESTE")));

    // 2. Listar cardápios
    mockMvc
        .perform(get("/restaurantes/1/cardapio").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));

    // 3. Deletar primeiro cardápio
    mockMvc
        .perform(delete("/cardapios/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // 4. Deletar segundo cardápio
    mockMvc
        .perform(delete("/cardapios/2").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // 5. Restaurante ainda existe
    mockMvc
        .perform(get("/restaurantes/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome", is("RESTAURANTE TESTE")));

    // 6. Listar cardápios restantes (apenas 1)
    mockMvc
        .perform(get("/restaurantes/1/cardapio").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(3)));
  }

  // ========================================================
  // FLUXO 4: VALIDAÇÕES E ERROS
  // ========================================================

  @Test
  @DisplayName("Fluxo: Criar cardápio sem restaurante → Erro 422")
  @WithMockUser
  void deveRetornarErroAoCriarCardapioSemRestaurante() throws Exception {
    String cardapioInvalido =
        """
                {
                  "nome": "Prato Órfão",
                  "descricao": "Sem restaurante",
                  "preco": 30.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/orfao.jpg",
                  "restauranteId": 9999
                }
                """;

    mockMvc
        .perform(
            post("/cardapios").contentType(MediaType.APPLICATION_JSON).content(cardapioInvalido))
        .andExpect(status().isNotFound());
  }

  // ========================================================
  // FLUXO 5: BUSCAR COM FILTROS
  // ========================================================

  @Test
  @DisplayName("Fluxo: Listar todos → Filtrar por restaurante → Verificar dados")
  @WithMockUser
  void deveListarComFiltros() throws Exception {
    // 1. Listar todos os cardápios
    mockMvc
        .perform(get("/cardapios").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));

    // 2. Filtrar por restaurante 1
    mockMvc
        .perform(get("/restaurantes/1/cardapio").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[*].restauranteId", everyItem(is(1))));

    // 3. Buscar específico
    mockMvc
        .perform(get("/restaurantes/1/cardapio/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.restauranteId", is(1)));
  }

  // ========================================================
  // FLUXO 6: INTEGRIDADE DE DADOS
  // ========================================================

  @Test
  @DisplayName("Fluxo: Dados devem ser consistentes após operações múltiplas")
  @WithMockUser
  @Sql(statements = "ALTER TABLE cardapio ALTER COLUMN id RESTART WITH 100;")
  void deveManterIntegridadeDados() throws Exception {
    // 1. Buscar estado inicial
    mockMvc
        .perform(get("/restaurantes/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.ativo", is(true)));

    // 2. Criar novo cardápio
    String novoCardapio =
        """
                {
                  "nome": "Novo Prato Integridade",
                  "descricao": "Para testar integridade",
                  "preco": 35.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/integridade.jpg",
                  "restauranteId": 1
                }
                """;

    mockMvc
        .perform(post("/cardapios").contentType(MediaType.APPLICATION_JSON).content(novoCardapio))
        .andExpect(status().isCreated());

    // 3. Restaurante permanece o mesmo
    mockMvc
        .perform(get("/restaurantes/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.nome", is("RESTAURANTE TESTE")))
        .andExpect(jsonPath("$.ativo", is(true)));

    // 4. Cardápio novo está vinculado ao restaurante
    mockMvc
        .perform(get("/restaurantes/1/cardapio").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$[?(@.nome == 'Novo Prato Integridade')].restauranteId", everyItem(is(1))));
  }
}
