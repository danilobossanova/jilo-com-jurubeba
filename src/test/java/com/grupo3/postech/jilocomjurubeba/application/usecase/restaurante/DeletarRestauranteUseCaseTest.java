package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

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

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;

@ExtendWith(MockitoExtension.class)
class DeletarRestauranteUseCaseTest {

    @Mock private RestauranteGateway restauranteGateway;

    private DeletarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeletarRestauranteUseCase(restauranteGateway);
    }

    @Test
    @DisplayName("Deve deletar restaurante quando existir")
    void deveDeletarQuandoExistir() {
        when(restauranteGateway.buscarPorId(1L))
                .thenReturn(Optional.of(RestauranteTestFactory.criarRestauranteValido()));

        useCase.executar(1L);

        verify(restauranteGateway).salvar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando restaurante nao existir")
    void deveLancarExcecaoQuandoNaoEncontrado() {
        when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
