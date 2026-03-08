package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.CardapioTestFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListarCardapioUseCaseTest {

  @Mock private CardapioGateway cardapioGateway;

  private ListarCardapioUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new ListarCardapioUseCase(cardapioGateway);
  }

  @Test
  @DisplayName("Deve listar cardapios com sucesso")
  void deveListarComSucesso() {
    when(cardapioGateway.findAllCardapio())
        .thenReturn(
            List.of(
                CardapioTestFactory.criarCardapioValido(),
                CardapioTestFactory.criarCardapioApenasLocal()));

    List<CardapioOutput> output = useCase.executar();

    assertThat(output).hasSize(2);
    assertThat(output.get(0).id()).isEqualTo(1L);
    assertThat(output.get(1).apenasNoLocal()).isTrue();
  }
}
