package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;

/**
 * Repository Spring Data JPA para TipoUsuario.
 *
 * <p>Spring Data gera a implementacao automaticamente a partir da interface. Metodos custom seguem
 * a convencao de nomes do Spring Data.
 *
 * @author Danilo Fernando
 */
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioJpaEntity, Long> {

    /**
     * Busca tipo de usuario pelo nome.
     *
     * @param nome nome do tipo
     * @return Optional com a entidade JPA ou vazio
     */
    Optional<TipoUsuarioJpaEntity> findByNome(String nome);

    /**
     * Verifica se existe tipo de usuario com o nome informado.
     *
     * @param nome nome a verificar
     * @return true se existe
     */
    boolean existsByNome(String nome);

    /**
     * Verifica se existe tipo de usuario com o nome informado, excluindo um id especifico.
     *
     * @param nome nome a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo nome
     */
    boolean existsByNomeAndIdNot(String nome, Long id);
}
