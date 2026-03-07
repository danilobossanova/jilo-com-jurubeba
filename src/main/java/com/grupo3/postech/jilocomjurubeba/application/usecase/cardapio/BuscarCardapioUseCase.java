package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class BuscarCardapioUseCase implements UseCase<Long, CardapioOutput> {

    private final CardapioGatewayDomain cardapioGateway;

    public BuscarCardapioUseCase(CardapioGatewayDomain cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    @Override
    public CardapioOutput executar(Long id) {
        return cardapioGateway.findByIdCardapio(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", id))
                .paraOutput();
    }
}