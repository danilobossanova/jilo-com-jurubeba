package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.factory.CardapioTestFactory;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;

@ExtendWith(MockitoExtension.class)
class AtualizarCardapioUseCaseTest {

    @Mock private CardapioGateway cardapioGateway;
    @Mock private RestauranteGateway restauranteGateway;

    private AtualizarCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarCardapioUseCase(cardapioGateway, restauranteGateway);
    }

    @Test
    @DisplayName("Deve atualizar cardapio com sucesso")
    void deveAtualizarComSucesso() {
        Cardapio existente = CardapioTestFactory.criarCardapioValido();
        Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();
        AtualizarCardapioInput input = new AtualizarCardapioInput(
                1L,
                "Nova pizza",
                null,
                null,
                true,
                null,
                1L
        );

        when(cardapioGateway.findByIdCardapio(1L)).thenReturn(Optional.of(existente));
        when(restauranteGateway.findByIdRestaurante(1L)).thenReturn(Optional.of(restaurante));
        when(cardapioGateway.saveCardapio(any(Cardapio.class))).thenAnswer(i -> i.getArgument(0));

        CardapioOutput output = useCase.executar(input);

        assertThat(output.nome()).isEqualTo("NOVA PIZZA");
        assertThat(output.apenasNoLocal()).isTrue();
        assertThat(output.restauranteId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao quando input for nulo")
    void deveLancarExcecaoQuandoInputForNulo() {
        assertThatThrownBy(() -> useCase.executar(null))
                .isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    @DisplayName("Deve lancar excecao quando id for nulo")
    void deveLancarExcecaoQuandoIdForNulo() {
        AtualizarCardapioInput input = new AtualizarCardapioInput(
                null,
                "Nome",
                "Desc",
                null,
                null,
                null,
                1L
        );

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    @DisplayName("Deve lancar excecao quando cardapio nao existir")
    void deveLancarExcecaoQuandoCardapioNaoExiste() {
        AtualizarCardapioInput input = new AtualizarCardapioInput(
                99L,
                "Nome",
                "Desc",
                null,
                null,
                null,
                1L
        );

        when(cardapioGateway.findByIdCardapio(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    @DisplayName("Deve lancar excecao quando restaurante nao existir")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        Cardapio existente = CardapioTestFactory.criarCardapioValido();
        AtualizarCardapioInput input = new AtualizarCardapioInput(
                1L,
                "Nome",
                "Desc",
                null,
                null,
                null,
                99L
        );

        when(cardapioGateway.findByIdCardapio(1L)).thenReturn(Optional.of(existente));
        when(restauranteGateway.findByIdRestaurante(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}

