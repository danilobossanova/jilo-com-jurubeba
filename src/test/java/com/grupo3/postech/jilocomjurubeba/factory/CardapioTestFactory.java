package com.grupo3.postech.jilocomjurubeba.factory;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import java.math.BigDecimal;

public class CardapioTestFactory {

  private CardapioTestFactory() {}

  public static Cardapio criarCardapioValido() {

    Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

    return new Cardapio(
        1L, "Pizza", "Pizza grande", BigDecimal.valueOf(50), false, "/foto.png", restaurante, true);
  }

  public static Cardapio criarCardapioApenasLocal() {

    Restaurante restaurante = RestauranteTestFactory.criarRestauranteValido();

    return new Cardapio(
        2L, "Lasanha", "Lasanha da casa", BigDecimal.valueOf(60), true, null, restaurante, true);
  }
}
