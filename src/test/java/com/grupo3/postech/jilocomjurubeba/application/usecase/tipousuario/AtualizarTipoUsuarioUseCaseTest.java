package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.AtualizarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AtualizarTipoUsuarioUseCaseTest {

  @Mock private TipoUsuarioGateway gateway;

  private AtualizarTipoUsuarioUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new AtualizarTipoUsuarioUseCase(gateway);
  }

  @Test
  @DisplayName("Deve atualizar tipo de usuario com sucesso")
  void deveAtualizarComSucesso() {
    AtualizarTipoUsuarioInput input =
        new AtualizarTipoUsuarioInput(1L, "SUPER_ADMIN", "Super administrador");
    TipoUsuario existente = new TipoUsuario(1L, "MASTER", "Administrador", true);
    TipoUsuario atualizado = new TipoUsuario(1L, "SUPER_ADMIN", "Super administrador", true);

    when(gateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
    when(gateway.existePorNomeEIdDiferente("SUPER_ADMIN", 1L)).thenReturn(false);
    when(gateway.salvar(any(TipoUsuario.class))).thenReturn(atualizado);

    TipoUsuarioOutput output = useCase.executar(input);

    assertThat(output.nome()).isEqualTo("SUPER_ADMIN");
    assertThat(output.descricao()).isEqualTo("Super administrador");
  }

  @Test
  @DisplayName("Deve lancar excecao quando tipo nao encontrado para atualizacao")
  void deveLancarExcecaoQuandoNaoEncontrado() {
    AtualizarTipoUsuarioInput input = new AtualizarTipoUsuarioInput(99L, "NOVO", "Novo tipo");

    when(gateway.buscarPorId(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(EntidadeNaoEncontradaException.class);

    verify(gateway, never()).salvar(any());
  }

  @Test
  @DisplayName("Deve lancar excecao quando nome duplicado na atualizacao")
  void deveLancarExcecaoQuandoNomeDuplicado() {
    AtualizarTipoUsuarioInput input = new AtualizarTipoUsuarioInput(1L, "CLIENTE", "Consumidor");
    TipoUsuario existente = new TipoUsuario(1L, "MASTER", "Administrador", true);

    when(gateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
    when(gateway.existePorNomeEIdDiferente("CLIENTE", 1L)).thenReturn(true);

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(RegraDeNegocioException.class)
        .hasMessageContaining("CLIENTE");

    verify(gateway, never()).salvar(any());
  }
}
