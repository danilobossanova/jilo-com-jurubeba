package com.grupo3.postech.jilocomjurubeba.interfaces.rest.usuario;

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
 * Testes de integracao para UsuarioController.
 *
 * <p>Valida o fluxo completo: HTTP Request -> Controller -> UseCase -> Gateway -> JPA -> Response.
 * Usa H2 em memoria como banco de testes. Os tipos de usuario sao semeados automaticamente pelo
 * TipoUsuarioDataSeeder (id=1 MASTER, id=2 DONO_RESTAURANTE, id=3 CLIENTE).
 *
 * @author Danilo Fernando
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    private static final String BASE_URL = "/v1/usuarios";

    /**
     * Cria um usuario via POST e retorna o id gerado. Metodo auxiliar para testes que precisam de
     * um usuario previamente cadastrado.
     */
    private Long criarUsuarioEObterIdHelper(String nome, String cpf, String email)
            throws Exception {
        String json =
                """
                {"nome": "%s", "cpf": "%s", "email": "%s", "telefone": "11999999999", "tipoUsuarioId": 3, "senha": "123456"}
                """
                        .formatted(nome, cpf, email);

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
    @DisplayName("POST /v1/usuarios deve criar novo usuario")
    @WithMockUser
    void deveCriarNovoUsuario() throws Exception {
        String json =
                """
                {"nome": "Test User", "cpf": "12345678909", "email": "test@test.com", "telefone": "11999999999", "tipoUsuarioId": 3, "senha": "123456"}
                """;

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("TEST USER")))
                .andExpect(jsonPath("$.cpf", is("12345678909")))
                .andExpect(jsonPath("$.email", is("test@test.com")))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("GET /v1/usuarios deve listar usuarios cadastrados")
    @WithMockUser
    void deveListarUsuarios() throws Exception {
        criarUsuarioEObterIdHelper("Usuario Lista", "98765432100", "lista@test.com");

        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} deve retornar usuario existente")
    @WithMockUser
    void deveBuscarPorId() throws Exception {
        Long id = criarUsuarioEObterIdHelper("Usuario Busca", "87654321098", "busca@test.com");

        mockMvc.perform(get(BASE_URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.nome", is("USUARIO BUSCA")))
                .andExpect(jsonPath("$.cpf", is("87654321098")));
    }

    @Test
    @DisplayName("PUT /v1/usuarios/{id} deve atualizar usuario existente")
    @WithMockUser
    void deveAtualizarUsuario() throws Exception {
        Long id =
                criarUsuarioEObterIdHelper(
                        "Usuario Atualizar", "76543210987", "atualizar@test.com");

        String json =
                """
                {"nome": "Usuario Atualizado", "email": "atualizado@test.com", "telefone": "11888888888"}
                """;

        mockMvc.perform(
                        put(BASE_URL + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("USUARIO ATUALIZADO")))
                .andExpect(jsonPath("$.email", is("atualizado@test.com")));
    }

    @Test
    @DisplayName("DELETE /v1/usuarios/{id} deve desativar usuario")
    @WithMockUser
    void deveDeletarUsuario() throws Exception {
        Long id = criarUsuarioEObterIdHelper("Usuario Deletar", "65432109876", "deletar@test.com");

        mockMvc.perform(delete(BASE_URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /v1/usuarios/{id} deve retornar 404 para id inexistente")
    @WithMockUser
    void deveRetornar404ParaIdInexistente() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
