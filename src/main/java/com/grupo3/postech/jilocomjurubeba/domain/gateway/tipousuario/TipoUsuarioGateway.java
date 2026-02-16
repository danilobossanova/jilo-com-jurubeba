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
 * dominio define O QUE precisa, e a infraestrutura decide COMO fazer.
 *
 * @author Danilo Fernando
 * @see TipoUsuario
 * @see com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario
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
