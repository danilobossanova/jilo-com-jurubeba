package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

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

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;

@ExtendWith(MockitoExtension.class)
class AtualizarRestauranteUseCaseTest {

    @Mock private RestauranteGateway restauranteGateway;

    private AtualizarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarRestauranteUseCase(restauranteGateway);
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void deveAtualizarComSucesso() {
        Restaurante existente = RestauranteTestFactory.criarRestauranteValido();
        AtualizarRestauranteInput input =
                new AtualizarRestauranteInput(1L, "Novo nome", null, null, null, null, 1L);

        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(restauranteGateway.salvar(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        RestauranteOutput output = useCase.executar(input);

        assertThat(output.nome()).isEqualTo("NOVO NOME");
        assertThat(output.endereco()).isEqualTo("Rua A, 123");
    }

    @Test
    @DisplayName("Deve lancar excecao quando restaurante nao existir")
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        AtualizarRestauranteInput input =
                new AtualizarRestauranteInput(99L, "Nome", "Endereco", null, null, null, 1L);

        when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
