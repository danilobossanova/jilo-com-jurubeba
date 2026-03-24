package com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

/**
 * Testes unitarios para a entidade de dominio TipoUsuario.
 *
 * <p>Testes PUROS — sem Spring, sem mocks de framework. Validam regras de negocio encapsuladas na
 * entidade.
 *
 * @author Danilo Fernando
 */
class TipoUsuarioTest {

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("Deve criar tipo de usuario com dados validos")
        void deveCriarComDadosValidos() {
            TipoUsuario tipo = new TipoUsuario("MASTER", "Administrador do sistema");

            assertThat(tipo.getId()).isNull();
            assertThat(tipo.getNome()).isEqualTo("MASTER");
            assertThat(tipo.getDescricao()).isEqualTo("Administrador do sistema");
            assertThat(tipo.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve converter nome para uppercase e trim")
        void deveConverterNomeParaUppercase() {
            TipoUsuario tipo = new TipoUsuario("  cliente  ", "Consumidor");

            assertThat(tipo.getNome()).isEqualTo("CLIENTE");
        }

        @Test
        @DisplayName("Deve aplicar trim na descricao")
        void deveAplicarTrimNaDescricao() {
            TipoUsuario tipo = new TipoUsuario("MASTER", "  Administrador  ");

            assertThat(tipo.getDescricao()).isEqualTo("Administrador");
        }

        @Test
        @DisplayName("Deve criar tipo de usuario com construtor completo")
        void deveCriarComConstrutorCompleto() {
            TipoUsuario tipo = new TipoUsuario(1L, "DONO_RESTAURANTE", "Proprietario", true);

            assertThat(tipo.getId()).isEqualTo(1L);
            assertThat(tipo.getNome()).isEqualTo("DONO_RESTAURANTE");
            assertThat(tipo.getDescricao()).isEqualTo("Proprietario");
            assertThat(tipo.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve lancar excecao quando nome for nulo")
        void deveLancarExcecaoQuandoNomeNulo() {
            assertThatThrownBy(() -> new TipoUsuario(null, "Descricao"))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Nome");
        }

        @Test
        @DisplayName("Deve lancar excecao quando nome for vazio")
        void deveLancarExcecaoQuandoNomeVazio() {
            assertThatThrownBy(() -> new TipoUsuario("  ", "Descricao"))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Nome");
        }

        @Test
        @DisplayName("Deve lancar excecao quando descricao for nula")
        void deveLancarExcecaoQuandoDescricaoNula() {
            assertThatThrownBy(() -> new TipoUsuario("MASTER", null))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Descricao");
        }

        @Test
        @DisplayName("Deve lancar excecao quando descricao for vazia")
        void deveLancarExcecaoQuandoDescricaoVazia() {
            assertThatThrownBy(() -> new TipoUsuario("MASTER", ""))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Descricao");
        }
    }

    @Nested
    @DisplayName("Comportamento")
    class Comportamento {

        @Test
        @DisplayName("Deve atualizar dados com validacao")
        void deveAtualizarDados() {
            TipoUsuario tipo = new TipoUsuario(1L, "MASTER", "Administrador", true);

            tipo.atualizarDados("SUPER_ADMIN", "Super administrador");

            assertThat(tipo.getNome()).isEqualTo("SUPER_ADMIN");
            assertThat(tipo.getDescricao()).isEqualTo("Super administrador");
        }

        @Test
        @DisplayName("Deve lancar excecao ao atualizar com nome nulo")
        void deveLancarExcecaoAoAtualizarComNomeNulo() {
            TipoUsuario tipo = new TipoUsuario(1L, "MASTER", "Administrador", true);

            assertThatThrownBy(() -> tipo.atualizarDados(null, "Nova descricao"))
                    .isInstanceOf(ValidacaoException.class);
        }

        @Test
        @DisplayName("Deve desativar tipo de usuario")
        void deveDesativar() {
            TipoUsuario tipo = new TipoUsuario(1L, "MASTER", "Administrador", true);

            tipo.desativar();

            assertThat(tipo.isAtivo()).isFalse();
        }

        @Test
        @DisplayName("Deve reativar tipo de usuario")
        void deveReativar() {
            TipoUsuario tipo = new TipoUsuario(1L, "MASTER", "Administrador", false);

            tipo.ativar();

            assertThat(tipo.isAtivo()).isTrue();
        }
    }

    @Nested
    @DisplayName("Igualdade")
    class Igualdade {

        @Test
        @DisplayName("Deve ser igual quando ids sao iguais")
        void deveSerIgualQuandoIdsSaoIguais() {
            TipoUsuario tipo1 = new TipoUsuario(1L, "MASTER", "Admin", true);
            TipoUsuario tipo2 = new TipoUsuario(1L, "CLIENTE", "Cliente", false);

            assertThat(tipo1).isEqualTo(tipo2);
            assertThat(tipo1.hashCode()).isEqualTo(tipo2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando ids sao diferentes")
        void deveSerDiferenteQuandoIdsSaoDiferentes() {
            TipoUsuario tipo1 = new TipoUsuario(1L, "MASTER", "Admin", true);
            TipoUsuario tipo2 = new TipoUsuario(2L, "MASTER", "Admin", true);

            assertThat(tipo1).isNotEqualTo(tipo2);
        }

        @Test
        @DisplayName("Deve retornar toString legivel")
        void deveRetornarToStringLegivel() {
            TipoUsuario tipo = new TipoUsuario(1L, "MASTER", "Admin", true);

            assertThat(tipo.toString()).contains("MASTER").contains("1");
        }
    }
}
