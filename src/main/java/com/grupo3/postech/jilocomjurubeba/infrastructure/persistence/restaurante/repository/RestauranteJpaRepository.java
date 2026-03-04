package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;

public interface RestauranteJpaRepository extends JpaRepository<RestauranteJpaEntity, Long> {

    // ✅ O "join fetch d.tipoUsuario" é o que mata o erro de vez
    @Query("""
        SELECT r FROM RestauranteJpaEntity r
        JOIN FETCH r.dono d
        JOIN FETCH d.tipoUsuario
        WHERE r.id = :id
    """)
    Optional<RestauranteJpaEntity> findByIdFetchDono(@Param("id") Long id);

    @Query("""
        SELECT r FROM RestauranteJpaEntity r
        JOIN FETCH r.dono d
        JOIN FETCH d.tipoUsuario
        WHERE UPPER(r.nome) = UPPER(:nome)
    """)
    Optional<RestauranteJpaEntity> findByNomeIgnoreCaseFetchDono(@Param("nome") String nome);

    @Query("""
        SELECT r FROM RestauranteJpaEntity r
        JOIN FETCH r.dono d
        JOIN FETCH d.tipoUsuario
        WHERE r.ativo = true
    """)
    List<RestauranteJpaEntity> findAllByAtivoTrueFetchDono();
}
