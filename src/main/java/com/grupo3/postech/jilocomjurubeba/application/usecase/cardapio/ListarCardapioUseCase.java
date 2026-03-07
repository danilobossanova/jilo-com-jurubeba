package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class ListarCardapioUseCase implements UseCaseSemEntrada<List<CardapioOutput>> {

    private final CardapioGatewayDomain cardapioGateway;

    public ListarCardapioUseCase(CardapioGatewayDomain cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    @Override
    public List<CardapioOutput> executar() {
        return cardapioGateway.findAllCardapio()
                .stream()
                .map(cardapio -> cardapio.paraOutput())
                .toList();
    }
}