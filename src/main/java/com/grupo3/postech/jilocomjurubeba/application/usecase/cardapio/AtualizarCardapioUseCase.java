package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class AtualizarCardapioUseCase implements UseCase<AtualizarCardapioInput, CardapioOutput> {

    private final CardapioGatewayDomain cardapioGatewayDomain;

    public AtualizarCardapioUseCase(CardapioGatewayDomain cardapioGatewayDomain) {
        this.cardapioGatewayDomain = cardapioGatewayDomain;
    }

    @Override
    public CardapioOutput executar(AtualizarCardapioInput input) {

        Cardapio cardapio = cardapioGatewayDomain.findByIdCardapio(input.id())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", input.id()));

        cardapio.atualizarDados(
            input.nome(),
            input.descricao(),
            input.preco(),
            input.apenasNoLocal(),
            input.caminhoFoto(),
            input.restaurante()
        );

        Cardapio atualizado = cardapioGatewayDomain.saveCardapio(cardapio);
        return toOutput(atualizado);
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
