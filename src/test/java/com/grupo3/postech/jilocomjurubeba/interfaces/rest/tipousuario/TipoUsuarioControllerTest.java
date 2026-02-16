package com.grupo3.postech.jilocomjurubeba.interfaces.rest.tipousuario;

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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes de integracao para TipoUsuarioController.
 *
 * <p>Valida o fluxo completo: HTTP Request → Controller → UseCase → Gateway → JPA → Response. Usa
 * H2 em memoria como banco de testes. Os 3 tipos padrao sao semeados automaticamente pelo
 * DataSeeder.
 *
 * @author Danilo Fernando
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TipoUsuarioControllerTest {

    @Autowired private MockMvc mockMvc;

    private static final String BASE_URL = "/v1/tipos-usuario";

    @Test
    @DisplayName("GET /v1/tipos-usuario deve listar tipos semeados")
    @WithMockUser
    void deveListarTiposSemeados() throws Exception {
        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)))
                .andExpect(jsonPath("$[0].nome", notNullValue()));
    }

    @Test
    @DisplayName("POST /v1/tipos-usuario deve criar novo tipo")
    @WithMockUser
    void deveCriarNovoTipo() throws Exception {
        String json =
                """
                {"nome": "MODERADOR", "descricao": "Moderador do sistema"}
                """;

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("MODERADOR")))
                .andExpect(jsonPath("$.descricao", is("Moderador do sistema")))
                .andExpect(jsonPath("$.ativo", is(true)));
    }

    @Test
    @DisplayName("POST /v1/tipos-usuario deve retornar 400 quando nome vazio")
    @WithMockUser
    void deveRetornar400QuandoNomeVazio() throws Exception {
        String json =
                """
                {"nome": "", "descricao": "Descricao"}
                """;

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /v1/tipos-usuario deve retornar 422 quando nome duplicado")
    @WithMockUser
    void deveRetornar422QuandoNomeDuplicado() throws Exception {
        String json =
                """
                {"nome": "MASTER", "descricao": "Duplicado"}
                """;

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("GET /v1/tipos-usuario/{id} deve retornar tipo existente")
    @WithMockUser
    void deveBuscarPorId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/tipos-usuario/{id} deve retornar 404 para id inexistente")
    @WithMockUser
    void deveRetornar404ParaIdInexistente() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /v1/tipos-usuario/{id} deve atualizar tipo existente")
    @WithMockUser
    void deveAtualizarTipo() throws Exception {
        String json =
                """
                {"nome": "MASTER", "descricao": "Administrador master atualizado"}
                """;

        mockMvc.perform(put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is("Administrador master atualizado")));
    }

    @Test
    @DisplayName("DELETE /v1/tipos-usuario/{id} deve desativar tipo")
    @WithMockUser
    void deveDesativarTipo() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /v1/tipos-usuario/{id} deve retornar 404 para id inexistente")
    @WithMockUser
    void deveRetornar404AoDeletarIdInexistente() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/9999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
