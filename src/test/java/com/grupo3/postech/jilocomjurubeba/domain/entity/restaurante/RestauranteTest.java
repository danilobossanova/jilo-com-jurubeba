package com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RestauranteTest {

  @Nested
  @DisplayName("Criacao")
  class Criacao {

    @Test
    @DisplayName("Deve criar restaurante com dados validos")
    void deveCriarRestauranteValido() {

      Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();
      Restaurante.RestauranteSnapshot snap = restaurante.snapshot();

      assertThat(snap.id()).isEqualTo(1L);
      assertThat(snap.nome()).isEqualTo("RESTAURANTE TESTE");
      assertThat(snap.endereco()).isEqualTo("Rua A, 123");
      assertThat(snap.typeCozinha()).isEqualTo(TypeCozinha.BRASILEIRA);
      assertThat(snap.ativo()).isTrue();
    }

    @Test
    @DisplayName("Deve converter nome para uppercase")
    void deveConverterNomeParaUppercase() {

      Restaurante restaurante =
          new Restaurante(
              1L,
              "restaurante teste",
              "Rua Teste",
              TypeCozinha.BRASILEIRA,
              LocalTime.of(10, 0),
              LocalTime.of(22, 0),
              UsuarioTestFactory.criarUsuarioValido(),
              true);

      assertThat(restaurante.snapshot().nome()).isEqualTo("RESTAURANTE TESTE");
    }

    @Test
    @DisplayName("Deve aplicar trim no endereco")
    void deveAplicarTrimNoEndereco() {

      Restaurante restaurante =
          new Restaurante(
              1L,
              "Restaurante",
              "  Rua Teste  ",
              TypeCozinha.BRASILEIRA,
              LocalTime.of(10, 0),
              LocalTime.of(22, 0),
              UsuarioTestFactory.criarUsuarioValido(),
              true);

      assertThat(restaurante.snapshot().endereco()).isEqualTo("Rua Teste");
    }

    @Test
    @DisplayName("Deve lancar excecao quando nome for nulo")
    void deveLancarExcecaoQuandoNomeNulo() {

      assertThatThrownBy(
              () ->
                  new Restaurante(
                      1L,
                      null,
                      "Rua",
                      TypeCozinha.BRASILEIRA,
                      LocalTime.of(10, 0),
                      LocalTime.of(22, 0),
                      UsuarioTestFactory.criarUsuarioValido(),
                      true))
          .isInstanceOf(RegraDeNegocioException.class)
          .hasMessageContaining("Nome");
    }

    @Test
    @DisplayName("Deve lancar excecao quando endereco for nulo")
    void deveLancarExcecaoQuandoEnderecoNulo() {

      assertThatThrownBy(
              () ->
                  new Restaurante(
                      1L,
                      "Restaurante",
                      null,
                      TypeCozinha.BRASILEIRA,
                      LocalTime.of(10, 0),
                      LocalTime.of(22, 0),
                      UsuarioTestFactory.criarUsuarioValido(),
                      true))
          .isInstanceOf(RegraDeNegocioException.class)
          .hasMessageContaining("Endereço");
    }

    @Test
    @DisplayName("Deve lancar excecao quando tipo cozinha for nulo")
    void deveLancarExcecaoQuandoTipoCozinhaNulo() {

      assertThatThrownBy(
              () ->
                  new Restaurante(
                      1L,
                      "Restaurante",
                      "Rua",
                      null,
                      LocalTime.of(10, 0),
                      LocalTime.of(22, 0),
                      UsuarioTestFactory.criarUsuarioValido(),
                      true))
          .isInstanceOf(RegraDeNegocioException.class)
          .hasMessageContaining("cozinha");
    }
  }

  @Nested
  @DisplayName("Comportamento")
  class Comportamento {

    @Test
    @DisplayName("Deve desativar restaurante")
    void deveDesativarRestaurante() {

      Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

      restaurante.desativar();

      assertThat(restaurante.snapshot().ativo()).isFalse();
    }

    @Test
    @DisplayName("Deve ativar restaurante")
    void deveAtivarRestaurante() {

      Restaurante restaurante = RestauranteTestFactory.criarRestauranteInativo();

      restaurante.ativar();

      assertThat(restaurante.snapshot().ativo()).isTrue();
    }

    @Test
    @DisplayName("Deve alterar endereco")
    void deveAlterarEndereco() {

      Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

      restaurante.alterarEndereco("Nova Rua 999");

      assertThat(restaurante.snapshot().endereco()).isEqualTo("Nova Rua 999");
    }

    @Test
    @DisplayName("Deve alterar horario")
    void deveAlterarHorario() {

      Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

      restaurante.alterarHorario(LocalTime.of(9, 0), LocalTime.of(23, 0));

      Restaurante.RestauranteSnapshot snap = restaurante.snapshot();

      assertThat(snap.horaAbertura()).isEqualTo(LocalTime.of(9, 0));
      assertThat(snap.horaFechamento()).isEqualTo(LocalTime.of(23, 0));
    }

    @Test
    @DisplayName("Deve retornar aberto quando dentro do horario")
    void deveRetornarAbertoQuandoDentroDoHorario() {

      Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

      boolean aberto = restaurante.estaAberto(LocalTime.of(12, 0));

      assertThat(aberto).isTrue();
    }

    @Test
    @DisplayName("Deve retornar fechado quando fora do horario")
    void deveRetornarFechadoQuandoForaDoHorario() {

      Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

      boolean aberto = restaurante.estaAberto(LocalTime.of(3, 0));

      assertThat(aberto).isFalse();
    }
  }

  @Nested
  @DisplayName("Igualdade")
  class Igualdade {

    @Test
    @DisplayName("Deve ser igual quando ids forem iguais")
    void deveSerIgualQuandoIdsForemIguais() {

      Restaurante r1 = RestauranteTestFactory.criarRestauranteValido();
      Restaurante r2 =
          new Restaurante(
              1L,
              "Outro",
              "Rua",
              TypeCozinha.BRASILEIRA,
              LocalTime.of(10, 0),
              LocalTime.of(22, 0),
              UsuarioTestFactory.criarUsuarioValido(),
              true);

      assertThat(r1).isEqualTo(r2);
      assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    @DisplayName("Deve ser diferente quando ids forem diferentes")
    void deveSerDiferenteQuandoIdsForemDiferentes() {

      Restaurante r1 = RestauranteTestFactory.criarRestauranteValido();
      Restaurante r2 = RestauranteTestFactory.criarRestauranteInativo();

      assertThat(r1).isNotEqualTo(r2);
    }
  }
}
