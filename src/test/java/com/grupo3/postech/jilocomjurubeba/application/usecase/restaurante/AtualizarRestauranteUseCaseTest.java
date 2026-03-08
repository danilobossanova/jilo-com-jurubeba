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
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;

@ExtendWith(MockitoExtension.class)
class AtualizarRestauranteUseCaseTest {

    @Mock private RestauranteGateway restauranteGateway;
    @Mock private UsuarioGateway usuarioGateway;

    private AtualizarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarRestauranteUseCase(restauranteGateway, usuarioGateway);
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void deveAtualizarComSucesso() {
        Restaurante existente = RestauranteTestFactory.criarRestauranteValido();
        Usuario novoDono = UsuarioTestFactory.criarDonoRestaurante();
        AtualizarRestauranteInput input = new AtualizarRestauranteInput(
                1L,
                "Novo nome",
                null,
                null,
                null,
                null,
                2L
        );

        when(restauranteGateway.findByIdRestaurante(1L)).thenReturn(Optional.of(existente));
        when(usuarioGateway.findByIdUsuario(2L)).thenReturn(Optional.of(novoDono));
        when(restauranteGateway.saveRestaurante(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

        RestauranteOutput output = useCase.executar(input);

        assertThat(output.nome()).isEqualTo("NOVO NOME");
        assertThat(output.endereco()).isEqualTo("Rua A, 123");
        assertThat(output.donoId()).isEqualTo(2L);
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
        AtualizarRestauranteInput input = new AtualizarRestauranteInput(
                null,
                "Nome",
                "Endereco",
                null,
                null,
                null,
                1L
        );

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    @DisplayName("Deve lancar excecao quando restaurante nao existir")
    void deveLancarExcecaoQuandoRestauranteNaoExistir() {
        AtualizarRestauranteInput input = new AtualizarRestauranteInput(
                99L,
                "Nome",
                "Endereco",
                null,
                null,
                null,
                1L
        );

        when(restauranteGateway.findByIdRestaurante(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    @DisplayName("Deve lancar excecao quando dono informado nao existir")
    void deveLancarExcecaoQuandoDonoInformadoNaoExistir() {
        Restaurante existente = RestauranteTestFactory.criarRestauranteValido();
        AtualizarRestauranteInput input = new AtualizarRestauranteInput(
                1L,
                null,
                null,
                null,
                null,
                null,
                99L
        );

        when(restauranteGateway.findByIdRestaurante(1L)).thenReturn(Optional.of(existente));
        when(usuarioGateway.findByIdUsuario(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}

