package com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario;

import java.util.List;
import java.util.Optional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;

/**
 * Port de saida (Gateway) para operacoes de persistencia de Usuario.
 *
 * <p>Esta interface define o contrato que a camada de dominio exige do mundo externo para persistir
 * e recuperar usuarios. A implementacao concreta fica na camada de infrastructure (ex: {@code
 * UsuarioGatewayJpa}).
 *
 * <p>Na Clean Architecture, este e o mecanismo de <strong>inversao de dependencia</strong>: o
 * dominio define O QUE precisa, e a infraestrutura decide COMO fazer. O dominio depende apenas
 * desta interface abstrata, enquanto a implementacao concreta depende do dominio.
 *
 * <p>Operacoes suportadas: CRUD completo (salvar, buscar por id/CPF/email, listar, deletar) e
 * verificacoes de existencia por CPF e email (com e sem exclusao de id, para validacao de unicidade
 * em atualizacoes).
 *
 * @see Usuario
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.CriptografiaSenhaGateway
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.AutenticacaoGateway
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public interface UsuarioGateway {

    /**
     * Salva (cria ou atualiza) um usuario.
     *
     * @param usuario entidade a ser salva
     * @return entidade salva com id gerado (se criacao)
     */
    Usuario salvar(Usuario usuario);

    /**
     * Busca um usuario pelo seu identificador.
     *
     * @param id identificador unico
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<Usuario> buscarPorId(Long id);

    /**
     * Busca um usuario pelo CPF.
     *
     * @param cpf CPF do usuario
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<Usuario> buscarPorCpf(String cpf);

    /**
     * Busca um usuario pelo email.
     *
     * @param email email do usuario
     * @return Optional contendo a entidade ou vazio se nao encontrada
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Lista todos os usuarios cadastrados.
     *
     * @return lista de todos os usuarios
     */
    List<Usuario> listarTodos();

    /**
     * Remove um usuario pelo identificador.
     *
     * @param id identificador unico
     */
    void deletar(Long id);

    /**
     * Verifica se existe um usuario com o CPF informado.
     *
     * @param cpf CPF a verificar
     * @return true se existe, false caso contrario
     */
    boolean existePorCpf(String cpf);

    /**
     * Verifica se existe um usuario com o email informado.
     *
     * @param email email a verificar
     * @return true se existe, false caso contrario
     */
    boolean existePorEmail(String email);

    /**
     * Verifica se existe um usuario com o CPF informado, excluindo um id especifico.
     *
     * <p>Util para validacao de unicidade durante atualizacao (nao contar o proprio registro).
     *
     * @param cpf CPF a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo CPF, false caso contrario
     */
    boolean existePorCpfEIdDiferente(String cpf, Long id);

    /**
     * Verifica se existe um usuario com o email informado, excluindo um id especifico.
     *
     * <p>Util para validacao de unicidade durante atualizacao (nao contar o proprio registro).
     *
     * @param email email a verificar
     * @param id id a excluir da verificacao
     * @return true se existe outro registro com o mesmo email, false caso contrario
     */
    boolean existePorEmailEIdDiferente(String email, Long id);
}
