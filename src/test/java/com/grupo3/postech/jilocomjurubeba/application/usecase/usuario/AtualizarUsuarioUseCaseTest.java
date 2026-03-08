package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

  @Mock private UsuarioGateway usuarioGateway;
  @Mock private TipoUsuarioGateway tipoUsuarioGateway;

  private AtualizarUsuarioUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new AtualizarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway);
  }

  @Test
  @DisplayName("Deve atualizar usuario com sucesso")
  void deveAtualizarComSucesso() {
    TipoUsuario tipoAtual = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
    TipoUsuario tipoNovo = new TipoUsuario(2L, "DONO_RESTAURANTE", "Dono", true);
    Usuario atual = novoUsuario(1L, "12345678909", "usuario@email.com", tipoAtual);
    AtualizarUsuarioInput input =
        new AtualizarUsuarioInput(
            null, "Usuario Atualizado", "111.222.333-44", "novo@email.com", "11988887777", 2L);

    when(usuarioGateway.findByIdUsuario(1L)).thenReturn(Optional.of(atual));
    when(usuarioGateway.findByCpf("11122233344")).thenReturn(Optional.empty());
    when(usuarioGateway.findByEmail("novo@email.com")).thenReturn(Optional.empty());
    when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(tipoAtual));
    when(tipoUsuarioGateway.buscarPorId(2L)).thenReturn(Optional.of(tipoNovo));
    when(usuarioGateway.saveUsuario(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

    UsuarioOutput output = useCase.executar(new AtualizarUsuarioUseCase.Input(1L, input));

    assertThat(output.nome()).isEqualTo("USUARIO ATUALIZADO");
    assertThat(output.cpf()).isEqualTo("11122233344");
    assertThat(output.email()).isEqualTo("novo@email.com");
    assertThat(output.tipoUsuarioId()).isEqualTo(2L);
  }

  @Test
  @DisplayName("Deve lancar excecao quando id for nulo")
  void deveLancarExcecaoQuandoIdNulo() {
    AtualizarUsuarioInput input = new AtualizarUsuarioInput(null, "Nome", null, null, null, null);

    assertThatThrownBy(() -> useCase.executar(new AtualizarUsuarioUseCase.Input(null, input)))
        .isInstanceOf(RegraDeNegocioException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando input for nulo")
  void deveLancarExcecaoQuandoInputNulo() {
    assertThatThrownBy(() -> useCase.executar(new AtualizarUsuarioUseCase.Input(1L, null)))
        .isInstanceOf(RegraDeNegocioException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando usuario nao existir")
  void deveLancarExcecaoQuandoUsuarioNaoExistir() {
    AtualizarUsuarioInput input = new AtualizarUsuarioInput(null, "Nome", null, null, null, null);
    when(usuarioGateway.findByIdUsuario(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(new AtualizarUsuarioUseCase.Input(99L, input)))
        .isInstanceOf(EntidadeNaoEncontradaException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando cpf estiver duplicado")
  void deveLancarExcecaoQuandoCpfDuplicado() {
    TipoUsuario tipoAtual = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
    Usuario atual = novoUsuario(1L, "12345678909", "usuario@email.com", tipoAtual);
    Usuario outro = novoUsuario(2L, "11122233344", "outro@email.com", tipoAtual);
    AtualizarUsuarioInput input =
        new AtualizarUsuarioInput(null, null, "11122233344", null, null, null);

    when(usuarioGateway.findByIdUsuario(1L)).thenReturn(Optional.of(atual));
    when(usuarioGateway.findByCpf("11122233344")).thenReturn(Optional.of(outro));

    assertThatThrownBy(() -> useCase.executar(new AtualizarUsuarioUseCase.Input(1L, input)))
        .isInstanceOf(RegraDeNegocioException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando email estiver duplicado")
  void deveLancarExcecaoQuandoEmailDuplicado() {
    TipoUsuario tipoAtual = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
    Usuario atual = novoUsuario(1L, "12345678909", "usuario@email.com", tipoAtual);
    Usuario outro = novoUsuario(2L, "11122233344", "novo@email.com", tipoAtual);
    AtualizarUsuarioInput input =
        new AtualizarUsuarioInput(null, null, null, "novo@email.com", null, null);

    when(usuarioGateway.findByIdUsuario(1L)).thenReturn(Optional.of(atual));
    when(usuarioGateway.findByEmail("novo@email.com")).thenReturn(Optional.of(outro));

    assertThatThrownBy(() -> useCase.executar(new AtualizarUsuarioUseCase.Input(1L, input)))
        .isInstanceOf(RegraDeNegocioException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando tipo atual nao existir")
  void deveLancarExcecaoQuandoTipoAtualNaoExistir() {
    TipoUsuario tipoAtual = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
    Usuario atual = novoUsuario(1L, "12345678909", "usuario@email.com", tipoAtual);
    AtualizarUsuarioInput input = new AtualizarUsuarioInput(null, "Nome", null, null, null, null);

    when(usuarioGateway.findByIdUsuario(1L)).thenReturn(Optional.of(atual));
    when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(new AtualizarUsuarioUseCase.Input(1L, input)))
        .isInstanceOf(RegraDeNegocioException.class);
  }

  @Test
  @DisplayName("Deve lancar excecao quando novo tipo nao existir")
  void deveLancarExcecaoQuandoNovoTipoNaoExistir() {
    TipoUsuario tipoAtual = new TipoUsuario(1L, "CLIENTE", "Cliente", true);
    Usuario atual = novoUsuario(1L, "12345678909", "usuario@email.com", tipoAtual);
    AtualizarUsuarioInput input = new AtualizarUsuarioInput(null, "Nome", null, null, null, 99L);

    when(usuarioGateway.findByIdUsuario(1L)).thenReturn(Optional.of(atual));
    when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(tipoAtual));
    when(tipoUsuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(new AtualizarUsuarioUseCase.Input(1L, input)))
        .isInstanceOf(RegraDeNegocioException.class);
  }

  private Usuario novoUsuario(Long id, String cpf, String email, TipoUsuario tipo) {
    return new Usuario(
        id,
        "Usuario Teste",
        new Cpf(cpf),
        new Email(email),
        "11999999999",
        tipo,
        true,
        List.of(),
        "123456");
  }
}
