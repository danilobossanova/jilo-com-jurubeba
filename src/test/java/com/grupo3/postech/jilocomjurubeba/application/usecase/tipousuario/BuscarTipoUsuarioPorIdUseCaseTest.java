package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

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

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
class BuscarTipoUsuarioPorIdUseCaseTest {

    @Mock private TipoUsuarioGateway gateway;

    private BuscarTipoUsuarioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarTipoUsuarioPorIdUseCase(gateway);
    }

    @Test
    @DisplayName("Deve buscar tipo de usuario por id com sucesso")
    void deveBuscarPorIdComSucesso() {
        TipoUsuario tipo = new TipoUsuario(1L, "MASTER", "Administrador", true);
        when(gateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));

        TipoUsuarioOutput output = useCase.executar(1L);

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("MASTER");
    }

    @Test
    @DisplayName("Deve lancar excecao quando tipo nao encontrado")
    void deveLancarExcecaoQuandoNaoEncontrado() {
        when(gateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("TipoUsuario")
                .hasMessageContaining("99");
    }
}
