package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.CardapioTestFactory;

@ExtendWith(MockitoExtension.class)
class DeletarCardapioUseCaseTest {

    @Mock private CardapioGateway cardapioGateway;

    private DeletarCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeletarCardapioUseCase(cardapioGateway);
    }

    @Test
    @DisplayName("Deve deletar cardapio quando existir")
    void deveDeletarQuandoExistir() {
        when(cardapioGateway.buscarPorId(1L))
                .thenReturn(Optional.of(CardapioTestFactory.criarCardapioValido()));

        useCase.executar(1L);

        verify(cardapioGateway).salvar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando cardapio nao existir")
    void deveLancarExcecaoQuandoNaoExistir() {
        when(cardapioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
