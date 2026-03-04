package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class CriarCardapioUseCase implements UseCase<CriarCardapioInput, CardapioOutput> {

    private final CardapioGatewayDomain cardapioGatewayDomain;

    public CriarCardapioUseCase(CardapioGatewayDomain cardapioGatewayDomain) {
        this.cardapioGatewayDomain = cardapioGatewayDomain;
    }

    @Override
    public CardapioOutput executar(CriarCardapioInput input) {
        if (cardapioGatewayDomain.findByNome(input.nome().trim().toUpperCase()).isPresent()) {
            throw new RegraDeNegocioException("Cardapio ja cadastrado");
        }

        Cardapio cardapio =
                new Cardapio(
                        input.nome(),
                        input.descricao(),
                        input.preco(),
                        input.apenasNoLocal(),
                        input.caminhoFoto(),
                        input.restaurante());

        Cardapio salvo = cardapioGatewayDomain.saveCardapio(cardapio);

        return toOutput(salvo);
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
