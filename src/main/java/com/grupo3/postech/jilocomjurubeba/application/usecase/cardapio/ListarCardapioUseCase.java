package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

import java.util.List;

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
        return new CardapioOutput(
            cardapio.getId(),
            cardapio.getNome(),
            cardapio.getDescricao(),
            cardapio.getPreco(),
            cardapio.isApenasNoLocal(),
            cardapio.getCaminhoFoto(),
            cardapio.getRestaurante(),
            cardapio.isAtivo()
        );
    }
}
