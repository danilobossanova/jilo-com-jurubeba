package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
class ListarTiposUsuarioUseCaseTest {

    @Mock private TipoUsuarioGateway gateway;

    private ListarTiposUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarTiposUsuarioUseCase(gateway);
    }

    @Test
    @DisplayName("Deve listar todos os tipos de usuario")
    void deveListarTodos() {
        List<TipoUsuario> tipos =
                List.of(
                        new TipoUsuario(1L, "MASTER", "Administrador", true),
                        new TipoUsuario(2L, "CLIENTE", "Consumidor", true));
        when(gateway.listarTodos()).thenReturn(tipos);

        List<TipoUsuarioOutput> outputs = useCase.executar();

        assertThat(outputs).hasSize(2);
        assertThat(outputs.get(0).nome()).isEqualTo("MASTER");
        assertThat(outputs.get(1).nome()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao existem tipos")
    void deveRetornarListaVazia() {
        when(gateway.listarTodos()).thenReturn(List.of());

        List<TipoUsuarioOutput> outputs = useCase.executar();

        assertThat(outputs).isEmpty();
    }
}
