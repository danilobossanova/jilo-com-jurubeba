package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.factory.CardapioTestFactory;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CriarCardapioUseCaseTest {

  @Mock private CardapioGateway cardapioGateway;
  @Mock private RestauranteGateway restauranteGateway;

  private CriarCardapioUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new CriarCardapioUseCase(cardapioGateway, restauranteGateway);
  }

  @Test
  @DisplayName("Deve criar cardapio com sucesso")
  void deveCriarComSucesso() {
    Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();
    CriarCardapioInput input =
        new CriarCardapioInput(
            "Pizza especial", "Pizza grande", BigDecimal.valueOf(59.90), false, "/foto.png", 1L);
    Cardapio salvo =
        new Cardapio(
            10L,
            "Pizza especial",
            "Pizza grande",
            BigDecimal.valueOf(59.90),
            false,
            "/foto.png",
            restaurante,
            true);

    when(cardapioGateway.findByNome("Pizza especial")).thenReturn(Optional.empty());
    when(restauranteGateway.findByIdRestaurante(1L)).thenReturn(Optional.of(restaurante));
    when(cardapioGateway.saveCardapio(any(Cardapio.class))).thenReturn(salvo);

    CardapioOutput output = useCase.executar(input);

    assertThat(output.id()).isEqualTo(10L);
    assertThat(output.nome()).isEqualTo("PIZZA ESPECIAL");
    assertThat(output.restauranteId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("Deve lancar excecao quando item ja existe")
  void deveLancarExcecaoQuandoJaExiste() {
    CriarCardapioInput input =
        new CriarCardapioInput("Pizza", "Descricao", BigDecimal.TEN, false, null, 1L);

    when(cardapioGateway.findByNome("Pizza"))
        .thenReturn(Optional.of(CardapioTestFactory.criarCardapioValido()));

    assertThatThrownBy(() -> useCase.executar(input)).isInstanceOf(RegraDeNegocioException.class);
    verify(cardapioGateway, never()).saveCardapio(any());
  }

  @Test
  @DisplayName("Deve lancar excecao quando restaurante nao existir")
  void deveLancarExcecaoQuandoRestauranteNaoExiste() {
    CriarCardapioInput input =
        new CriarCardapioInput("Pizza", "Descricao", BigDecimal.TEN, false, null, 99L);

    when(cardapioGateway.findByNome("Pizza")).thenReturn(Optional.empty());
    when(restauranteGateway.findByIdRestaurante(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(EntidadeNaoEncontradaException.class);
    verify(cardapioGateway, never()).saveCardapio(any());
  }

  @Test
  @DisplayName("Deve lancar excecao quando nome for nulo")
  void deveLancarExcecaoQuandoNomeForNulo() {
    CriarCardapioInput input =
        new CriarCardapioInput(null, "Descricao", BigDecimal.TEN, false, null, 1L);

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(RegraDeNegocioException.class)
        .hasMessage("nome é obrigatório");
    verify(cardapioGateway, never()).saveCardapio(any());
  }

  @Test
  @DisplayName("Deve lancar excecao quando nome for em branco")
  void deveLancarExcecaoQuandoNomeForEmBranco() {
    CriarCardapioInput input =
        new CriarCardapioInput("   ", "Descricao", BigDecimal.TEN, false, null, 1L);

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(RegraDeNegocioException.class)
        .hasMessage("nome é obrigatório");
    verify(cardapioGateway, never()).saveCardapio(any());
  }
}
