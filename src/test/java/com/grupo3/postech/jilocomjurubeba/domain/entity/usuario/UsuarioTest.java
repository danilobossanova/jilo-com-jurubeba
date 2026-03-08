package com.grupo3.postech.jilocomjurubeba.domain.entity.usuario;

import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory.criarUsuarioValido;
import static com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory.criarUsuarioInativo;
import static com.grupo3.postech.jilocomjurubeba.factory.UsuarioTestFactory.criarDonoRestaurante;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioTest {

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("Deve criar usuario valido")
        void deveCriarUsuarioValido() {

            Usuario usuario = criarUsuarioValido();

            Usuario.UsuarioSnapshot snap = usuario.snapshot();

            assertThat(snap.id()).isEqualTo(1L);
            assertThat(snap.nome()).isEqualTo("USUARIO TESTE");
            assertThat(snap.cpf()).isEqualTo("12345678909");
            assertThat(snap.email()).isEqualTo("usuario@email.com");
            assertThat(snap.telefone()).isEqualTo("11999999999");
            assertThat(snap.tipoUsuarioNome()).isEqualTo("CLIENTE");
            assertThat(snap.ativo()).isTrue();
        }

        @Test
        @DisplayName("Deve normalizar nome")
        void deveNormalizarNome() {

            Usuario usuario = new Usuario(
                "  joao  ",
                new Cpf("12345678909"),
                new Email("joao@email.com"),
                "11999999999",
                criarUsuarioValido().getTipoUsuario(),
                null,
                "123456"
            );

            assertThat(usuario.getNome()).isEqualTo("JOAO");
        }

        @Test
        @DisplayName("Deve lancar excecao quando nome for nulo")
        void deveLancarExcecaoQuandoNomeNulo() {

            assertThatThrownBy(() ->
                new Usuario(
                    null,
                    new Cpf("12345678909"),
                    new Email("teste@email.com"),
                    "11999999999",
                    criarUsuarioValido().getTipoUsuario(),
                    null,
                    "123456"
                )
            )
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Nome");
        }

        @Test
        @DisplayName("Deve lancar excecao quando senha for nula")
        void deveLancarExcecaoQuandoSenhaNula() {

            assertThatThrownBy(() ->
                new Usuario(
                    "Teste",
                    new Cpf("12345678909"),
                    new Email("teste@email.com"),
                    "11999999999",
                    criarUsuarioValido().getTipoUsuario(),
                    null,
                    null
                )
            )
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Senha");
        }

        @Test
        @DisplayName("Deve lancar excecao quando cpf for nulo")
        void deveLancarExcecaoQuandoCpfNulo() {

            assertThatThrownBy(() ->
                new Usuario(
                    "Teste",
                    null,
                    new Email("teste@email.com"),
                    "11999999999",
                    criarUsuarioValido().getTipoUsuario(),
                    null,
                    "123456"
                )
            )
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("CPF");
        }

        @Test
        @DisplayName("Deve lancar excecao quando email for nulo")
        void deveLancarExcecaoQuandoEmailNulo() {

            assertThatThrownBy(() ->
                new Usuario(
                    "Teste",
                    new Cpf("12345678909"),
                    null,
                    "11999999999",
                    criarUsuarioValido().getTipoUsuario(),
                    null,
                    "123456"
                )
            )
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Email");
        }
    }

    @Nested
    @DisplayName("Comportamento")
    class Comportamento {

        @Test
        @DisplayName("Deve atualizar cadastro")
        void deveAtualizarCadastro() {

            Usuario usuario = criarUsuarioValido();

            usuario.atualizarCadastro(
                "Maria",
                new Cpf("98765432100"),
                new Email("maria@email.com"),
                "11888888888",
                criarDonoRestaurante().getTipoUsuario()
            );

            Usuario.UsuarioSnapshot snap = usuario.snapshot();

            assertThat(snap.nome()).isEqualTo("MARIA");
            assertThat(snap.cpf()).isEqualTo("98765432100");
            assertThat(snap.email()).isEqualTo("maria@email.com");
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
        @DisplayName("Deve identificar usuario cliente")
        void deveIdentificarCliente() {

            Usuario usuario = criarUsuarioValido();

            assertThat(usuario.eCliente()).isTrue();
        }

        @Test
        @DisplayName("Deve identificar dono de restaurante")
        void deveIdentificarDonoRestaurante() {

            Usuario usuario = criarDonoRestaurante();

            assertThat(usuario.eDonoDeRestaurante()).isTrue();
        }

        @Test
        @DisplayName("Deve validar cpf usado")
        void deveValidarCpf() {

            Usuario usuario = criarUsuarioValido();

            assertThat(usuario.usaCpf("12345678909")).isTrue();
            assertThat(usuario.usaCpf("00000000000")).isFalse();
        }

        @Test
        @DisplayName("Deve validar email usado")
        void deveValidarEmail() {

            Usuario usuario = criarUsuarioValido();

            assertThat(usuario.usaEmail("usuario@email.com")).isTrue();
            assertThat(usuario.usaEmail("outro@email.com")).isFalse();
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
