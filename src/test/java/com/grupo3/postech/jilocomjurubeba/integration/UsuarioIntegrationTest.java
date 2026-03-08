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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(
        scripts = {
                "classpath:sql/integration/reset.sql",
                "classpath:data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class UsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/usuarios";

    @Test
    @DisplayName("GET /usuarios deve listar usuários")
    @WithMockUser
    void deveListarUsuarios() throws Exception {
        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("POST /usuarios deve criar usuário")
    @WithMockUser
    @Sql(statements = "ALTER TABLE usuario ALTER COLUMN id RESTART WITH 100")
    void deveCriarUsuario() throws Exception {
        String json = """
                {
                  "nome": "Juliana Teste",
                  "cpf": "12312312312",
                  "email": "juliana.teste@email.com",
                  "telefone": "11999990000",
                  "tipoUsuarioId": 2,
                  "senha": "123456"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.nome", is("JULIANA TESTE")))
                .andExpect(jsonPath("$.email", is("juliana.teste@email.com")))
                .andExpect(jsonPath("$.tipoUsuarioId", is(2)));
    }

    @Test
    @DisplayName("GET /usuarios/{id} deve retornar usuário existente")
    @WithMockUser
    void deveBuscarUsuarioPorId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("DONO TESTE")))
                .andExpect(jsonPath("$.email", is("dono@teste.com")));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} deve atualizar usuário")
    @WithMockUser
    void deveAtualizarUsuario() throws Exception {
        String json = """
                {
                  "nome": "Dono Atualizado",
                  "telefone": "11911112222",
                  "email": "dono.atualizado@teste.com"
                }
                """;

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("DONO ATUALIZADO")))
                .andExpect(jsonPath("$.telefone", is("11911112222")))
                .andExpect(jsonPath("$.email", is("dono.atualizado@teste.com")));
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} deve remover usuário")
    @WithMockUser
    @Sql(statements = "ALTER TABLE usuario ALTER COLUMN id RESTART WITH 100")
    void deveDeletarUsuario() throws Exception {
        String json = """
                {
                  "nome": "Usuario Remocao",
                  "cpf": "32132132132",
                  "email": "remover@teste.com",
                  "telefone": "11900000000",
                  "tipoUsuarioId": 2,
                  "senha": "123456"
                }
                """;

        var created = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();

        String body = created.getResponse().getContentAsString();
        long id = Long.parseLong(body.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /usuarios deve retornar 422 ao criar com email duplicado")
    @WithMockUser
    void deveRetornar422ComEmailDuplicado() throws Exception {
        String json = """
                {
                  "nome": "Outro Dono",
                  "cpf": "99988877766",
                  "email": "dono@teste.com",
                  "telefone": "11977778888",
                  "tipoUsuarioId": 2,
                  "senha": "123456"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnprocessableEntity());
    }
}
