package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioRefInputTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve criar UsuarioRefInput corretamente via construtor")
    void deveCriarViaConstrutor() {

        UsuarioRefInput input = new UsuarioRefInput(5L);

        assertThat(input.id()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Deve desserializar JSON para UsuarioRefInput")
    void deveDesserializarJson() throws Exception {

        String json = """
        {
          "id": 10
        }
        """;

        UsuarioRefInput input =
            objectMapper.readValue(json, UsuarioRefInput.class);

        assertThat(input.id()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Deve serializar UsuarioRefInput para JSON")
    void deveSerializarParaJson() throws Exception {

        UsuarioRefInput input = new UsuarioRefInput(3L);

        String json = objectMapper.writeValueAsString(input);

        assertThat(json).contains("\"id\":3");
    }
}
