package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AtualizarRestauranteInputTest {

  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new JavaTimeModule()); // Necessário para LocalTime

  @Test
  @DisplayName("Deve criar AtualizarRestauranteInput corretamente via construtor")
  void deveCriarViaConstrutor() {

    AtualizarRestauranteInput input =
        new AtualizarRestauranteInput(
            1L,
            "Restaurante Bom Sabor",
            "Rua A, 123",
            TypeCozinha.BRASILEIRA,
            LocalTime.of(9, 0),
            LocalTime.of(22, 0),
            10L);

    assertThat(input.id()).isEqualTo(1L);
    assertThat(input.nome()).isEqualTo("Restaurante Bom Sabor");
    assertThat(input.endereco()).isEqualTo("Rua A, 123");
    assertThat(input.typeCozinha()).isEqualTo(TypeCozinha.BRASILEIRA);
    assertThat(input.horaAbertura()).isEqualTo(LocalTime.of(9, 0));
    assertThat(input.horaFechamento()).isEqualTo(LocalTime.of(22, 0));
    assertThat(input.donoId()).isEqualTo(10L);
  }

  @Test
  @DisplayName("Deve desserializar JSON para AtualizarRestauranteInput")
  void deveDesserializarJson() throws Exception {

    String json =
        """
        {
          "id": 2,
          "nome": "Sushi House",
          "endereco": "Av. Central, 500",
          "typeCozinha": "JAPONESA",
          "horaAbertura": "11:00",
          "horaFechamento": "23:00",
          "donoId": 7
        }
        """;

    AtualizarRestauranteInput input = objectMapper.readValue(json, AtualizarRestauranteInput.class);

    assertThat(input.id()).isEqualTo(2L);
    assertThat(input.nome()).isEqualTo("Sushi House");
    assertThat(input.endereco()).isEqualTo("Av. Central, 500");
    assertThat(input.typeCozinha()).isEqualTo(TypeCozinha.JAPONESA);
    assertThat(input.horaAbertura()).isEqualTo(LocalTime.of(11, 0));
    assertThat(input.horaFechamento()).isEqualTo(LocalTime.of(23, 0));
    assertThat(input.donoId()).isEqualTo(7L);
  }

  @Test
  @DisplayName("Deve serializar AtualizarRestauranteInput para JSON")
  void deveSerializarParaJson() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    AtualizarRestauranteInput input =
        new AtualizarRestauranteInput(
            3L,
            "Japonese Food",
            "Rua das Flores, 88",
            TypeCozinha.JAPONESA,
            LocalTime.of(12, 0),
            LocalTime.of(23, 30),
            15L);

    String json = objectMapper.writeValueAsString(input);

    assertThat(json).contains("\"id\":3");
    assertThat(json).contains("\"nome\":\"Japonese Food\"");
    assertThat(json).contains("\"endereco\":\"Rua das Flores, 88\"");
    assertThat(json).contains("\"typeCozinha\":\"JAPONESA\"");
    assertThat(json).contains("\"horaAbertura\":\"12:00:00\"");
    assertThat(json).contains("\"horaFechamento\":\"23:30:00\"");
    ;
    assertThat(json).contains("\"donoId\":15");
  }
}
