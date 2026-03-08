package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUsuarioJpaRepository extends JpaRepository<TipoUsuarioJpaEntity, Long> {

  Optional<TipoUsuarioJpaEntity> findByNomeIgnoreCase(String nome);

  boolean existsByNomeIgnoreCase(String nome);

  boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
