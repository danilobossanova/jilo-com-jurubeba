package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

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

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;

@ExtendWith(MockitoExtension.class)
class BuscarRestauranteUseCaseTest {

    @Mock private RestauranteGateway restauranteGateway;

    private BuscarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarRestauranteUseCase(restauranteGateway);
    }

    @Test
    @DisplayName("Deve buscar restaurante por id com sucesso")
    void deveBuscarComSucesso() {
        when(restauranteGateway.buscarPorId(1L))
                .thenReturn(Optional.of(RestauranteTestFactory.criarRestauranteValido()));

        RestauranteOutput output = useCase.executar(1L);

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("RESTAURANTE TESTE");
    }

    @Test
    @DisplayName("Deve lancar excecao quando restaurante nao existir")
    void deveLancarExcecaoQuandoNaoExistir() {
        when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
