package com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio;

import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.grupo3.postech.jilocomjurubeba.factory.RestauranteTestFactory.criarRestauranteValido;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardapioTest {

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("Deve criar cardapio com dados validos")
        void deveCriarComDadosValidos() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "Pizza grande",
                BigDecimal.valueOf(50),
                false,
                "/foto.png",
                criarRestauranteValido()
            );

            Cardapio.CardapioSnapshot snap = cardapio.snapshot();

            assertThat(snap.id()).isNull();
            assertThat(snap.nome()).isEqualTo("PIZZA");
            assertThat(snap.descricao()).isEqualTo("Pizza grande");
            assertThat(snap.preco()).isEqualTo(BigDecimal.valueOf(50));
            assertThat(snap.apenasNoLocal()).isFalse();
            assertThat(snap.caminhoFoto()).isEqualTo("/foto.png");
            assertThat(snap.restauranteId()).isEqualTo(1L);
            assertThat(snap.ativo()).isTrue();
        }

        @Test
        @DisplayName("Deve converter nome para uppercase e trim")
        void deveConverterNomeParaUppercase() {

            Cardapio cardapio = new Cardapio(
                "  pizza  ",
                "descricao",
                BigDecimal.valueOf(20),
                false,
                null,
                criarRestauranteValido()
            );

            assertThat(cardapio.snapshot().nome()).isEqualTo("PIZZA");
        }

        @Test
        @DisplayName("Deve aplicar trim na descricao")
        void deveAplicarTrimNaDescricao() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "  descricao teste  ",
                BigDecimal.valueOf(20),
                false,
                null,
                criarRestauranteValido()
            );

            assertThat(cardapio.snapshot().descricao()).isEqualTo("descricao teste");
        }

        @Test
        @DisplayName("Deve criar cardapio com construtor completo")
        void deveCriarComConstrutorCompleto() {

            Cardapio cardapio = new Cardapio(
                1L,
                "Pizza",
                "Descricao",
                BigDecimal.valueOf(40),
                false,
                "/foto.png",
                criarRestauranteValido(),
                true
            );

            Cardapio.CardapioSnapshot snap = cardapio.snapshot();

            assertThat(snap.id()).isEqualTo(1L);
            assertThat(snap.nome()).isEqualTo("PIZZA");
            assertThat(snap.descricao()).isEqualTo("Descricao");
            assertThat(snap.preco()).isEqualTo(BigDecimal.valueOf(40));
            assertThat(snap.ativo()).isTrue();
        }

        @Test
        @DisplayName("Deve lancar excecao quando nome for nulo")
        void deveLancarExcecaoQuandoNomeNulo() {

            assertThatThrownBy(() ->
                new Cardapio(
                    null,
                    "desc",
                    BigDecimal.valueOf(10),
                    false,
                    null,
                    criarRestauranteValido()
                )
            )
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Nome");
        }

        @Test
        @DisplayName("Deve lancar excecao quando preco for invalido")
        void deveLancarExcecaoQuandoPrecoInvalido() {

            assertThatThrownBy(() ->
                new Cardapio(
                    "Pizza",
                    "desc",
                    BigDecimal.ZERO,
                    false,
                    null,
                    criarRestauranteValido()
                )
            )
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Preço");
        }

        @Test
        @DisplayName("Deve lancar excecao quando restaurante for nulo")
        void deveLancarExcecaoQuandoRestauranteNulo() {

            assertThatThrownBy(() ->
                new Cardapio(
                    "Pizza",
                    "desc",
                    BigDecimal.valueOf(10),
                    false,
                    null,
                    null
                )
            )
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("restaurante");
        }
    }

    @Nested
    @DisplayName("Comportamento")
    class Comportamento {

        @Test
        @DisplayName("Deve atualizar dados com validacao")
        void deveAtualizarDados() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido()
            );

            cardapio.atualizarCadastro(
                "Hamburguer",
                "novo",
                BigDecimal.valueOf(35),
                true,
                "/foto.png",
                criarRestauranteValido()
            );

            Cardapio.CardapioSnapshot snap = cardapio.snapshot();

            assertThat(snap.nome()).isEqualTo("HAMBURGUER");
            assertThat(snap.descricao()).isEqualTo("novo");
            assertThat(snap.preco()).isEqualTo(BigDecimal.valueOf(35));
            assertThat(snap.apenasNoLocal()).isTrue();
        }

        @Test
        @DisplayName("Deve alterar preco")
        void deveAlterarPreco() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido()
            );

            cardapio.alterarPreco(BigDecimal.valueOf(60));

            assertThat(cardapio.snapshot().preco()).isEqualTo(BigDecimal.valueOf(60));
        }

        @Test
        @DisplayName("Deve alterar disponibilidade somente local")
        void deveAlterarDisponibilidade() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido()
            );

            cardapio.alterarDisponibilidadeSomenteLocal(true);

            assertThat(cardapio.snapshot().apenasNoLocal()).isTrue();
        }

        @Test
        @DisplayName("Deve alterar foto")
        void deveAlterarFoto() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido()
            );

            cardapio.alterarFoto(" /nova.png ");

            assertThat(cardapio.snapshot().caminhoFoto()).isEqualTo("/nova.png");
        }

        @Test
        @DisplayName("Deve desativar cardapio")
        void deveDesativar() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido()
            );

            cardapio.desativar();

            assertThat(cardapio.snapshot().ativo()).isFalse();
        }

        @Test
        @DisplayName("Deve reativar cardapio")
        void deveReativar() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido()
            );

            cardapio.desativar();
            cardapio.ativar();

            assertThat(cardapio.snapshot().ativo()).isTrue();
        }

        @Test
        @DisplayName("Deve verificar se pertence ao restaurante")
        void deveVerificarPertencimentoRestaurante() {

            Cardapio cardapio = new Cardapio(
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido()
            );

            assertThat(cardapio.pertenceAoRestaurante(1L)).isTrue();
            assertThat(cardapio.pertenceAoRestaurante(2L)).isFalse();
        }
    }

    @Nested
    @DisplayName("Igualdade")
    class Igualdade {

        @Test
        @DisplayName("Deve ser igual quando ids sao iguais")
        void deveSerIgualQuandoIdsSaoIguais() {

            Cardapio c1 = new Cardapio(
                1L,
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido(),
                true
            );

            Cardapio c2 = new Cardapio(
                1L,
                "Hamburguer",
                "desc",
                BigDecimal.valueOf(20),
                false,
                null,
                criarRestauranteValido(),
                true
            );

            assertThat(c1).isEqualTo(c2);
            assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando ids sao diferentes")
        void deveSerDiferenteQuandoIdsSaoDiferentes() {

            Cardapio c1 = new Cardapio(
                1L,
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido(),
                true
            );

            Cardapio c2 = new Cardapio(
                2L,
                "Pizza",
                "desc",
                BigDecimal.valueOf(40),
                false,
                null,
                criarRestauranteValido(),
                true
            );

            assertThat(c1).isNotEqualTo(c2);
        }
    }
}
