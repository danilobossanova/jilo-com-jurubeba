package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;

@ExtendWith(MockitoExtension.class)
class ListarUsuarioUseCaseTest {

    @Mock private UsuarioGateway usuarioGateway;

    private ListarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarUsuarioUseCase(usuarioGateway);
    }

    @Test
    @DisplayName("Deve listar usuarios com sucesso")
    void deveListarComSucesso() {
        when(usuarioGateway.listarTodos())
                .thenReturn(
                        List.of(
                                UsuarioTestFactory.criarUsuarioValido(),
                                UsuarioTestFactory.criarUsuarioInativo()));

        List<UsuarioOutput> output = useCase.executar();

        assertThat(output).hasSize(2);
        assertThat(output.get(0).id()).isEqualTo(1L);
        assertThat(output.get(1).ativo()).isFalse();
    }
}
