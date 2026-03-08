package com.grupo3.postech.jilocomjurubeba.factory;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import java.time.LocalTime;

public class RestauranteTestFactory {

  private RestauranteTestFactory() {}

  public static Restaurante criarRestauranteValido() {

    Usuario dono = UsuarioTestFactory.criarUsuarioValido();

    return new Restaurante(
        1L,
        "Restaurante Teste",
        "Rua A, 123",
        TypeCozinha.BRASILEIRA,
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
        TypeCozinha.ITALIANA,
        LocalTime.of(11, 0),
        LocalTime.of(23, 0),
        dono,
        false);
  }
}
