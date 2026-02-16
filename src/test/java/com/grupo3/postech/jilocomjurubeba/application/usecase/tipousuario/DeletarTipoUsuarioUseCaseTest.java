package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
class DeletarTipoUsuarioUseCaseTest {

    @Mock private TipoUsuarioGateway gateway;

    private DeletarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeletarTipoUsuarioUseCase(gateway);
    }

    @Test
    @DisplayName("Deve desativar tipo de usuario com sucesso")
    void deveDesativarComSucesso() {
        TipoUsuario tipo = new TipoUsuario(1L, "MASTER", "Administrador", true);
        when(gateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));
        when(gateway.salvar(any(TipoUsuario.class))).thenReturn(tipo);

        useCase.executar(1L);

        verify(gateway).salvar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando tipo nao encontrado para delecao")
    void deveLancarExcecaoQuandoNaoEncontrado() {
        when(gateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);

        verify(gateway, never()).salvar(any());
    }
}
