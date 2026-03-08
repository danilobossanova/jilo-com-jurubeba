package com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import java.util.List;
import java.util.Optional;

public interface CardapioGateway {
  Cardapio saveCardapio(Cardapio cardapio);

  Optional<Cardapio> findByIdCardapio(Long id);

  List<Cardapio> findAllCardapio();

  void deleteCardapio(Long id);

  Optional<Cardapio> findByNome(String nome);
}
