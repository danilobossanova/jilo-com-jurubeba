package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class CriarRestauranteInputTest {

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    @DisplayName("Deve criar CriarRestauranteInput corretamente via construtor")
    void deveCriarViaConstrutor() {

        CriarRestauranteInput input =
                new CriarRestauranteInput(
                        "Restaurante Bom Sabor",
                        "Rua A, 123",
                        "BRASILEIRA",
                        LocalTime.of(9, 0),
                        LocalTime.of(22, 0),
                        10L);

        assertThat(input.nome()).isEqualTo("Restaurante Bom Sabor");
        assertThat(input.endereco()).isEqualTo("Rua A, 123");
        assertThat(input.tipoCozinha()).isEqualTo("BRASILEIRA");
        assertThat(input.horaAbertura()).isEqualTo(LocalTime.of(9, 0));
        assertThat(input.horaFechamento()).isEqualTo(LocalTime.of(22, 0));
        assertThat(input.donoId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Deve desserializar JSON para CriarRestauranteInput")
    void deveDesserializarJson() throws Exception {

        String json =
                """
        {
          "nome": "Sushi House",
          "endereco": "Av. Central, 500",
          "tipoCozinha": "JAPONESA",
          "horaAbertura": "11:00:00",
          "horaFechamento": "23:00:00",
          "donoId": 7
        }
        """;

        CriarRestauranteInput input = objectMapper.readValue(json, CriarRestauranteInput.class);

        assertThat(input.nome()).isEqualTo("Sushi House");
        assertThat(input.endereco()).isEqualTo("Av. Central, 500");
        assertThat(input.tipoCozinha()).isEqualTo("JAPONESA");
        assertThat(input.horaAbertura()).isEqualTo(LocalTime.of(11, 0));
        assertThat(input.horaFechamento()).isEqualTo(LocalTime.of(23, 0));
        assertThat(input.donoId()).isEqualTo(7L);
    }

    @Test
    @DisplayName("Deve serializar CriarRestauranteInput para JSON")
    void deveSerializarParaJson() throws Exception {

        CriarRestauranteInput input =
                new CriarRestauranteInput(
                        "Nostra Comida",
                        "Rua das Flores, 88",
                        "ITALIANA",
                        LocalTime.of(12, 0),
                        LocalTime.of(23, 30),
                        15L);

        String json = objectMapper.writeValueAsString(input);

        assertThat(json).contains("\"nome\":\"Nostra Comida\"");
        assertThat(json).contains("\"endereco\":\"Rua das Flores, 88\"");
        assertThat(json).contains("\"tipoCozinha\":\"ITALIANA\"");
        assertThat(json).contains("\"horaAbertura\":\"12:00:00\"");
        assertThat(json).contains("\"horaFechamento\":\"23:30:00\"");
        assertThat(json).contains("\"donoId\":15");
    }
}
