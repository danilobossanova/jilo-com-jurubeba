package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

import java.util.Optional;

public class DeletarCardapioUseCase implements UseCaseSemSaida<Long> {

    private final CardapioGatewayDomain cardapioGatewayDomain;

    public DeletarCardapioUseCase(CardapioGatewayDomain cardapioGatewayDomain) {
        this.cardapioGatewayDomain = cardapioGatewayDomain;
    }

    @Override
    public void executar(Long input) {
        Optional<Cardapio> cardapio = cardapioGatewayDomain.findByIdCardapio(input)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", input));

        cardapio.desativar();
        cardapioGatewayDomain.saveCardapio(cardapio);

    }


}
