package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.mapper;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;

public final class CardapioJpaMapper {

    private CardapioJpaMapper() {}

    public static CardapioJpaEntity toJpa(Cardapio domain, RestauranteJpaEntity restauranteJpa) {
        CardapioJpaEntity e = new CardapioJpaEntity();

        // ✅ ESSENCIAL: ID no JPA vindo do domínio
        e.setId(domain.getId());

        e.setNome(domain.getNome());
        e.setDescricao(domain.getDescricao());
        e.setPreco(domain.getPreco());
        e.setApenasNoLocal(domain.isApenasNoLocal());
        e.setCaminhoFoto(domain.getCaminhoFoto());

        // ✅ ESSENCIAL: ativo coerente
        e.setAtivo(domain.isAtivo());

        e.setRestaurante(restauranteJpa);
        return e;
    }

    public static Cardapio toDomain(CardapioJpaEntity e, Restaurante restauranteDomain) {
        Cardapio c = new Cardapio(
                e.getNome(),
                e.getDescricao(),
                e.getPreco(),
                e.isApenasNoLocal(),
                e.getCaminhoFoto(),
                restauranteDomain
        );

        // ✅ ESSENCIAL: sem isso, UPDATE vira INSERT (duplica no delete/atualizar)
        c.setId(e.getId());
        c.setAtivo(e.isAtivo());

        return c;
    }
}