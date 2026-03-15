package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.CriarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.PasswordHashGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

  @Mock private UsuarioGateway usuarioGateway;
  @Mock private TipoUsuarioGateway tipoUsuarioGateway;
  @Mock private PasswordHashGateway passwordHashGateway;

  private CriarUsuarioUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new CriarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway, passwordHashGateway);
  }

  @Test
  @DisplayName("Deve criar usuario com sucesso")
  void deveCriarComSucesso() {
    CriarUsuarioInput input =
        new CriarUsuarioInput(
            "Usuario Teste", "123.456.789-09", "USUARIO@EMAIL.COM", "11999999999", 1L, "123456");
    TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
    Usuario salvo =
        new Usuario(
            10L,
            "Usuario Teste",
            new Cpf("12345678909"),
            new Email("usuario@email.com"),
            "11999999999",
            tipo,
            true,
            List.of(),
            "123456");

    when(usuarioGateway.findByCpf("12345678909")).thenReturn(Optional.empty());
    when(usuarioGateway.findByEmail("usuario@email.com")).thenReturn(Optional.empty());
    when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));
    when(passwordHashGateway.hash("123456")).thenReturn("$2a$10$hash");
    when(usuarioGateway.saveUsuario(any(Usuario.class))).thenReturn(salvo);

    UsuarioOutput output = useCase.executar(input);

    assertThat(output.id()).isEqualTo(10L);
    assertThat(output.cpf()).isEqualTo("12345678909");
    assertThat(output.email()).isEqualTo("usuario@email.com");
  }

  @Test
  @DisplayName("Deve lancar excecao de validacao para campos obrigatorios")
  void deveLancarExcecaoParaCamposObrigatorios() {
    CriarUsuarioInput input = new CriarUsuarioInput(null, null, null, "11999999999", 1L, null);

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(ValidacaoException.class)
        .satisfies(
            ex ->
                assertThat(((ValidacaoException) ex).getErrosPorCampo())
                    .containsAllEntriesOf(
                        Map.of(
                            "cpf", "cpf é obrigatório",
                            "email", "email é obrigatório",
                            "nome", "nome é obrigatório",
                            "senha", "senha é obrigatória")));

    verify(usuarioGateway, never()).saveUsuario(any());
  }

  @Test
  @DisplayName("Deve lancar excecao quando cpf nao tiver 11 digitos")
  void deveLancarExcecaoQuandoCpfInvalido() {
    CriarUsuarioInput input =
        new CriarUsuarioInput(
            "Usuario Teste", "123", "usuario@email.com", "11999999999", 1L, "123456");

    assertThatThrownBy(() -> useCase.executar(input)).isInstanceOf(ValidacaoException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando cpf ja estiver cadastrado")
  void deveLancarExcecaoQuandoCpfDuplicado() {
    CriarUsuarioInput input =
        new CriarUsuarioInput(
            "Usuario Teste", "12345678909", "usuario@email.com", "11999999999", 1L, "123456");

    when(usuarioGateway.findByCpf("12345678909"))
        .thenReturn(Optional.of(UsuarioTestFactory.criarUsuarioValido()));

    assertThatThrownBy(() -> useCase.executar(input)).isInstanceOf(RegraDeNegocioException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando email ja estiver cadastrado")
  void deveLancarExcecaoQuandoEmailDuplicado() {
    CriarUsuarioInput input =
        new CriarUsuarioInput(
            "Usuario Teste", "12345678909", "usuario@email.com", "11999999999", 1L, "123456");

    when(usuarioGateway.findByCpf("12345678909")).thenReturn(Optional.empty());
    when(usuarioGateway.findByEmail("usuario@email.com"))
        .thenReturn(Optional.of(UsuarioTestFactory.criarUsuarioValido()));

    assertThatThrownBy(() -> useCase.executar(input)).isInstanceOf(RegraDeNegocioException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando tipo de usuario nao existir")
  void deveLancarExcecaoQuandoTipoNaoExistir() {
    CriarUsuarioInput input =
        new CriarUsuarioInput(
            "Usuario Teste", "12345678909", "usuario@email.com", "11999999999", 99L, "123456");

    when(usuarioGateway.findByCpf("12345678909")).thenReturn(Optional.empty());
    when(usuarioGateway.findByEmail("usuario@email.com")).thenReturn(Optional.empty());
    when(tipoUsuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(EntidadeNaoEncontradaException.class);
  }
}
