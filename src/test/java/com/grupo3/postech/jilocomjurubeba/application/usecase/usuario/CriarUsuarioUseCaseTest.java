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

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.CriarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.CriptografiaSenhaGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

    @Mock private UsuarioGateway usuarioGateway;
    @Mock private TipoUsuarioGateway tipoUsuarioGateway;
    @Mock private CriptografiaSenhaGateway criptografiaSenhaGateway;

    private CriarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase =
                new CriarUsuarioUseCase(
                        usuarioGateway, tipoUsuarioGateway, criptografiaSenhaGateway);
    }

    @Test
    @DisplayName("Deve criar usuario com sucesso")
    void deveCriarComSucesso() {
        CriarUsuarioInput input =
                new CriarUsuarioInput(
                        "Usuario Teste",
                        "12345678909",
                        "usuario@email.com",
                        "11999999999",
                        1L,
                        "123456");
        TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
        Usuario salvo =
                new Usuario(
                        10L,
                        "Usuario Teste",
                        new Cpf("12345678909"),
                        new Email("usuario@email.com"),
                        "11999999999",
                        tipo,
                        "$2a$10$hash",
                        true,
                        List.of());

        when(usuarioGateway.existePorCpf("12345678909")).thenReturn(false);
        when(usuarioGateway.existePorEmail("usuario@email.com")).thenReturn(false);
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));
        when(criptografiaSenhaGateway.criptografar("123456")).thenReturn("$2a$10$hash");
        when(usuarioGateway.salvar(any(Usuario.class))).thenReturn(salvo);

        UsuarioOutput output = useCase.executar(input);

        assertThat(output.id()).isEqualTo(10L);
        assertThat(output.cpf()).isEqualTo("12345678909");
        assertThat(output.email()).isEqualTo("usuario@email.com");
    }

    @Test
    @DisplayName("Deve lancar excecao quando cpf ja estiver cadastrado")
    void deveLancarExcecaoQuandoCpfDuplicado() {
        CriarUsuarioInput input =
                new CriarUsuarioInput(
                        "Usuario Teste",
                        "12345678909",
                        "usuario@email.com",
                        "11999999999",
                        1L,
                        "123456");

        when(usuarioGateway.existePorCpf("12345678909")).thenReturn(true);

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    @DisplayName("Deve lancar excecao quando email ja estiver cadastrado")
    void deveLancarExcecaoQuandoEmailDuplicado() {
        CriarUsuarioInput input =
                new CriarUsuarioInput(
                        "Usuario Teste",
                        "12345678909",
                        "usuario@email.com",
                        "11999999999",
                        1L,
                        "123456");

        when(usuarioGateway.existePorCpf("12345678909")).thenReturn(false);
        when(usuarioGateway.existePorEmail("usuario@email.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    @DisplayName("Deve lancar excecao quando tipo de usuario nao existir")
    void deveLancarExcecaoQuandoTipoNaoExistir() {
        CriarUsuarioInput input =
                new CriarUsuarioInput(
                        "Usuario Teste",
                        "12345678909",
                        "usuario@email.com",
                        "11999999999",
                        99L,
                        "123456");

        when(usuarioGateway.existePorCpf("12345678909")).thenReturn(false);
        when(usuarioGateway.existePorEmail("usuario@email.com")).thenReturn(false);
        when(tipoUsuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }
}
