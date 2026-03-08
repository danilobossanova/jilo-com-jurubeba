package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CriarRestauranteUseCaseTest {

  @Mock private RestauranteGateway restauranteGateway;
  @Mock private UsuarioGateway usuarioGateway;

  private CriarRestauranteUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new CriarRestauranteUseCase(restauranteGateway, usuarioGateway);
  }

  @Test
  @DisplayName("Deve criar restaurante com sucesso")
  void deveCriarComSucesso() {
    Usuario dono = UsuarioTestFactory.criarDonoRestaurante();
    CriarRestauranteInput input =
        new CriarRestauranteInput(
            "Novo restaurante",
            "Rua Nova, 999",
            TypeCozinha.BRASILEIRA,
            LocalTime.of(10, 0),
            LocalTime.of(22, 0),
            2L);
    Restaurante salvo =
        new Restaurante(
            10L,
            "Novo restaurante",
            "Rua Nova, 999",
            TypeCozinha.BRASILEIRA,
            LocalTime.of(10, 0),
            LocalTime.of(22, 0),
            dono,
            true);

    when(restauranteGateway.findByNome("Novo restaurante")).thenReturn(Optional.empty());
    when(usuarioGateway.findByIdUsuario(2L)).thenReturn(Optional.of(dono));
    when(restauranteGateway.saveRestaurante(any(Restaurante.class))).thenReturn(salvo);

    RestauranteOutput output = useCase.executar(input);

    assertThat(output.id()).isEqualTo(10L);
    assertThat(output.nome()).isEqualTo("NOVO RESTAURANTE");
    assertThat(output.donoId()).isEqualTo(2L);
  }

  @Test
  @DisplayName("Deve lancar excecao quando restaurante ja existir")
  void deveLancarExcecaoQuandoJaExistir() {
    CriarRestauranteInput input =
        new CriarRestauranteInput(
            "Restaurante Teste",
            "Rua A",
            TypeCozinha.BRASILEIRA,
            LocalTime.of(9, 0),
            LocalTime.of(20, 0),
            1L);

    when(restauranteGateway.findByNome("Restaurante Teste"))
        .thenReturn(Optional.of(RestauranteTestFactory.criarRestauranteValido()));

    assertThatThrownBy(() -> useCase.executar(input)).isInstanceOf(RegraDeNegocioException.class);
    verify(restauranteGateway, never()).saveRestaurante(any());
  }

  @Test
  @DisplayName("Deve lancar excecao quando dono nao existir")
  void deveLancarExcecaoQuandoDonoNaoExistir() {
    CriarRestauranteInput input =
        new CriarRestauranteInput(
            "Novo restaurante",
            "Rua Nova, 999",
            TypeCozinha.BRASILEIRA,
            LocalTime.of(10, 0),
            LocalTime.of(22, 0),
            99L);

    when(restauranteGateway.findByNome("Novo restaurante")).thenReturn(Optional.empty());
    when(usuarioGateway.findByIdUsuario(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.executar(input))
        .isInstanceOf(EntidadeNaoEncontradaException.class);
  }
}
