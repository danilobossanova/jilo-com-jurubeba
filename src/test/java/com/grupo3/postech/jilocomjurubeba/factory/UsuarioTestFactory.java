package com.grupo3.postech.jilocomjurubeba.factory;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

public class UsuarioTestFactory {

    private UsuarioTestFactory() {}

    public static Usuario criarUsuarioValido() {

        TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Usuario cliente do sistema", true);

        return new Usuario(
                1L,
                "Usuario Teste",
                new Cpf("12345678909"),
                new Email("usuario@email.com"),
                "11999999999",
                tipo,
                "123456",
                true,
                List.of());
    }

    public static Usuario criarDonoRestaurante() {

        TipoUsuario tipo =
                new TipoUsuario(2L, "DONO_RESTAURANTE", "Usuario dono de restaurante", true);

        return new Usuario(
                2L,
                "Dono Restaurante",
                new Cpf("98765432100"),
                new Email("dono@email.com"),
                "11988888888",
                tipo,
                "123456",
                true,
                List.of());
    }

    public static Usuario criarUsuarioInativo() {

        TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE", "Usuario cliente do sistema", true);

        return new Usuario(
                3L,
                "Usuario Inativo",
                new Cpf("11122233344"),
                new Email("inativo@email.com"),
                "11977777777",
                tipo,
                "123456",
                false,
                List.of());
    }
}
