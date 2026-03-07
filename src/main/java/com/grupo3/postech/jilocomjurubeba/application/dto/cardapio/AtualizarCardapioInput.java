package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import java.math.BigDecimal;

public record AtualizarCardapioInput(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Boolean apenasNoLocal,
        String caminhoFoto,
        Long restauranteId
) {}