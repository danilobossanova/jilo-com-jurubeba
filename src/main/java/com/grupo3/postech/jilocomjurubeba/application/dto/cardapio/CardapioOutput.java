package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;

import java.math.BigDecimal;

public record CardapioOutput(Long id,
                             String nome,
                             String descricao,
                             BigDecimal preco,
                             boolean apenasNoLocal,
                             String caminhoFoto,
                             Restaurante restaurante,
                             Boolean ativo) {
}
