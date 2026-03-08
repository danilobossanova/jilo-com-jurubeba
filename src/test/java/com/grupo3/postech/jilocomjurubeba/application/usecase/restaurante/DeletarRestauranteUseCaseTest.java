package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

@ExtendWith(MockitoExtension.class)
class DeletarRestauranteUseCaseTest {

    @Mock private RestauranteGateway restauranteGateway;

    private DeletarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeletarRestauranteUseCase(restauranteGateway);
    }

    @Test
    @DisplayName("Deve delegar exclusao para gateway")
    void deveDelegarExclusaoParaGateway() {
        useCase.executar(1L);

        verify(restauranteGateway).deleteRestaurante(1L);
    }

    @Test
    @DisplayName("Deve propagar excecao de entidade nao encontrada")
    void devePropagarExcecaoQuandoNaoEncontrado() {
        doThrow(new EntidadeNaoEncontradaException("Restaurante", 99L))
                .when(restauranteGateway).deleteRestaurante(99L);

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}

