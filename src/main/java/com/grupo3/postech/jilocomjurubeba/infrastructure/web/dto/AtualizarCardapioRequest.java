package com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto;

import java.math.BigDecimal;

public record AtualizarCardapioRequest(
        String nome,
        String descricao,
        BigDecimal preco,
        boolean apenasNoLocal,
        String caminhoFoto,
        Long restauranteId
) {}
