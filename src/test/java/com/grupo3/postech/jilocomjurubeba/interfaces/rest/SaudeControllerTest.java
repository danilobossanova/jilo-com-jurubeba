package com.grupo3.postech.jilocomjurubeba.interfaces.rest;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
 * Testes de integração para o SaudeController.
 *
 * <p>Valida o fluxo completo end-to-end: HTTP Request -> Controller -> UseCase -> Response
 *
 * @author Danilo Fernando
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SaudeControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    @DisplayName("GET /v1/health deve retornar status UP")
    @WithMockUser
    void deveRetornarStatusUp() throws Exception {
        mockMvc.perform(get("/v1/health").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.versao", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/health deve retornar JSON válido")
    @WithMockUser
    void deveRetornarJsonValido() throws Exception {
        mockMvc.perform(get("/v1/health").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.versao").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
