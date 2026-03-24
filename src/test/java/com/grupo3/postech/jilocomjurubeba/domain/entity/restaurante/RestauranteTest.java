package com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.TipoCozinha;
import com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory;
import com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory;

class RestauranteTest {

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("Deve criar restaurante com dados validos")
        void deveCriarRestauranteValido() {

            Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

            assertThat(restaurante.getId()).isEqualTo(1L);
            assertThat(restaurante.getNome()).isEqualTo("RESTAURANTE TESTE");
            assertThat(restaurante.getEndereco()).isEqualTo("Rua A, 123");
            assertThat(restaurante.getTipoCozinha()).isEqualTo(TipoCozinha.BRASILEIRA);
            assertThat(restaurante.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve converter nome para uppercase")
        void deveConverterNomeParaUppercase() {

            Restaurante restaurante =
                    new Restaurante(
                            1L,
                            "restaurante teste",
                            "Rua Teste",
                            TipoCozinha.BRASILEIRA,
                            LocalTime.of(10, 0),
                            LocalTime.of(22, 0),
                            UsuarioTestFactory.criarUsuarioValido(),
                            true);

            assertThat(restaurante.getNome()).isEqualTo("RESTAURANTE TESTE");
        }

        @Test
        @DisplayName("Deve aplicar trim no endereco")
        void deveAplicarTrimNoEndereco() {

            Restaurante restaurante =
                    new Restaurante(
                            1L,
                            "Restaurante",
                            "  Rua Teste  ",
                            TipoCozinha.BRASILEIRA,
                            LocalTime.of(10, 0),
                            LocalTime.of(22, 0),
                            UsuarioTestFactory.criarUsuarioValido(),
                            true);

            assertThat(restaurante.getEndereco()).isEqualTo("Rua Teste");
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
                                            TipoCozinha.BRASILEIRA,
                                            LocalTime.of(10, 0),
                                            LocalTime.of(22, 0),
                                            UsuarioTestFactory.criarUsuarioValido(),
                                            true))
                    .isInstanceOf(ValidacaoException.class)
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
                                            TipoCozinha.BRASILEIRA,
                                            LocalTime.of(10, 0),
                                            LocalTime.of(22, 0),
                                            UsuarioTestFactory.criarUsuarioValido(),
                                            true))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Endereco");
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
                    .isInstanceOf(ValidacaoException.class)
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

            assertThat(restaurante.isAtivo()).isFalse();
        }

        @Test
        @DisplayName("Deve ativar restaurante")
        void deveAtivarRestaurante() {

            Restaurante restaurante = RestauranteTestFactory.criarRestauranteInativo();

            restaurante.ativar();

            assertThat(restaurante.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve atualizar dados do restaurante")
        void deveAtualizarDados() {

            Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

            restaurante.atualizarDados(
                    "Novo Nome",
                    "Nova Rua 999",
                    TipoCozinha.JAPONESA,
                    LocalTime.of(9, 0),
                    LocalTime.of(23, 0));

            assertThat(restaurante.getNome()).isEqualTo("NOVO NOME");
            assertThat(restaurante.getEndereco()).isEqualTo("Nova Rua 999");
            assertThat(restaurante.getTipoCozinha()).isEqualTo(TipoCozinha.JAPONESA);
            assertThat(restaurante.getHoraAbertura()).isEqualTo(LocalTime.of(9, 0));
            assertThat(restaurante.getHoraFechamento()).isEqualTo(LocalTime.of(23, 0));
        }

        @Test
        @DisplayName("Deve verificar pertencimento ao dono")
        void deveVerificarPertencimentoAoDono() {

            Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

            assertThat(restaurante.pertenceAoDono(1L)).isTrue();
            assertThat(restaurante.pertenceAoDono(99L)).isFalse();
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
                            TipoCozinha.BRASILEIRA,
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
