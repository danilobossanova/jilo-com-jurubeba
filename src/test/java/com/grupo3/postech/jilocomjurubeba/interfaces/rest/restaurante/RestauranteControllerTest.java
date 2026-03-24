package com.grupo3.postech.jilocomjurubeba.interfaces.rest.restaurante;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.grupo3.postech.jilocomjurubeba.infrastructure.security.TestSecurityConfig;

/**
 * Testes de integracao para RestauranteController.
 *
 * <p>Valida o fluxo completo: HTTP Request -> Controller -> UseCase -> Gateway -> JPA -> Response.
 * Usa H2 em memoria como banco de testes. Os tipos de usuario sao semeados automaticamente pelo
 * TipoUsuarioDataSeeder. Cada teste que precisa de um restaurante cria primeiro um usuario dono via
 * POST /v1/usuarios.
 *
 * @author Danilo Fernando
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class RestauranteControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    private static final String BASE_URL = "/v1/restaurantes";
    private static final String USUARIOS_URL = "/v1/usuarios";

    /** Contador interno para gerar CPFs e emails unicos entre testes. */
    private static int contadorUsuario = 0;

    /**
     * Cria um usuario do tipo DONO_RESTAURANTE (tipoUsuarioId=2) via POST e retorna o id gerado.
     */
    private Long criarDonoEObterIdHelper() throws Exception {
        contadorUsuario++;
        String cpf = String.format("5%010d", contadorUsuario);
        String email = "dono" + contadorUsuario + "@test.com";

        String json =
                """
                {"nome": "Dono Restaurante %d", "cpf": "%s", "email": "%s", "telefone": "11999999999", "tipoUsuarioId": 2, "senha": "123456"}
                """
                        .formatted(contadorUsuario, cpf, email);

        MvcResult result =
                mockMvc.perform(
                                post(USUARIOS_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(json))
                        .andExpect(status().isCreated())
                        .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    /** Cria um restaurante via POST e retorna o id gerado. */
    private Long criarRestauranteEObterIdHelper(Long donoId) throws Exception {
        String json =
                """
                {"nome": "Restaurante Teste", "endereco": "Rua A, 123 - Centro", "tipoCozinha": "BRASILEIRA", "horaAbertura": "10:00", "horaFechamento": "22:00", "donoId": %d}
                """
                        .formatted(donoId);

        MvcResult result =
                mockMvc.perform(
                                post(BASE_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(json))
                        .andExpect(status().isCreated())
                        .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    @Test
    @DisplayName("POST /v1/restaurantes deve criar novo restaurante")
    @WithMockUser
    void deveCriarNovoRestaurante() throws Exception {
        Long donoId = criarDonoEObterIdHelper();

        String json =
                """
                {"nome": "Restaurante Bom Sabor", "endereco": "Rua das Flores, 123 - Centro", "tipoCozinha": "BRASILEIRA", "horaAbertura": "08:00", "horaFechamento": "22:00", "donoId": %d}
                """
                        .formatted(donoId);

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("RESTAURANTE BOM SABOR")))
                .andExpect(jsonPath("$.tipoCozinha", is("BRASILEIRA")))
                .andExpect(jsonPath("$.donoId", is(donoId.intValue())))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("GET /v1/restaurantes deve listar restaurantes cadastrados")
    @WithMockUser
    void deveListarRestaurantes() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        criarRestauranteEObterIdHelper(donoId);

        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{id} deve retornar restaurante existente")
    @WithMockUser
    void deveBuscarPorId() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);

        mockMvc.perform(get(BASE_URL + "/" + restauranteId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(restauranteId.intValue())))
                .andExpect(jsonPath("$.nome", is("RESTAURANTE TESTE")))
                .andExpect(jsonPath("$.donoId", is(donoId.intValue())));
    }

    @Test
    @DisplayName("PUT /v1/restaurantes/{id} deve atualizar restaurante existente")
    @WithMockUser
    void deveAtualizarRestaurante() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);

        String json =
                """
                {"nome": "Restaurante Atualizado", "endereco": "Rua Nova, 456 - Centro", "tipoCozinha": "ITALIANA", "horaAbertura": "09:00", "horaFechamento": "23:00"}
                """;

        mockMvc.perform(
                        put(BASE_URL + "/" + restauranteId + "?donoId=" + donoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("RESTAURANTE ATUALIZADO")))
                .andExpect(jsonPath("$.tipoCozinha", is("ITALIANA")));
    }

    @Test
    @DisplayName("DELETE /v1/restaurantes/{id} deve desativar restaurante")
    @WithMockUser
    void deveDeletarRestaurante() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);

        mockMvc.perform(
                        delete(BASE_URL + "/" + restauranteId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /v1/restaurantes/{id} deve retornar 404 para id inexistente")
    @WithMockUser
    void deveRetornar404ParaIdInexistente() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
