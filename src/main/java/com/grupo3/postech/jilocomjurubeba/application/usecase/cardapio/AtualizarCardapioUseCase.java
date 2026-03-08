package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import java.math.BigDecimal;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.cardapio.CardapioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

public class AtualizarCardapioUseCase implements UseCase<AtualizarCardapioInput, CardapioOutput> {

    private final CardapioGateway cardapioGateway;
    private final RestauranteGateway restauranteGateway;

    public AtualizarCardapioUseCase(
        CardapioGateway cardapioGateway,
        RestauranteGateway restauranteGateway
    ) {
        this.cardapioGateway = cardapioGateway;
        this.restauranteGateway = restauranteGateway;
    }

    @Override
    public CardapioOutput executar(AtualizarCardapioInput input) {

        if (input == null) {
            throw new RegraDeNegocioException("Dados para atualização são obrigatórios");
        }

        if (input.id() == null) {
            throw new RegraDeNegocioException("id do cardápio é obrigatório");
        }

        Cardapio cardapio = cardapioGateway.findByIdCardapio(input.id())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", input.id()));

        Cardapio.CardapioSnapshot atual = cardapio.snapshot();

        String nomeNovo = hasText(input.nome()) ? input.nome() : atual.nome();
        String descricaoNova = input.descricao() != null ? input.descricao() : atual.descricao();
        BigDecimal precoNovo = input.preco() != null ? input.preco() : atual.preco();
        boolean apenasNoLocalNovo = input.apenasNoLocal() != null ? input.apenasNoLocal() : atual.apenasNoLocal();
        String caminhoFotoNovo = input.caminhoFoto() != null ? input.caminhoFoto() : atual.caminhoFoto();

        Long restauranteId = input.restauranteId() != null ? input.restauranteId() : atual.restauranteId();

        Restaurante restaurante = restauranteGateway.findByIdRestaurante(restauranteId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", restauranteId));

        cardapio.atualizarCadastro(
            nomeNovo,
            descricaoNova,
            precoNovo,
            apenasNoLocalNovo,
            caminhoFotoNovo,
            restaurante
        );

        Cardapio atualizado = cardapioGateway.saveCardapio(cardapio);

        return CardapioMapper.toOutput(atualizado);
    }

    private boolean hasText(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
}
