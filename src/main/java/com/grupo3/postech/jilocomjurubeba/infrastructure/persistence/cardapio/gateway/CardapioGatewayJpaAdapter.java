package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.mapper.CardapioJpaMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.repository.CardapioJpaRepository;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.mapper.RestauranteMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.repository.RestauranteJpaRepository;

@Component
public class CardapioGatewayJpaAdapter implements CardapioGateway {

    private final CardapioJpaRepository cardapioRepo;
    private final RestauranteJpaRepository restauranteRepo;
    private final RestauranteMapper restauranteMapper;

    public CardapioGatewayJpaAdapter(
            CardapioJpaRepository cardapioRepo,
            RestauranteJpaRepository restauranteRepo,
            RestauranteMapper restauranteMapper
    ) {
        this.cardapioRepo = cardapioRepo;
        this.restauranteRepo = restauranteRepo;
        this.restauranteMapper = restauranteMapper;
    }

    @Override
    @Transactional
    public Cardapio saveCardapio(Cardapio cardapio) {
        Long restauranteId = cardapio.snapshot().restauranteId();
        if (restauranteId == null) {
            throw new IllegalArgumentException("restauranteId é obrigatório no Cardápio");
        }

        RestauranteJpaEntity restauranteJpa = restauranteRepo
                .findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado para o cardápio"));

        CardapioJpaEntity salvo = cardapioRepo.save(CardapioJpaMapper.toJpa(cardapio, restauranteJpa));
        return mapToDomain(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cardapio> findByIdCardapio(Long id) {
        return cardapioRepo.findById(id).map(this::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cardapio> findAllCardapio() {
        return cardapioRepo.findAllByAtivoTrue()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteCardapio(Long id) {
        cardapioRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cardapio> findByNome(String nome) {
        String normalizado = nome == null ? null : nome.trim().toUpperCase();
        return cardapioRepo.findByNome(normalizado).map(this::mapToDomain);
    }

    private Cardapio mapToDomain(CardapioJpaEntity entity) {
        return CardapioJpaMapper.toDomain(
                entity,
                restauranteMapper.toDomain(entity.getRestaurante())
        );
    }
}
