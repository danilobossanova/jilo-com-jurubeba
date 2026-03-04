package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import java.math.BigDecimal;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;

public record AtualizarCardapioInput(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        boolean apenasNoLocal,
        String caminhoFoto,
        Restaurante restaurante) {}
