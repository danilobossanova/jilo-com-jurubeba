package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.cardapio.CardapioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;

public class BuscarCardapioUseCase implements UseCase<Long, CardapioOutput> {

  private final CardapioGateway cardapioGateway;

  public BuscarCardapioUseCase(CardapioGateway cardapioGateway) {
    this.cardapioGateway = cardapioGateway;
  }

  @Override
  public CardapioOutput executar(Long id) {

    Cardapio cardapio =
        cardapioGateway
            .findByIdCardapio(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", id));

    return CardapioMapper.toOutput(cardapio);
  }
}
