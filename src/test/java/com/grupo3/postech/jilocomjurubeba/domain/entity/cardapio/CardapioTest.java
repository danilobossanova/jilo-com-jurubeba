package com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio;

import static com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory.criarRestauranteValido;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

class CardapioTest {

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("Deve criar cardapio com dados validos")
        void deveCriarComDadosValidos() {

            Cardapio cardapio =
                    new Cardapio(
                            "Pizza",
                            "Pizza grande",
                            BigDecimal.valueOf(50),
                            false,
                            "/foto.png",
                            criarRestauranteValido());

            assertThat(cardapio.getId()).isNull();
            assertThat(cardapio.getNome()).isEqualTo("PIZZA");
            assertThat(cardapio.getDescricao()).isEqualTo("Pizza grande");
            assertThat(cardapio.getPreco()).isEqualTo(BigDecimal.valueOf(50));
            assertThat(cardapio.isApenasNoLocal()).isFalse();
            assertThat(cardapio.getCaminhoFoto()).isEqualTo("/foto.png");
            assertThat(cardapio.getRestaurante().getId()).isEqualTo(1L);
            assertThat(cardapio.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve converter nome para uppercase e trim")
        void deveConverterNomeParaUppercase() {

            Cardapio cardapio =
                    new Cardapio(
                            "  pizza  ",
                            "descricao",
                            BigDecimal.valueOf(20),
                            false,
                            null,
                            criarRestauranteValido());

            assertThat(cardapio.getNome()).isEqualTo("PIZZA");
        }

        @Test
        @DisplayName("Deve aplicar trim na descricao")
        void deveAplicarTrimNaDescricao() {

            Cardapio cardapio =
                    new Cardapio(
                            "Pizza",
                            "  descricao teste  ",
                            BigDecimal.valueOf(20),
                            false,
                            null,
                            criarRestauranteValido());

            assertThat(cardapio.getDescricao()).isEqualTo("descricao teste");
        }

        @Test
        @DisplayName("Deve criar cardapio com construtor completo")
        void deveCriarComConstrutorCompleto() {

            Cardapio cardapio =
                    new Cardapio(
                            1L,
                            "Pizza",
                            "Descricao",
                            BigDecimal.valueOf(40),
                            false,
                            "/foto.png",
                            criarRestauranteValido(),
                            true);

            assertThat(cardapio.getId()).isEqualTo(1L);
            assertThat(cardapio.getNome()).isEqualTo("PIZZA");
            assertThat(cardapio.getDescricao()).isEqualTo("Descricao");
            assertThat(cardapio.getPreco()).isEqualTo(BigDecimal.valueOf(40));
            assertThat(cardapio.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve lancar excecao quando nome for nulo")
        void deveLancarExcecaoQuandoNomeNulo() {

            assertThatThrownBy(
                            () ->
                                    new Cardapio(
                                            null,
                                            "desc",
                                            BigDecimal.valueOf(10),
                                            false,
                                            null,
                                            criarRestauranteValido()))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Nome");
        }

        @Test
        @DisplayName("Deve lancar excecao quando preco for invalido")
        void deveLancarExcecaoQuandoPrecoInvalido() {

            assertThatThrownBy(
                            () ->
                                    new Cardapio(
                                            "Pizza",
                                            "desc",
                                            BigDecimal.ZERO,
                                            false,
                                            null,
                                            criarRestauranteValido()))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Preco");
        }

        @Test
        @DisplayName("Deve lancar excecao quando restaurante for nulo")
        void deveLancarExcecaoQuandoRestauranteNulo() {

            assertThatThrownBy(
                            () ->
                                    new Cardapio(
                                            "Pizza",
                                            "desc",
                                            BigDecimal.valueOf(10),
                                            false,
                                            null,
                                            null))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("restaurante");
        }
    }

    @Nested
    @DisplayName("Comportamento")
    class Comportamento {

        @Test
        @DisplayName("Deve atualizar dados com validacao")
        void deveAtualizarDados() {

            Cardapio cardapio =
                    new Cardapio(
                            "Pizza",
                            "desc",
                            BigDecimal.valueOf(40),
                            false,
                            null,
                            criarRestauranteValido());

            cardapio.atualizarDados(
                    "Hamburguer", "novo", BigDecimal.valueOf(35), true, "/foto.png");

            assertThat(cardapio.getNome()).isEqualTo("HAMBURGUER");
            assertThat(cardapio.getDescricao()).isEqualTo("novo");
            assertThat(cardapio.getPreco()).isEqualTo(BigDecimal.valueOf(35));
            assertThat(cardapio.isApenasNoLocal()).isTrue();
        }

        @Test
        @DisplayName("Deve desativar cardapio")
        void deveDesativar() {

            Cardapio cardapio =
                    new Cardapio(
                            "Pizza",
                            "desc",
                            BigDecimal.valueOf(40),
                            false,
                            null,
                            criarRestauranteValido());

            cardapio.desativar();

            assertThat(cardapio.isAtivo()).isFalse();
        }

        @Test
        @DisplayName("Deve reativar cardapio")
        void deveReativar() {

            Cardapio cardapio =
                    new Cardapio(
                            "Pizza",
                            "desc",
                            BigDecimal.valueOf(40),
                            false,
                            null,
                            criarRestauranteValido());

            cardapio.desativar();
            cardapio.ativar();

            assertThat(cardapio.isAtivo()).isTrue();
        }
    }

    @Nested
    @DisplayName("Igualdade")
    class Igualdade {

        @Test
        @DisplayName("Deve ser igual quando ids sao iguais")
        void deveSerIgualQuandoIdsSaoIguais() {

            Cardapio c1 =
                    new Cardapio(
                            1L,
                            "Pizza",
                            "desc",
                            BigDecimal.valueOf(40),
                            false,
                            null,
                            criarRestauranteValido(),
                            true);

            Cardapio c2 =
                    new Cardapio(
                            1L,
                            "Hamburguer",
                            "desc",
                            BigDecimal.valueOf(20),
                            false,
                            null,
                            criarRestauranteValido(),
                            true);

            assertThat(c1).isEqualTo(c2);
            assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando ids sao diferentes")
        void deveSerDiferenteQuandoIdsSaoDiferentes() {

            Cardapio c1 =
                    new Cardapio(
                            1L,
                            "Pizza",
                            "desc",
                            BigDecimal.valueOf(40),
                            false,
                            null,
                            criarRestauranteValido(),
                            true);

            Cardapio c2 =
                    new Cardapio(
                            2L,
                            "Pizza",
                            "desc",
                            BigDecimal.valueOf(40),
                            false,
                            null,
                            criarRestauranteValido(),
                            true);

            assertThat(c1).isNotEqualTo(c2);
        }
    }
}
