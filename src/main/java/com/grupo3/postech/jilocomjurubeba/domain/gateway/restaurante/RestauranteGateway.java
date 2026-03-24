package com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante;

import java.util.List;
import java.util.Optional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;

/**
 * Port de saida (Gateway) para operacoes de persistencia de Restaurante.
 *
 * <p>Esta interface define o contrato que a camada de dominio exige do mundo externo para persistir
 * e recuperar restaurantes. A implementacao concreta fica na camada de infrastructure (ex: {@code
 * RestauranteGatewayJpa}).
 *
 * <p>Na Clean Architecture, este e o mecanismo de <strong>inversao de dependencia</strong>: o
 * dominio define O QUE precisa, e a infraestrutura decide COMO fazer. O dominio depende apenas
 * desta interface abstrata, enquanto a implementacao concreta depende do dominio.
 *
 * <p>Operacoes suportadas: CRUD completo (salvar, buscar por id/nome, listar todos, listar por
 * dono, deletar) e verificacoes de existencia por nome (com e sem exclusao de id, para validacao de
 * unicidade em atualizacoes).
 *
 * @see Restaurante
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public interface RestauranteGateway {

    /**
     * Salva (cria ou atualiza) um restaurante.
     *
     * @param restaurante entidade a ser salva
     * @return entidade salva com id gerado (se criacao)
     */
    Restaurante salvar(Restaurante restaurante);

    /**
     * Busca um restaurante pelo seu identificador.
     *
     * @param id identificador unico
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<Restaurante> buscarPorId(Long id);

    /**
     * Busca um restaurante pelo nome.
     *
     * @param nome nome do restaurante
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<Restaurante> buscarPorNome(String nome);

    /**
     * Lista todos os restaurantes cadastrados.
     *
     * @return lista de todos os restaurantes
     */
    List<Restaurante> listarTodos();

    /**
     * Lista todos os restaurantes de um determinado dono.
     *
     * @param donoId identificador unico do dono do restaurante
     * @return lista de restaurantes pertencentes ao dono informado
     */
    List<Restaurante> listarPorDono(Long donoId);

    /**
     * Remove um restaurante pelo identificador.
     *
     * @param id identificador unico
     */
    void deletar(Long id);

    /**
     * Verifica se existe um restaurante com o nome informado.
     *
     * @param nome nome a verificar
     * @return true se existe, false caso contrario
     */
    boolean existePorNome(String nome);

    /**
     * Verifica se existe um restaurante com o nome informado, excluindo um id especifico.
     *
     * <p>Util para validacao de unicidade durante atualizacao (nao contar o proprio registro).
     *
     * @param nome nome a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo nome, false caso contrario
     */
    boolean existePorNomeEIdDiferente(String nome, Long id);
}
