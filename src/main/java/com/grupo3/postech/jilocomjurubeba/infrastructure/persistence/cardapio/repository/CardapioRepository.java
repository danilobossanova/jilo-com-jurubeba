package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;

/**
 * Repository Spring Data JPA para Cardapio.
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
public interface CardapioRepository extends JpaRepository<CardapioJpaEntity, Long> {

    /**
     * Busca cardapio pelo nome (case insensitive).
     *
     * @param nome nome do item do cardapio
     * @return Optional com a entidade JPA ou vazio
     */
    Optional<CardapioJpaEntity> findByNomeIgnoreCase(String nome);

    /**
     * Verifica se existe cardapio com o nome informado (case insensitive).
     *
     * @param nome nome a verificar
     * @return true se existe
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Lista todos os cardapios de um determinado restaurante.
     *
     * @param restauranteId identificador do restaurante
     * @return lista de cardapios do restaurante
     */
    List<CardapioJpaEntity> findByRestauranteId(Long restauranteId);
}
