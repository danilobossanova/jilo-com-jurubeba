package com.grupo3.postech.jilocomjurubeba.domain.entity.usuario;

import static com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory.criarDonoRestaurante;
import static com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory.criarUsuarioInativo;
import static com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory.criarUsuarioValido;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

class UsuarioTest {

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("Deve criar usuario valido")
        void deveCriarUsuarioValido() {

            Usuario usuario = criarUsuarioValido();

            assertThat(usuario.getId()).isEqualTo(1L);
            assertThat(usuario.getNome()).isEqualTo("USUARIO TESTE");
            assertThat(usuario.getCpf().getNumero()).isEqualTo("12345678909");
            assertThat(usuario.getEmail().getEndereco()).isEqualTo("usuario@email.com");
            assertThat(usuario.getTelefone()).isEqualTo("11999999999");
            assertThat(usuario.getTipoUsuario().getNome()).isEqualTo("CLIENTE");
            assertThat(usuario.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve criar usuario com construtor de criacao")
        void deveCriarComConstrutorDeCriacao() {

            TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Usuario cliente do sistema", true);

            Usuario usuario =
                    new Usuario(
                            "  joao  ",
                            "12345678909",
                            "joao@email.com",
                            "11999999999",
                            tipo,
                            "123456");

            assertThat(usuario.getNome()).isEqualTo("JOAO");
            assertThat(usuario.getId()).isNull();
            assertThat(usuario.isAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve lancar excecao quando nome for nulo")
        void deveLancarExcecaoQuandoNomeNulo() {

            TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Usuario cliente do sistema", true);

            assertThatThrownBy(
                            () ->
                                    new Usuario(
                                            null,
                                            "12345678909",
                                            "teste@email.com",
                                            "11999999999",
                                            tipo,
                                            "123456"))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Nome");
        }

        @Test
        @DisplayName("Deve lancar excecao quando senha for nula")
        void deveLancarExcecaoQuandoSenhaNula() {

            TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Usuario cliente do sistema", true);

            assertThatThrownBy(
                            () ->
                                    new Usuario(
                                            "Teste",
                                            "12345678909",
                                            "teste@email.com",
                                            "11999999999",
                                            tipo,
                                            null))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Senha");
        }

        @Test
        @DisplayName("Deve lancar excecao quando cpf for nulo")
        void deveLancarExcecaoQuandoCpfNulo() {

            TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Usuario cliente do sistema", true);

            assertThatThrownBy(
                            () ->
                                    new Usuario(
                                            "Teste",
                                            null,
                                            "teste@email.com",
                                            "11999999999",
                                            tipo,
                                            "123456"))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("CPF");
        }

        @Test
        @DisplayName("Deve lancar excecao quando email for nulo")
        void deveLancarExcecaoQuandoEmailNulo() {

            TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Usuario cliente do sistema", true);

            assertThatThrownBy(
                            () ->
                                    new Usuario(
                                            "Teste",
                                            "12345678909",
                                            null,
                                            "11999999999",
                                            tipo,
                                            "123456"))
                    .isInstanceOf(ValidacaoException.class)
                    .hasMessageContaining("Email");
        }
    }

    @Nested
    @DisplayName("Comportamento")
    class Comportamento {

        @Test
        @DisplayName("Deve atualizar dados")
        void deveAtualizarDados() {

            Usuario usuario = criarUsuarioValido();

            usuario.atualizarDados("Maria", "maria@email.com", "11888888888");

            assertThat(usuario.getNome()).isEqualTo("MARIA");
            assertThat(usuario.getEmail().getEndereco()).isEqualTo("maria@email.com");
            assertThat(usuario.getTelefone()).isEqualTo("11888888888");
        }

        @Test
        @DisplayName("Deve atualizar senha")
        void deveAtualizarSenha() {

            Usuario usuario = criarUsuarioValido();

            usuario.atualizarSenha("novaSenha123");

            assertThat(usuario.getSenha()).isEqualTo("novaSenha123");
        }

        @Test
        @DisplayName("Deve ativar usuario")
        void deveAtivarUsuario() {

            Usuario usuario = criarUsuarioInativo();

            usuario.ativar();

            assertThat(usuario.estaAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve desativar usuario")
        void deveDesativarUsuario() {

            Usuario usuario = criarUsuarioValido();

            usuario.desativar();

            assertThat(usuario.estaAtivo()).isFalse();
        }

        @Test
        @DisplayName("Deve identificar dono de restaurante")
        void deveIdentificarDonoRestaurante() {

            Usuario usuario = criarDonoRestaurante();

            assertThat(usuario.eDonoDeRestaurante()).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false para eDonoDeRestaurante quando for cliente")
        void deveRetornarFalseQuandoCliente() {

            Usuario usuario = criarUsuarioValido();

            assertThat(usuario.eDonoDeRestaurante()).isFalse();
        }
    }

    @Nested
    @DisplayName("Igualdade")
    class Igualdade {

        @Test
        @DisplayName("Deve ser igual quando ids iguais")
        void deveSerIgualQuandoIdsIguais() {

            Usuario u1 = criarUsuarioValido();
            Usuario u2 = criarUsuarioValido();

            assertThat(u1).isEqualTo(u2);
            assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando ids diferentes")
        void deveSerDiferenteQuandoIdsDiferentes() {

            Usuario u1 = criarUsuarioValido();
            Usuario u2 = criarUsuarioInativo();

            assertThat(u1).isNotEqualTo(u2);
        }
    }
}
