package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;

/**
 * Repository Spring Data JPA para Usuario.
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
public interface UsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {

    /**
     * Busca usuario pelo CPF.
     *
     * @param cpf CPF do usuario
     * @return Optional com a entidade JPA ou vazio
     */
    Optional<UsuarioJpaEntity> findByCpf(String cpf);

    /**
     * Busca usuario pelo email (case insensitive).
     *
     * @param email email do usuario
     * @return Optional com a entidade JPA ou vazio
     */
    Optional<UsuarioJpaEntity> findByEmailIgnoreCase(String email);

    /**
     * Verifica se existe usuario com o CPF informado.
     *
     * @param cpf CPF a verificar
     * @return true se existe
     */
    boolean existsByCpf(String cpf);

    /**
     * Verifica se existe usuario com o email informado (case insensitive).
     *
     * @param email email a verificar
     * @return true se existe
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Verifica se existe usuario com o CPF informado, excluindo um id especifico.
     *
     * @param cpf CPF a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo CPF
     */
    boolean existsByCpfAndIdNot(String cpf, Long id);

    /**
     * Verifica se existe usuario com o email informado, excluindo um id especifico.
     *
     * @param email email a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo email
     */
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
