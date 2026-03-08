package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioResumoOutputTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve criar UsuarioResumoOutput corretamente via construtor")
    void deveCriarViaConstrutor() {

        UsuarioResumoOutput output = new UsuarioResumoOutput(
            1L,
            "Juliana",
            "juliana@email.com"
        );

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("Juliana");
        assertThat(output.email()).isEqualTo("juliana@email.com");
    }

    @Test
    @DisplayName("Deve desserializar JSON para UsuarioResumoOutput")
    void deveDesserializarJson() throws Exception {

        String json = """
        {
          "id": 2,
          "nome": "Carlos",
          "email": "carlos@email.com"
        }
        """;

        UsuarioResumoOutput output =
            objectMapper.readValue(json, UsuarioResumoOutput.class);

        assertThat(output.id()).isEqualTo(2L);
        assertThat(output.nome()).isEqualTo("Carlos");
        assertThat(output.email()).isEqualTo("carlos@email.com");
    }

    @Test
    @DisplayName("Deve serializar UsuarioResumoOutput para JSON")
    void deveSerializarParaJson() throws Exception {

        UsuarioResumoOutput output = new UsuarioResumoOutput(
            3L,
            "Marina",
            "marina@email.com"
        );

        String json = objectMapper.writeValueAsString(output);

        assertThat(json).contains("\"id\":3");
        assertThat(json).contains("\"nome\":\"Marina\"");
        assertThat(json).contains("\"email\":\"marina@email.com\"");
    }
}
