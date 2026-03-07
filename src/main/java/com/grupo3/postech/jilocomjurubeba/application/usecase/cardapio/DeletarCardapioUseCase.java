package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class DeletarCardapioUseCase implements UseCaseSemSaida<Long> {

    private final CardapioGatewayDomain cardapioGateway;

    public DeletarCardapioUseCase(CardapioGatewayDomain cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    @Override
    public void executar(Long id) {
        cardapioGateway.findByIdCardapio(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", id));

        cardapioGateway.deleteCardapio(id);
    }
}