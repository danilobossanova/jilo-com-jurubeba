package com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AtualizarCardapioRequestTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Deve criar AtualizarCardapioRequest corretamente via construtor")
  void deveCriarRequestViaConstrutor() {
    AtualizarCardapioRequest req =
        new AtualizarCardapioRequest(
            "Pizza",
            "Pizza de calabresa",
            new BigDecimal("39.90"),
            true,
            "/imagens/pizza.png",
            10L);

    assertThat(req.nome()).isEqualTo("Pizza");
    assertThat(req.descricao()).isEqualTo("Pizza de calabresa");
    assertThat(req.preco()).isEqualByComparingTo("39.90");
    assertThat(req.apenasNoLocal()).isTrue();
    assertThat(req.caminhoFoto()).isEqualTo("/imagens/pizza.png");
    assertThat(req.restauranteId()).isEqualTo(10L);
  }

  @Test
  @DisplayName("Deve desserializar JSON para AtualizarCardapioRequest")
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

    AtualizarCardapioRequest req = objectMapper.readValue(json, AtualizarCardapioRequest.class);

    assertThat(req.nome()).isEqualTo("Hamburguer");
    assertThat(req.descricao()).isEqualTo("Hamburguer artesanal");
    assertThat(req.preco()).isEqualByComparingTo("25.50");
    assertThat(req.apenasNoLocal()).isFalse();
    assertThat(req.caminhoFoto()).isEqualTo("/img/burger.png");
    assertThat(req.restauranteId()).isEqualTo(5L);
  }

  @Test
  @DisplayName("Deve serializar AtualizarCardapioRequest para JSON")
  void deveSerializarParaJson() throws Exception {
    AtualizarCardapioRequest req =
        new AtualizarCardapioRequest(
            "Sushi", "Combo de sushi", new BigDecimal("59.90"), false, "/img/sushi.png", 3L);

    String json = objectMapper.writeValueAsString(req);

    assertThat(json).contains("\"nome\":\"Sushi\"");
    assertThat(json).contains("\"descricao\":\"Combo de sushi\"");
    assertThat(json).contains("\"preco\":59.90");
    assertThat(json).contains("\"apenasNoLocal\":false");
    assertThat(json).contains("\"caminhoFoto\":\"/img/sushi.png\"");
    assertThat(json).contains("\"restauranteId\":3");
  }
}
