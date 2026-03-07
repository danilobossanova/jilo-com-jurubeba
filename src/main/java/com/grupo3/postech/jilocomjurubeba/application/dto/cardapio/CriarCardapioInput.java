package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import java.math.BigDecimal;

public record CriarCardapioInput(
        String nome,
        String descricao,
        BigDecimal preco,
        boolean apenasNoLocal,
        String caminhoFoto,
        Long restauranteId
) {}