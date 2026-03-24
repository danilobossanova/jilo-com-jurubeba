package com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario;

import java.util.List;
import java.util.Optional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;

/**
 * Port de saida (Gateway) para operacoes de persistencia de TipoUsuario.
 *
 * <p>Esta interface define o contrato que a camada de dominio exige do mundo externo para persistir
 * e recuperar tipos de usuario. A implementacao concreta fica na camada de infrastructure (ex:
 * {@code TipoUsuarioGatewayJpa}).
 *
 * <p>Na Clean Architecture, este e o mecanismo de <strong>inversao de dependencia</strong>: o
 * dominio define O QUE precisa, e a infraestrutura decide COMO fazer. O dominio depende apenas
 * desta interface abstrata, enquanto a implementacao concreta depende do dominio, invertendo o
 * fluxo natural de dependencia.
 *
 * <p>Operacoes suportadas: CRUD completo (salvar, buscar, listar, deletar) e verificacoes de
 * existencia por nome (com e sem exclusao de id, para validacao de unicidade em atualizacoes).
 *
 * @see TipoUsuario
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public interface TipoUsuarioGateway {

    /**
     * Salva (cria ou atualiza) um tipo de usuario.
     *
     * @param tipoUsuario entidade a ser salva
     * @return entidade salva com id gerado (se criacao)
     */
    TipoUsuario salvar(TipoUsuario tipoUsuario);

    /**
     * Busca um tipo de usuario pelo seu identificador.
     *
     * @param id identificador unico
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<TipoUsuario> buscarPorId(Long id);

    /**
     * Busca um tipo de usuario pelo nome.
     *
     * @param nome nome do tipo (ex: "MASTER")
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<TipoUsuario> buscarPorNome(String nome);

    /**
     * Lista todos os tipos de usuario cadastrados.
     *
     * @return lista de todos os tipos de usuario
     */
    List<TipoUsuario> listarTodos();

    /**
     * Remove um tipo de usuario pelo identificador.
     *
     * @param id identificador unico
     */
    void deletar(Long id);

    /**
     * Verifica se existe um tipo de usuario com o nome informado.
     *
     * @param nome nome a verificar
     * @return true se existe, false caso contrario
     */
    boolean existePorNome(String nome);

    /**
     * Verifica se existe um tipo de usuario com o nome informado, excluindo um id especifico.
     *
     * <p>Util para validacao de unicidade durante atualizacao (nao contar o proprio registro).
     *
     * @param nome nome a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo nome, false caso contrario
     */
    boolean existePorNomeEIdDiferente(String nome, Long id);
}
