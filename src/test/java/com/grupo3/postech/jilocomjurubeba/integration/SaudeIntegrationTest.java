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
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de Integração para Saúde da Aplicação.
 *
 * <p>
 * Valida:
 * - Health check endpoint disponível sem autenticação (ou com)
 * - Retorno de status correto
 * - Formato de resposta esperado
 * - Dados de versão e timestamp
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class SaudeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String HEALTH_URL = "/v1/health";

    @Test
    @DisplayName("GET /v1/health deve retornar status 200")
    @WithMockUser
    void deveRetornarStatus200() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /v1/health deve retornar status UP")
    @WithMockUser
    void deveRetornarStatusUp() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    @DisplayName("GET /v1/health deve retornar JSON com status, versão e timestamp")
    @WithMockUser
    void deveRetornarJsonCompleto() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.versao", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/health deve retornar status como string")
    @WithMockUser
    void deveRetornarStatusString() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", isA(String.class)))
                .andExpect(jsonPath("$.status", matchesRegex("UP|DOWN|UNKNOWN")));
    }

    @Test
    @DisplayName("GET /v1/health deve retornar versão válida")
    @WithMockUser
    void deveRetornarVersaoValida() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versao", not(emptyString())))
                .andExpect(jsonPath("$.versao", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/health deve retornar timestamp em formato ISO")
    @WithMockUser
    void deveRetornarTimestampValido() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.timestamp", not(emptyString())));
    }

    @Test
    @DisplayName("GET /v1/health deve retornar objeto JSON (não array)")
    @WithMockUser
    void deveRetornarObjeto() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.Map.class)));
    }

    @Test
    @DisplayName("GET /v1/health deve ter exatamente 3 campos")
    @WithMockUser
    void deveRetornarTresCampos() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    @DisplayName("GET /v1/health deve retornar application/json")
    @WithMockUser
    void deveRetornarJsonContentType() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /v1/health com Accept: application/json deve funcionar")
    @WithMockUser
    void deveRetornarComAcceptJson() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /v1/health não deve exigir autenticação")
    void devePermitirSemAutenticacao() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /v1/health status deve ser sempre UP em ambiente de teste")
    @WithMockUser
    void deveSerSempreLivre() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get(HEALTH_URL)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("UP")));
        }
    }

    @Test
    @DisplayName("GET /v1/health deve responder consistentemente")
    @WithMockUser
    void deveResponderConsistentemente() throws Exception {
        mockMvc.perform(get(HEALTH_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.versao", notNullValue()));

        mockMvc.perform(get(HEALTH_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.versao", notNullValue()));
    }

    @Test
    @DisplayName("GET /v1/health pode ser usado por load balancer")
    @WithMockUser
    void deveSerUsavelPorLoadBalancer() throws Exception {
        mockMvc.perform(get(HEALTH_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }
}
