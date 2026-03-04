package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.gateway;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.mapper.CardapioJpaMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.repository.CardapioJpaRepository;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.repository.RestauranteJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CardapioGatewayJpaAdapter implements CardapioGatewayDomain {

    private final CardapioJpaRepository cardapioRepo;
    private final RestauranteJpaRepository restauranteRepo;

    public CardapioGatewayJpaAdapter(
            CardapioJpaRepository cardapioRepo,
            RestauranteJpaRepository restauranteRepo) {
        this.cardapioRepo = cardapioRepo;
        this.restauranteRepo = restauranteRepo;
    }

    @Override
    @Transactional
    public Cardapio saveCardapio(Cardapio cardapio) {
        Long restauranteId = cardapio.getRestaurante() != null ? cardapio.getRestaurante().getId() : null;
        if (restauranteId == null) {
            throw new IllegalArgumentException("restauranteId é obrigatório no Cardápio");
        }

        RestauranteJpaEntity restauranteJpa = restauranteRepo
                .findById(restauranteId)
                .orElseThrow();

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
        // público: normalmente só ativos
        return cardapioRepo.findAllByAtivoTrue().stream().map(this::mapToDomain).toList();
    }

    @Override
    @Transactional
    public void deleteCardapio(Long id) {
        // ⚠️ Se teu projeto usa soft delete, esse método pode nem ser chamado.
        // Hard delete:
        cardapioRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cardapio> findByNome(String nome) {
        String normalizado = nome == null ? null : nome.trim().toUpperCase();
        return cardapioRepo.findByNome(normalizado).map(this::mapToDomain);
    }

    private Cardapio mapToDomain(CardapioJpaEntity e) {
        Long restId = e.getRestaurante() != null ? e.getRestaurante().getId() : null;

        // ✅ ESSENCIAL: restauranteId preenchido no domínio (pra filtro do endpoint público)
        Restaurante restauranteDomain = new Restaurante();
        restauranteDomain.setId(restId);

        // ✅ ESSENCIAL: CardapioJpaMapper.toDomain agora seta id/ativo no Cardápio domínio
        return CardapioJpaMapper.toDomain(e, restauranteDomain);
    }
}