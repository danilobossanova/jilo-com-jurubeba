package com.grupo3.postech.jilocomjurubeba.integration;

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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de Integração para Cardápio.
 *
 * <p>
 * Valida o fluxo completo: HTTP Request → Controller → UseCase → Repository →
 * Database
 *
 * <p>
 * Cobre:
 * - Criar cardápio
 * - Listar cardápios
 * - Buscar cardápio por ID
 * - Atualizar cardápio
 * - Deletar cardápio
 * - Filtrar por restaurante
 * - Validações e erros
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
                "classpath:sql/integration/cardapio-seed.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class CardapioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/cardapios";

    @Test
    @DisplayName("GET /cardapios deve retornar lista de todos os cardápios")
    @WithMockUser
    void deveListarTodosCatalogos() throws Exception {
        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].nome", notNullValue()))
                .andExpect(jsonPath("$[0].preco", notNullValue()));
    }

    @Test
    @DisplayName("GET /restaurantes/{id}/cardapio deve retornar cardápios do restaurante")
    @WithMockUser
    void deveListarCardapiosPorRestaurante() throws Exception {
        mockMvc.perform(get("/restaurantes/1/cardapio").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].restauranteId", is(1)))
                .andExpect(jsonPath("$[1].restauranteId", is(1)));
    }

    @Test
    @DisplayName("GET /restaurantes/{id}/cardapio deve retornar 200 com lista vazia para restaurante sem cardápios")
    @WithMockUser
    void deveRetornarListaVaziaQuandoRestauranteSemCardapios() throws Exception {
        mockMvc.perform(get("/restaurantes/9999/cardapio").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /cardapios/{id} deve retornar cardápio existente")
    @WithMockUser
    void deveBuscarCardapioPorId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("PIZZA MARGHERITA")))
                .andExpect(jsonPath("$.preco", is(45.0)))
                .andExpect(jsonPath("$.restauranteId", is(1)))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("GET /cardapios/{id} deve retornar 404 para ID inexistente")
    @WithMockUser
    void deveRetornar404ParaIdInexistente() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /restaurantes/{id}/cardapio/{cardapioId} deve retornar cardápio do restaurante")
    @WithMockUser
    void deveBuscarCardapioDoRestaurante() throws Exception {
        mockMvc.perform(get("/restaurantes/1/cardapio/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("PIZZA MARGHERITA")));
    }

    @Test
    @DisplayName("GET /restaurantes/{id}/cardapio/{cardapioId} deve retornar 404 se cardápio não pertence ao restaurante")
    @WithMockUser
    void deveRetornar404QuandoCardapioNaoPertenceAoRestaurante() throws Exception {
        mockMvc.perform(get("/restaurantes/1/cardapio/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /cardapios deve criar novo cardápio")
    @WithMockUser
    @Sql(statements = "ALTER TABLE cardapio ALTER COLUMN id RESTART WITH 100")
    void deveCriarNovoCardapio() throws Exception {
        String json = """
                {
                  "nome": "Fettuccine Alfredo",
                  "descricao": "Massa italiana com molho cremoso",
                  "preco": 42.50,
                  "apenasNoLocal": true,
                  "caminhoFoto": "/fotos/fettuccine.jpg",
                  "restauranteId": 1
                }
                """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("FETTUCCINE ALFREDO")))
                .andExpect(jsonPath("$.preco", is(42.50)))
                .andExpect(jsonPath("$.restauranteId", is(1)))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("POST /restaurantes/{id}/cardapio deve criar cardápio usando ID do path")
    @WithMockUser
    @Sql(statements = "ALTER TABLE cardapio ALTER COLUMN id RESTART WITH 100")
    void deveCriarCardapioComRestauranteDoPath() throws Exception {
        String json = """
                {
                  "nome": "Pappardelle al Cinghiale",
                  "descricao": "Massa fresca com molho de javali",
                  "preco": 65.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/fotos/pappardelle.jpg"
                }
                """;

        mockMvc.perform(post("/restaurantes/2/cardapio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("PAPPARDELLE AL CINGHIALE")))
                .andExpect(jsonPath("$.restauranteId", is(2)));
    }

    @Test
    @DisplayName("POST /cardapios deve retornar 422 quando nome vazio")
    @WithMockUser
    void deveRetornar422QuandoNomeVazio() throws Exception {
        String json = """
                {
                  "nome": "",
                  "descricao": "Descrição válida",
                  "preco": 50.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/foto.jpg",
                  "restauranteId": 1
                }
                """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("POST /cardapios deve retornar 422 quando preço negativo")
    @WithMockUser
    void deveRetornar422QuandoPrecoNegativo() throws Exception {
        String json = """
                {
                  "nome": "Prato Inválido",
                  "descricao": "Descrição",
                  "preco": -10.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/foto.jpg",
                  "restauranteId": 1
                }
                """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /cardapios/{id} deve atualizar cardápio existente")
    @WithMockUser
    void deveAtualizarCardapio() throws Exception {
        String json = """
                {
                  "nome": "Pizza Premium",
                  "descricao": "Pizza premium com ingredientes importados",
                  "preco": 75.00,
                  "apenasNoLocal": true,
                  "caminhoFoto": "/fotos/pizza-premium.jpg",
                  "restauranteId": 1
                }
                """;

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("PIZZA PREMIUM")))
                .andExpect(jsonPath("$.preco", is(75.00)))
                .andExpect(jsonPath("$.apenasNoLocal", is(true)));
    }

    @Test
    @DisplayName("PUT /restaurantes/{id}/cardapio/{cardapioId} deve atualizar usando path")
    @WithMockUser
    void deveAtualizarCardapioComRestaurantePath() throws Exception {
        String json = """
                {
                  "nome": "Risoto Atualizado",
                  "descricao": "Novo risoto",
                  "preco": 60.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/fotos/risoto-novo.jpg"
                }
                """;

        mockMvc.perform(put("/restaurantes/1/cardapio/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.nome", is("RISOTO ATUALIZADO")))
                .andExpect(jsonPath("$.restauranteId", is(1)));
    }

    @Test
    @DisplayName("PUT /cardapios/{id} deve retornar 404 para ID inexistente")
    @WithMockUser
    void deveRetornar404AoAtualizarIdInexistente() throws Exception {
        String json = """
                {
                  "nome": "Prato Fantasma",
                  "descricao": "Atualização",
                  "preco": 50.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/foto.jpg",
                  "restauranteId": 1
                }
                """;

        mockMvc.perform(put(BASE_URL + "/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /cardapios/{id} deve deletar cardápio existente")
    @WithMockUser
    void deveDeletarCardapio() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verifica que foi de fato deletado
        mockMvc.perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /restaurantes/{id}/cardapio/{cardapioId} deve deletar do restaurante")
    @WithMockUser
    void deveDeletarCardapioDoRestaurante() throws Exception {
        mockMvc.perform(delete("/restaurantes/1/cardapio/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verifica que foi deletado
        mockMvc.perform(get(BASE_URL + "/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /cardapios/{id} deve retornar 404 para ID inexistente")
    @WithMockUser
    void deveRetornar404AoDeletarIdInexistente() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ========================================================
    // TESTES DE FLUXO COMPLETO
    // ========================================================

    @Test
    @DisplayName("Fluxo Completo: Criar → Buscar → Atualizar → Deletar")
    @WithMockUser
    @Sql(statements = "ALTER TABLE cardapio ALTER COLUMN id RESTART WITH 100")
    void deveExecutarFluxoCompleto() throws Exception {
        // 1. Criar
        String criar = """
                {
                  "nome": "Novo Prato",
                  "descricao": "Descrição",
                  "preco": 35.00,
                  "apenasNoLocal": false,
                  "caminhoFoto": "/foto.jpg",
                  "restauranteId": 1
                }
                """;

        var criarResponse = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(criar))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();

        Long novoId = 100L; // ID mockado

        // 2. Buscar
        mockMvc.perform(get(BASE_URL + "/" + novoId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("NOVO PRATO")));

        // 3. Atualizar
        String atualizar = """
                {
                  "nome": "Novo Prato Atualizado",
                  "descricao": "Descrição atualizada",
                  "preco": 40.00,
                  "apenasNoLocal": true,
                  "caminhoFoto": "/foto-nova.jpg",
                  "restauranteId": 1
                }
                """;

        mockMvc.perform(put(BASE_URL + "/" + novoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(atualizar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("NOVO PRATO ATUALIZADO")))
                .andExpect(jsonPath("$.preco", is(40.00)));

        // 4. Deletar
        mockMvc.perform(delete(BASE_URL + "/" + novoId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
