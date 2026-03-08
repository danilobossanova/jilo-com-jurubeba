package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CriarCardapioInputTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Deve criar CriarCardapioInput corretamente via construtor")
  void deveCriarViaConstrutor() {

    CriarCardapioInput input =
        new CriarCardapioInput(
            "Pizza Calabresa",
            "Pizza com calabresa e cebola",
            new BigDecimal("39.90"),
            true,
            "/img/pizza.png",
            10L);

    assertThat(input.nome()).isEqualTo("Pizza Calabresa");
    assertThat(input.descricao()).isEqualTo("Pizza com calabresa e cebola");
    assertThat(input.preco()).isEqualByComparingTo("39.90");
    assertThat(input.apenasNoLocal()).isTrue();
    assertThat(input.caminhoFoto()).isEqualTo("/img/pizza.png");
    assertThat(input.restauranteId()).isEqualTo(10L);
  }

  @Test
  @DisplayName("Deve desserializar JSON para CriarCardapioInput")
  void deveDesserializarJson() throws Exception {

    String json =
        """
        {
          "nome": "Hamburguer",
          "descricao": "Hamburguer artesanal",
          "preco": 25.50,
          "apenasNoLocal": false,
          "caminhoFoto": "/img/burger.png",
          "restauranteId": 5
        }
        """;

    CriarCardapioInput input = objectMapper.readValue(json, CriarCardapioInput.class);

    assertThat(input.nome()).isEqualTo("Hamburguer");
    assertThat(input.descricao()).isEqualTo("Hamburguer artesanal");
    assertThat(input.preco()).isEqualByComparingTo("25.50");
    assertThat(input.apenasNoLocal()).isFalse();
    assertThat(input.caminhoFoto()).isEqualTo("/img/burger.png");
    assertThat(input.restauranteId()).isEqualTo(5L);
  }

  @Test
  @DisplayName("Deve serializar CriarCardapioInput para JSON")
  void deveSerializarParaJson() throws Exception {

    CriarCardapioInput input =
        new CriarCardapioInput(
            "Sushi", "Combo de sushi", new BigDecimal("59.90"), false, "/img/sushi.png", 7L);

    String json = objectMapper.writeValueAsString(input);

    assertThat(json).contains("\"nome\":\"Sushi\"");
    assertThat(json).contains("\"descricao\":\"Combo de sushi\"");
    assertThat(json).contains("\"preco\":59.90");
    assertThat(json).contains("\"apenasNoLocal\":false");
    assertThat(json).contains("\"caminhoFoto\":\"/img/sushi.png\"");
    assertThat(json).contains("\"restauranteId\":7");
  }
}
