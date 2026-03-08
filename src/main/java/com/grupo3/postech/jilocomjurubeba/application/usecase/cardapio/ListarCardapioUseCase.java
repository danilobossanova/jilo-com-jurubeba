package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.cardapio.CardapioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;

public class ListarCardapioUseCase implements UseCaseSemEntrada<List<CardapioOutput>> {

    private final CardapioGateway cardapioGateway;

    public ListarCardapioUseCase(CardapioGateway cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    @Override
    public List<CardapioOutput> executar() {
        return cardapioGateway.findAllCardapio()
            .stream()
            .map(CardapioMapper::toOutput)
            .toList();
    }
}
