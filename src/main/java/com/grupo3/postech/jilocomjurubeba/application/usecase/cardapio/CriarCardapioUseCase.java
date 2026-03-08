package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.cardapio.CardapioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

public class CriarCardapioUseCase implements UseCase<CriarCardapioInput, CardapioOutput> {

  private final CardapioGateway cardapioGateway;
  private final RestauranteGateway restauranteGateway;

  public CriarCardapioUseCase(
      CardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
    this.cardapioGateway = cardapioGateway;
    this.restauranteGateway = restauranteGateway;
  }

  @Override
  public CardapioOutput executar(CriarCardapioInput input) {
    if (input == null) {
      throw new RegraDeNegocioException("Dados do cardápio são obrigatórios");
    }

    if (input.restauranteId() == null) {
      throw new RegraDeNegocioException("restauranteId é obrigatório");
    }

    if (cardapioGateway.findByNome(input.nome().trim()).isPresent()) {
      throw new RegraDeNegocioException("Item de cardápio já cadastrado");
    }

    Restaurante restaurante =
        restauranteGateway
            .findByIdRestaurante(input.restauranteId())
            .orElseThrow(
                () -> new EntidadeNaoEncontradaException("Restaurante", input.restauranteId()));

    Cardapio cardapio =
        new Cardapio(
            input.nome(),
            input.descricao(),
            input.preco(),
            input.apenasNoLocal(),
            input.caminhoFoto(),
            restaurante);

    Cardapio salvo = cardapioGateway.saveCardapio(cardapio);
    return CardapioMapper.toOutput(salvo);
  }
}
