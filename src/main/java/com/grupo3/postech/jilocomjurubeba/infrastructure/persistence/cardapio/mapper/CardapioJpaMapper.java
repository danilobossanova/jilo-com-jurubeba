package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.mapper;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;

public final class CardapioJpaMapper {

    private CardapioJpaMapper() {}

    public static CardapioJpaEntity toJpa(Cardapio domain, RestauranteJpaEntity restauranteJpa) {
        Cardapio.CardapioSnapshot dados = domain.snapshot();

        CardapioJpaEntity e = new CardapioJpaEntity();
        e.setId(dados.id());
        e.setNome(dados.nome());
        e.setDescricao(dados.descricao());
        e.setPreco(dados.preco());
        e.setApenasNoLocal(dados.apenasNoLocal());
        e.setCaminhoFoto(dados.caminhoFoto());
        e.setAtivo(dados.ativo());
        e.setRestaurante(restauranteJpa);
        return e;
    }

    public static Cardapio toDomain(CardapioJpaEntity e, Restaurante restauranteDomain) {
        return new Cardapio(
                e.getId(),
                e.getNome(),
                e.getDescricao(),
                e.getPreco(),
                e.isApenasNoLocal(),
                e.getCaminhoFoto(),
                restauranteDomain,
                e.isAtivo()
        );
    }
}