package com.grupo3.postech.jilocomjurubeba.application.mapper.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;

public class RestauranteMapper {

  public static RestauranteOutput paraOutput(Restaurante restaurante) {

    Long donoId = restaurante.getDono() != null ? restaurante.getDono().getId() : null;

    return new RestauranteOutput(
        restaurante.getId(),
        restaurante.getNome(),
        restaurante.getEndereco(),
        restaurante.getTypeCozinha(),
        restaurante.getHoraAbertura(),
        restaurante.getHoraFechamento(),
        donoId,
        restaurante.isAtivo());
  }
}
