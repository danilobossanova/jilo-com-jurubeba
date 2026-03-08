package com.grupo3.postech.jilocomjurubeba.application.mapper.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;

public class CardapioMapper {

    private CardapioMapper() {}

    public static CardapioOutput toOutput(Cardapio cardapio) {
        if (cardapio == null) {
            return null;
        }

        return new CardapioOutput(
            cardapio.getId(),
            cardapio.getNome(),
            cardapio.getDescricao(),
            cardapio.getPreco(),
            cardapio.isApenasNoLocal(),
            cardapio.getCaminhoFoto(),
            cardapio.getRestaurante() != null ? cardapio.getRestaurante().getId() : null,
            cardapio.isAtivo()
        );
    }
}
