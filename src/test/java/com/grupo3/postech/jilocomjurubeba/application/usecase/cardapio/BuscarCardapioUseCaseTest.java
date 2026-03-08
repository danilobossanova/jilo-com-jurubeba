package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.CardapioTestFactory;

@ExtendWith(MockitoExtension.class)
class BuscarCardapioUseCaseTest {

    @Mock private CardapioGateway cardapioGateway;

    private BuscarCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarCardapioUseCase(cardapioGateway);
    }

    @Test
    @DisplayName("Deve buscar cardapio por id com sucesso")
    void deveBuscarPorIdComSucesso() {
        when(cardapioGateway.findByIdCardapio(1L)).thenReturn(Optional.of(CardapioTestFactory.criarCardapioValido()));

        CardapioOutput output = useCase.executar(1L);

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("PIZZA");
    }

    @Test
    @DisplayName("Deve lancar excecao quando cardapio nao existir")
    void deveLancarExcecaoQuandoNaoExistir() {
        when(cardapioGateway.findByIdCardapio(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}

