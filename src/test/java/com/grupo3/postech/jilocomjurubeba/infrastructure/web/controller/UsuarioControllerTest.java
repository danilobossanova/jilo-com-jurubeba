package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)

// LIMPA TODAS AS TABELAS E RECRIA tipo_usuario ANTES DE CADA TESTE
@Sql(statements = {
    "DELETE FROM cardapio;",
    "DELETE FROM restaurante;",
    "DELETE FROM usuario;",
    "DELETE FROM tipo_usuario;",

    "ALTER TABLE cardapio ALTER COLUMN id RESTART WITH 1;",
    "ALTER TABLE restaurante ALTER COLUMN id RESTART WITH 1;",
    "ALTER TABLE usuario ALTER COLUMN id RESTART WITH 1;",
    "ALTER TABLE tipo_usuario ALTER COLUMN id RESTART WITH 1;",

    "INSERT INTO tipo_usuario (nome, descricao, ativo) VALUES ('MASTER', 'Administrador master', TRUE);",
    "INSERT INTO tipo_usuario (nome, descricao, ativo) VALUES ('CLIENTE', 'Cliente do sistema', TRUE);",
    "INSERT INTO tipo_usuario (nome, descricao, ativo) VALUES ('DONO_RESTAURANTE', 'Proprietario', TRUE);"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/usuarios";

    @Test
    @DisplayName("GET /usuarios deve listar usuarios")
    @WithMockUser
    void deveListarUsuarios() throws Exception {

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    @DisplayName("POST /usuarios deve criar usuario")
    @WithMockUser
    void deveCriarUsuario() throws Exception {

        String json = """
        {
          "nome": "JOAO",
          "cpf": "12345678909",
          "email": "joao@email.com",
          "telefone": "11999999999",
          "tipoUsuarioId": 2,
          "senha": "123456"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.nome").value("JOAO"));
    }

    @Sql(statements = {
        "INSERT INTO usuario (id, nome, cpf, email, telefone, tipo_usuario_id, ativo, senha_hash) " +
            "VALUES (2, 'Usuario Teste', '98765432100', 'usuario@email.com', '11999999999', 2, TRUE, '123456');"
    })
    @Test
    @DisplayName("GET /usuarios/{id} deve buscar usuario existente")
    @WithMockUser
    void deveBuscarUsuarioPorId() throws Exception {

        mockMvc.perform(get(BASE_URL + "/2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    @DisplayName("GET /usuarios/{id} deve retornar 404 para usuario inexistente")
    @WithMockUser
    void deveRetornar404QuandoUsuarioNaoExiste() throws Exception {

        mockMvc.perform(get(BASE_URL + "/9999")
                .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isNotFound());
    }

    @Sql(statements = {
        "INSERT INTO tipo_usuario (id, nome, descricao, ativo) VALUES (99, 'TESTE', 'Tipo Teste', TRUE);",
        "INSERT INTO usuario (id, nome, cpf, email, telefone, tipo_usuario_id, ativo, senha_hash) " +
            "VALUES (10, 'Usuario Teste', '98765432100', 'usuario@email.com', '11999999999', 99, TRUE, '123456');"
    })
    @Test
    @DisplayName("DELETE /usuarios/{id} deve deletar usuario existente")
    @WithMockUser
    void deveDeletarUsuario() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/10"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} deve retornar 404 para id inexistente")
    @WithMockUser
    void deveRetornar404AoDeletarUsuarioInexistente() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/9999")
                .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /usuarios/{id} deve atualizar usuario existente")
    @WithMockUser
    void deveAtualizarUsuario() throws Exception {

        // 1. Criar usuário primeiro
        String criarJson = """
    {
      "nome": "MARIA",
      "cpf": "11111111111",
      "email": "maria@email.com",
      "telefone": "11999990000",
      "tipoUsuarioId": 2,
      "senha": "senha123"
    }
    """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(criarJson))
            .andExpect(status().isCreated());

        // 2. Atualizar usuário criado
        String atualizarJson = """
    {
      "nome": "MARIA SILVA",
      "telefone": "11988887777",
      "email": "maria.silva@email.com",
      "ativo": true
    }
    """;

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(atualizarJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("MARIA SILVA"))
            .andExpect(jsonPath("$.telefone").value("11988887777"))
            .andExpect(jsonPath("$.email").value("maria.silva@email.com"));
    }

}
