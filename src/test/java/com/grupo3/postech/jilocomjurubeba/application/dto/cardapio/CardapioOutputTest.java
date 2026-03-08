package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CardapioOutputTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Deve criar CardapioOutput corretamente via construtor")
  void deveCriarViaConstrutor() {

    CardapioOutput output =
        new CardapioOutput(
            1L,
            "Pizza Calabresa",
            "Pizza com calabresa e cebola",
            new BigDecimal("39.90"),
            true,
            "/img/pizza.png",
            10L,
            true);

    assertThat(output.id()).isEqualTo(1L);
    assertThat(output.nome()).isEqualTo("Pizza Calabresa");
    assertThat(output.descricao()).isEqualTo("Pizza com calabresa e cebola");
    assertThat(output.preco()).isEqualByComparingTo("39.90");
    assertThat(output.apenasNoLocal()).isTrue();
    assertThat(output.caminhoFoto()).isEqualTo("/img/pizza.png");
    assertThat(output.restauranteId()).isEqualTo(10L);
    assertThat(output.ativo()).isTrue();
  }

  @Test
  @DisplayName("Deve desserializar JSON para CardapioOutput")
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
          "restauranteId": 5,
          "ativo": false
        }
        """;

    CardapioOutput output = objectMapper.readValue(json, CardapioOutput.class);

    assertThat(output.id()).isEqualTo(2L);
    assertThat(output.nome()).isEqualTo("Hamburguer");
    assertThat(output.descricao()).isEqualTo("Hamburguer artesanal");
    assertThat(output.preco()).isEqualByComparingTo("25.50");
    assertThat(output.apenasNoLocal()).isFalse();
    assertThat(output.caminhoFoto()).isEqualTo("/img/burger.png");
    assertThat(output.restauranteId()).isEqualTo(5L);
    assertThat(output.ativo()).isFalse();
  }

  @Test
  @DisplayName("Deve serializar CardapioOutput para JSON")
  void deveSerializarParaJson() throws Exception {

    CardapioOutput output =
        new CardapioOutput(
            3L,
            "Sushi",
            "Combo de sushi",
            new BigDecimal("59.90"),
            false,
            "/img/sushi.png",
            7L,
            true);

    String json = objectMapper.writeValueAsString(output);

    assertThat(json).contains("\"id\":3");
    assertThat(json).contains("\"nome\":\"Sushi\"");
    assertThat(json).contains("\"descricao\":\"Combo de sushi\"");
    assertThat(json).contains("\"preco\":59.90");
    assertThat(json).contains("\"apenasNoLocal\":false");
    assertThat(json).contains("\"caminhoFoto\":\"/img/sushi.png\"");
    assertThat(json).contains("\"restauranteId\":7");
    assertThat(json).contains("\"ativo\":true");
  }
}
