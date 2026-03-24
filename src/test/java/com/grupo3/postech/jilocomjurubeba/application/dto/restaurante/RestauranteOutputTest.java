package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class RestauranteOutputTest {

    private final ObjectMapper objectMapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    @DisplayName("Deve criar RestauranteOutput corretamente via construtor")
    void deveCriarViaConstrutor() {

        RestauranteOutput output =
                new RestauranteOutput(
                        1L,
                        "Japonese Food",
                        "Rua das Flores, 88",
                        "JAPONESA",
                        LocalTime.of(12, 0),
                        LocalTime.of(23, 30),
                        10L,
                        true);

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("Japonese Food");
        assertThat(output.endereco()).isEqualTo("Rua das Flores, 88");
        assertThat(output.tipoCozinha()).isEqualTo("JAPONESA");
        assertThat(output.horaAbertura()).isEqualTo(LocalTime.of(12, 0));
        assertThat(output.horaFechamento()).isEqualTo(LocalTime.of(23, 30));
        assertThat(output.donoId()).isEqualTo(10L);
        assertThat(output.ativo()).isTrue();
    }

    @Test
    @DisplayName("Deve desserializar JSON para RestauranteOutput")
    void deveDesserializarJson() throws Exception {

        String json =
                """
        {
          "id": 2,
          "nome": "Sushi House",
          "endereco": "Av. Central, 500",
          "tipoCozinha": "JAPONESA",
          "horaAbertura": "11:00:00",
          "horaFechamento": "23:00:00",
          "donoId": 7,
          "ativo": false
        }
        """;

        RestauranteOutput output = objectMapper.readValue(json, RestauranteOutput.class);

        assertThat(output.id()).isEqualTo(2L);
        assertThat(output.nome()).isEqualTo("Sushi House");
        assertThat(output.endereco()).isEqualTo("Av. Central, 500");
        assertThat(output.tipoCozinha()).isEqualTo("JAPONESA");
        assertThat(output.horaAbertura()).isEqualTo(LocalTime.of(11, 0));
        assertThat(output.horaFechamento()).isEqualTo(LocalTime.of(23, 0));
        assertThat(output.donoId()).isEqualTo(7L);
        assertThat(output.ativo()).isFalse();
    }

    @Test
    @DisplayName("Deve serializar RestauranteOutput para JSON")
    void deveSerializarParaJson() throws Exception {

        RestauranteOutput output =
                new RestauranteOutput(
                        3L,
                        "Temaki House",
                        "Rua Sakura, 100",
                        "JAPONESA",
                        LocalTime.of(18, 0),
                        LocalTime.of(23, 0),
                        20L,
                        true);

        String json = objectMapper.writeValueAsString(output);

        assertThat(json).contains("\"id\":3");
        assertThat(json).contains("\"nome\":\"Temaki House\"");
        assertThat(json).contains("\"endereco\":\"Rua Sakura, 100\"");
        assertThat(json).contains("\"tipoCozinha\":\"JAPONESA\"");
        assertThat(json).contains("\"horaAbertura\":\"18:00:00\"");
        assertThat(json).contains("\"horaFechamento\":\"23:00:00\"");
        assertThat(json).contains("\"donoId\":20");
        assertThat(json).contains("\"ativo\":true");
    }
}
