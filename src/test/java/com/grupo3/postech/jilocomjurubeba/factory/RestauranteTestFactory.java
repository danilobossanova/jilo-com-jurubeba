package com.grupo3.postech.jilocomjurubeba.factory;

import java.time.LocalTime;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.TipoCozinha;

public class RestauranteTestFactory {

    private RestauranteTestFactory() {}

    public static Restaurante criarRestauranteValido() {

        Usuario dono = UsuarioTestFactory.criarUsuarioValido();

        return new Restaurante(
                1L,
                "Restaurante Teste",
                "Rua A, 123",
                TipoCozinha.BRASILEIRA,
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                dono,
                true);
    }

    public static Restaurante criarRestauranteInativo() {

        Usuario dono = UsuarioTestFactory.criarUsuarioValido();

        return new Restaurante(
                2L,
                "Restaurante Fechado",
                "Rua B, 456",
                TipoCozinha.ITALIANA,
                LocalTime.of(11, 0),
                LocalTime.of(23, 0),
                dono,
                false);
    }
}
