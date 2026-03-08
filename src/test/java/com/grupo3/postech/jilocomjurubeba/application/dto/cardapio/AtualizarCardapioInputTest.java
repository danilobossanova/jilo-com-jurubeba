package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AtualizarCardapioInputTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Deve criar AtualizarCardapioInput corretamente via construtor")
  void deveCriarViaConstrutor() {

    AtualizarCardapioInput input =
        new AtualizarCardapioInput(
            1L,
            "Pizza Calabresa",
            "Pizza com calabresa e cebola",
            new BigDecimal("39.90"),
            true,
            "/img/pizza.png",
            10L);

    assertThat(input.id()).isEqualTo(1L);
    assertThat(input.nome()).isEqualTo("Pizza Calabresa");
    assertThat(input.descricao()).isEqualTo("Pizza com calabresa e cebola");
    assertThat(input.preco()).isEqualByComparingTo("39.90");
    assertThat(input.apenasNoLocal()).isTrue();
    assertThat(input.caminhoFoto()).isEqualTo("/img/pizza.png");
    assertThat(input.restauranteId()).isEqualTo(10L);
  }

  @Test
  @DisplayName("Deve desserializar JSON para AtualizarCardapioInput")
  void deveDesserializarJson() throws Exception {

    String json =
        """
        {
          "id": 2,
          "nome": "Hamburguer",
          "descricao": "Hamburguer artesanal",
          "preco": 25.50,
          "apenasNoLocal": false,
          "caminhoFoto": "/img/burger.png",
          "restauranteId": 5
        }
        """;

    AtualizarCardapioInput input = objectMapper.readValue(json, AtualizarCardapioInput.class);

    assertThat(input.id()).isEqualTo(2L);
    assertThat(input.nome()).isEqualTo("Hamburguer");
    assertThat(input.descricao()).isEqualTo("Hamburguer artesanal");
    assertThat(input.preco()).isEqualByComparingTo("25.50");
    assertThat(input.apenasNoLocal()).isFalse();
    assertThat(input.caminhoFoto()).isEqualTo("/img/burger.png");
    assertThat(input.restauranteId()).isEqualTo(5L);
  }

  @Test
  @DisplayName("Deve serializar AtualizarCardapioInput para JSON")
  void deveSerializarParaJson() throws Exception {

    AtualizarCardapioInput input =
        new AtualizarCardapioInput(
            3L, "Sushi", "Combo de sushi", new BigDecimal("59.90"), false, "/img/sushi.png", 7L);

    String json = objectMapper.writeValueAsString(input);

    assertThat(json).contains("\"id\":3");
    assertThat(json).contains("\"nome\":\"Sushi\"");
    assertThat(json).contains("\"descricao\":\"Combo de sushi\"");
    assertThat(json).contains("\"preco\":59.90");
    assertThat(json).contains("\"apenasNoLocal\":false");
    assertThat(json).contains("\"caminhoFoto\":\"/img/sushi.png\"");
    assertThat(json).contains("\"restauranteId\":7");
  }
}
