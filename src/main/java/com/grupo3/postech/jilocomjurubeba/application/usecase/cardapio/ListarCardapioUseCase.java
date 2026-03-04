package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class ListarCardapioUseCase implements UseCaseSemEntrada<List<CardapioOutput>> {

    private final CardapioGatewayDomain cardapioGatewayDomain;

    public ListarCardapioUseCase(CardapioGatewayDomain cardapioGatewayDomain) {
        this.cardapioGatewayDomain = cardapioGatewayDomain;
    }

    @Override
    public List<CardapioOutput> executar() {
        return cardapioGatewayDomain.findAllCardapio().stream().map(this::toOutput).toList();
    }

    private CardapioOutput toOutput(Cardapio cardapio) {
        Long restauranteId =
                cardapio.getRestaurante() != null ? cardapio.getRestaurante().getId() : null;

        return new CardapioOutput(
                cardapio.getId(),
                cardapio.getNome(),
                cardapio.getDescricao(),
                cardapio.getPreco(),
                cardapio.isApenasNoLocal(),
                cardapio.getCaminhoFoto(),
                restauranteId,
                cardapio.isAtivo());
    }
}
