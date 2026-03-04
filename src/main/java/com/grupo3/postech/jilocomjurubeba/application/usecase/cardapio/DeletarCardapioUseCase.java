package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

public class DeletarCardapioUseCase implements UseCaseSemSaida<Long> {

    private final CardapioGatewayDomain cardapioGatewayDomain;

    public DeletarCardapioUseCase(CardapioGatewayDomain cardapioGatewayDomain) {
        this.cardapioGatewayDomain = Objects.requireNonNull(cardapioGatewayDomain);
    }

    @Override
    public void executar(Long input) {
        if (input == null) {
            throw new IllegalArgumentException("Id do Cardapio não pode ser nulo");
        }

        Cardapio cardapio =
                cardapioGatewayDomain
                        .findByIdCardapio(input)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", input));

        // Soft delete
        cardapio.desativar();

        cardapioGatewayDomain.saveCardapio(cardapio);
    }
}
