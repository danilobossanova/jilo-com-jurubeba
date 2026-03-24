package com.grupo3.postech.jilocomjurubeba.interfaces.rest.cardapio;

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
 * Testes de integracao para CardapioController.
 *
 * <p>Valida o fluxo completo: HTTP Request -> Controller -> UseCase -> Gateway -> JPA -> Response.
 * Usa H2 em memoria como banco de testes. Cada teste que precisa de um item de cardapio cria
 * primeiro um usuario dono e um restaurante via seus respectivos endpoints.
 *
 * @author Danilo Fernando
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class CardapioControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    private static final String BASE_URL = "/v1/cardapios";
    private static final String USUARIOS_URL = "/v1/usuarios";
    private static final String RESTAURANTES_URL = "/v1/restaurantes";

    /** Contador interno para gerar CPFs e emails unicos entre testes. */
    private static int contadorUsuario = 0;

    /**
     * Cria um usuario do tipo DONO_RESTAURANTE (tipoUsuarioId=2) via POST e retorna o id gerado.
     */
    private Long criarDonoEObterIdHelper() throws Exception {
        contadorUsuario++;
        String cpf = String.format("6%010d", contadorUsuario);
        String email = "cardapiodono" + contadorUsuario + "@test.com";

        String json =
                """
                {"nome": "Dono Cardapio %d", "cpf": "%s", "email": "%s", "telefone": "11999999999", "tipoUsuarioId": 2, "senha": "123456"}
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
                {"nome": "Restaurante Cardapio", "endereco": "Rua B, 456 - Centro", "tipoCozinha": "BRASILEIRA", "horaAbertura": "10:00", "horaFechamento": "22:00", "donoId": %d}
                """
                        .formatted(donoId);

        MvcResult result =
                mockMvc.perform(
                                post(RESTAURANTES_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(json))
                        .andExpect(status().isCreated())
                        .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    /** Cria um item de cardapio via POST e retorna o id gerado. */
    private Long criarItemCardapioEObterIdHelper(Long restauranteId) throws Exception {
        String json =
                """
                {"nome": "Feijoada Completa", "descricao": "Feijoada com arroz e couve", "preco": 45.90, "apenasNoLocal": false, "caminhoFoto": "/fotos/feijoada.jpg", "restauranteId": %d}
                """
                        .formatted(restauranteId);

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
    @DisplayName("POST /v1/cardapios deve criar novo item de cardapio")
    @WithMockUser
    void deveCriarNovoItemCardapio() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);

        String json =
                """
                {"nome": "Feijoada", "descricao": "Feijoada completa", "preco": 45.90, "apenasNoLocal": false, "caminhoFoto": "/fotos/feijoada.jpg", "restauranteId": %d}
                """
                        .formatted(restauranteId);

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("FEIJOADA")))
                .andExpect(jsonPath("$.preco", is(45.90)))
                .andExpect(jsonPath("$.restauranteId", is(restauranteId.intValue())))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("GET /v1/cardapios deve listar itens de cardapio cadastrados")
    @WithMockUser
    void deveListarItens() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);
        criarItemCardapioEObterIdHelper(restauranteId);

        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/cardapios/{id} deve retornar item de cardapio existente")
    @WithMockUser
    void deveBuscarPorId() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);
        Long itemId = criarItemCardapioEObterIdHelper(restauranteId);

        mockMvc.perform(get(BASE_URL + "/" + itemId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId.intValue())))
                .andExpect(jsonPath("$.nome", is("FEIJOADA COMPLETA")))
                .andExpect(jsonPath("$.restauranteId", is(restauranteId.intValue())));
    }

    @Test
    @DisplayName("PUT /v1/cardapios/{id} deve atualizar item de cardapio existente")
    @WithMockUser
    void deveAtualizarItem() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);
        Long itemId = criarItemCardapioEObterIdHelper(restauranteId);

        String json =
                """
                {"nome": "Feijoada Especial", "descricao": "Feijoada com carnes nobres", "preco": 55.90, "apenasNoLocal": true, "caminhoFoto": "/fotos/feijoada-especial.jpg"}
                """;

        mockMvc.perform(
                        put(BASE_URL + "/" + itemId + "?donoId=" + donoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("FEIJOADA ESPECIAL")))
                .andExpect(jsonPath("$.preco", is(55.90)));
    }

    @Test
    @DisplayName("DELETE /v1/cardapios/{id} deve desativar item de cardapio")
    @WithMockUser
    void deveDeletarItem() throws Exception {
        Long donoId = criarDonoEObterIdHelper();
        Long restauranteId = criarRestauranteEObterIdHelper(donoId);
        Long itemId = criarItemCardapioEObterIdHelper(restauranteId);

        mockMvc.perform(delete(BASE_URL + "/" + itemId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /v1/cardapios/{id} deve retornar 404 para id inexistente")
    @WithMockUser
    void deveRetornar404ParaIdInexistente() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
