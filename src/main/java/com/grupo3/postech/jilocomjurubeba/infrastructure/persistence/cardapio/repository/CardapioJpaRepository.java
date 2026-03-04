package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.repository;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardapioJpaRepository extends JpaRepository<CardapioJpaEntity, Long> {

    Optional<CardapioJpaEntity> findByNome(String nome);

    List<CardapioJpaEntity> findAllByAtivoTrue();

    // (Opcional - melhora performance do endpoint público por restaurante)
    // List<CardapioJpaEntity> findAllByRestaurante_IdAndAtivoTrue(Long restauranteId);
}