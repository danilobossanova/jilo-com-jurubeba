package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class BuscarCardapioUseCase implements UseCase<Long, CardapioOutput> {

    private final CardapioGatewayDomain cardapioGatewayDomain;

    public BuscarCardapioUseCase(CardapioGatewayDomain cardapioGatewayDomain) {
        this.cardapioGatewayDomain = cardapioGatewayDomain;
    }

    public CardapioOutput executar(Long id) {
        Cardapio cardapio = cardapioGatewayDomain.findByIdCardapio(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", id));
        return toOutput(cardapio);
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
