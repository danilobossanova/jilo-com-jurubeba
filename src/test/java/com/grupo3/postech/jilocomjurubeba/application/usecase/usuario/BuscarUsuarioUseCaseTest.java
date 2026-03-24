package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

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

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioUseCaseTest {

    @Mock private UsuarioGateway usuarioGateway;

    private BuscarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarUsuarioUseCase(usuarioGateway);
    }

    @Test
    @DisplayName("Deve buscar usuario por id com sucesso")
    void deveBuscarComSucesso() {
        when(usuarioGateway.buscarPorId(1L))
                .thenReturn(Optional.of(UsuarioTestFactory.criarUsuarioValido()));

        UsuarioOutput output = useCase.executar(1L);

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("USUARIO TESTE");
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existir")
    void deveLancarExcecaoQuandoNaoExistir() {
        when(usuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
