// UsuarioJpaRepository.java
package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, Long> {

  @Query(
      """
        SELECT u FROM UsuarioJpaEntity u
        JOIN FETCH u.tipoUsuario
        WHERE u.id = :id
    """)
  Optional<UsuarioJpaEntity> findByIdFetchTipoUsuario(@Param("id") Long id);

  @Query(
      """
        SELECT DISTINCT u FROM UsuarioJpaEntity u
        JOIN FETCH u.tipoUsuario
        ORDER BY u.id
    """)
  List<UsuarioJpaEntity> findAllFetchTipoUsuario();

  @Query(
      """
        SELECT u FROM UsuarioJpaEntity u
        JOIN FETCH u.tipoUsuario
        WHERE u.email = :email
    """)
  Optional<UsuarioJpaEntity> findByEmailFetchTipoUsuario(@Param("email") String email);

  @Query(
      """
        SELECT u FROM UsuarioJpaEntity u
        JOIN FETCH u.tipoUsuario
        WHERE u.cpf = :cpf
    """)
  Optional<UsuarioJpaEntity> findByCpfFetchTipoUsuario(@Param("cpf") String cpf);

  Optional<UsuarioJpaEntity> findByEmail(String email);

  Optional<UsuarioJpaEntity> findByCpf(String cpf);
}
