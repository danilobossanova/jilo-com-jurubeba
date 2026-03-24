package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;

/**
 * Repository Spring Data JPA para Restaurante.
 *
 * <p>Spring Data gera a implementacao automaticamente a partir da interface. Metodos custom seguem
 * a convencao de nomes do Spring Data.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public interface RestauranteRepository extends JpaRepository<RestauranteJpaEntity, Long> {

    /**
     * Busca restaurante pelo nome (case insensitive).
     *
     * @param nome nome do restaurante
     * @return Optional com a entidade JPA ou vazio
     */
    Optional<RestauranteJpaEntity> findByNomeIgnoreCase(String nome);

    /**
     * Verifica se existe restaurante com o nome informado (case insensitive).
     *
     * @param nome nome a verificar
     * @return true se existe
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Verifica se existe restaurante com o nome informado, excluindo um id especifico.
     *
     * @param nome nome a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo nome
     */
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    /**
     * Lista todos os restaurantes de um determinado dono.
     *
     * @param donoId identificador do dono
     * @return lista de restaurantes do dono
     */
    List<RestauranteJpaEntity> findByDonoId(Long donoId);
}
