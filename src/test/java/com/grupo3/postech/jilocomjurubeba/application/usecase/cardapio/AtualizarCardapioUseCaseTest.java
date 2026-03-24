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
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.CardapioTestFactory;

@ExtendWith(MockitoExtension.class)
class AtualizarCardapioUseCaseTest {

    @Mock private CardapioGateway cardapioGateway;

    private AtualizarCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarCardapioUseCase(cardapioGateway);
    }

    @Test
    @DisplayName("Deve atualizar cardapio com sucesso")
    void deveAtualizarComSucesso() {
        Cardapio existente = CardapioTestFactory.criarCardapioValido();
        AtualizarCardapioInput input =
                new AtualizarCardapioInput(1L, "Nova pizza", null, null, true, null, 1L);

        when(cardapioGateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(cardapioGateway.salvar(any(Cardapio.class))).thenAnswer(i -> i.getArgument(0));

        CardapioOutput output = useCase.executar(input);

        assertThat(output.nome()).isEqualTo("NOVA PIZZA");
        assertThat(output.apenasNoLocal()).isTrue();
        assertThat(output.restauranteId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao quando cardapio nao existir")
    void deveLancarExcecaoQuandoCardapioNaoExiste() {
        AtualizarCardapioInput input =
                new AtualizarCardapioInput(99L, "Nome", "Desc", null, false, null, 1L);

        when(cardapioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
