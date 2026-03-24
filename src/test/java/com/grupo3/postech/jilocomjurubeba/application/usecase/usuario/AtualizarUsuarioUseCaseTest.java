package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock private UsuarioGateway usuarioGateway;

    private AtualizarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarUsuarioUseCase(usuarioGateway);
    }

    @Test
    @DisplayName("Deve atualizar usuario com sucesso")
    void deveAtualizarComSucesso() {
        TipoUsuario tipoAtual = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
        Usuario atual = novoUsuario(1L, "12345678909", "usuario@email.com", tipoAtual);
        AtualizarUsuarioInput input =
                new AtualizarUsuarioInput(
                        1L, "Usuario Atualizado", "novo@email.com", "11988887777");

        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(atual));
        when(usuarioGateway.salvar(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        UsuarioOutput output = useCase.executar(input);

        assertThat(output.nome()).isEqualTo("USUARIO ATUALIZADO");
        assertThat(output.email()).isEqualTo("novo@email.com");
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        AtualizarUsuarioInput input =
                new AtualizarUsuarioInput(99L, "Nome", "email@test.com", null);
        when(usuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    private Usuario novoUsuario(Long id, String cpf, String email, TipoUsuario tipo) {
        return new Usuario(
                id,
                "Usuario Teste",
                new Cpf(cpf),
                new Email(email),
                "11999999999",
                tipo,
                "123456",
                true,
                List.of());
    }
}
