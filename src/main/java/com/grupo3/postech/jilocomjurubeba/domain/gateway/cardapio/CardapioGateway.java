package com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio;

import java.util.List;
import java.util.Optional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;

/**
 * Port de saida (Gateway) para operacoes de persistencia de Cardapio.
 *
 * <p>Esta interface define o contrato que a camada de dominio exige do mundo externo para persistir
 * e recuperar cardapios. A implementacao concreta fica na camada de infrastructure (ex: {@code
 * CardapioGatewayJpa}).
 *
 * <p>Na Clean Architecture, este e o mecanismo de <strong>inversao de dependencia</strong>: o
 * dominio define O QUE precisa, e a infraestrutura decide COMO fazer. O dominio depende apenas
 * desta interface abstrata, enquanto a implementacao concreta depende do dominio.
 *
 * <p>Operacoes suportadas: CRUD completo (salvar, buscar por id/nome, listar todos, listar por
 * restaurante, deletar) e verificacao de existencia por nome.
 *
 * @see Cardapio
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public interface CardapioGateway {

    /**
     * Salva (cria ou atualiza) um cardapio.
     *
     * @param cardapio entidade a ser salva
     * @return entidade salva com id gerado (se criacao)
     */
    Cardapio salvar(Cardapio cardapio);

    /**
     * Busca um cardapio pelo seu identificador.
     *
     * @param id identificador unico
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<Cardapio> buscarPorId(Long id);

    /**
     * Busca um cardapio pelo nome.
     *
     * @param nome nome do cardapio
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<Cardapio> buscarPorNome(String nome);

    /**
     * Lista todos os cardapios cadastrados.
     *
     * @return lista de todos os cardapios
     */
    List<Cardapio> listarTodos();

    /**
     * Lista todos os cardapios de um determinado restaurante.
     *
     * @param restauranteId identificador unico do restaurante
     * @return lista de cardapios pertencentes ao restaurante informado
     */
    List<Cardapio> listarPorRestaurante(Long restauranteId);

    /**
     * Remove um cardapio pelo identificador.
     *
     * @param id identificador unico
     */
    void deletar(Long id);

    /**
     * Verifica se existe um cardapio com o nome informado.
     *
     * @param nome nome a verificar
     * @return true se existe, false caso contrario
     */
    boolean existePorNome(String nome);
}
