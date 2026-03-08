package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeletarUsuarioUseCaseTest {

  @Mock private UsuarioGateway usuarioGateway;

  private DeletarUsuarioUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new DeletarUsuarioUseCase(usuarioGateway);
  }

  @Test
  @DisplayName("Deve deletar usuario quando existir")
  void deveDeletarQuandoExistir() {
    when(usuarioGateway.findByIdUsuario(1L))
        .thenReturn(Optional.of(UsuarioTestFactory.criarUsuarioValido()));

    useCase.executar(1L);

    verify(usuarioGateway).deletarUsuario(1L);
  }

  @Test
  @DisplayName("Deve lancar excecao quando usuario nao existir")
  void deveLancarExcecaoQuandoNaoExistir() {
    when(usuarioGateway.findByIdUsuario(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(99L))
        .isInstanceOf(EntidadeNaoEncontradaException.class);
    verify(usuarioGateway, never()).deletarUsuario(99L);
  }
}
