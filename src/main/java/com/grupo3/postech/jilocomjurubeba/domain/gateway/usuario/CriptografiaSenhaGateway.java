package com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario;

/**
 * Port de saida (Gateway) para criptografia de senhas.
 *
 * <p>Esta interface define o contrato que a camada de dominio exige do mundo externo para
 * criptografar senhas de usuarios. A implementacao concreta fica na camada de infrastructure e pode
 * utilizar qualquer algoritmo de hashing (ex: BCrypt, Argon2, PBKDF2).
 *
 * <p>Na Clean Architecture, este e o mecanismo de <strong>inversao de dependencia</strong>: o
 * dominio define O QUE precisa (criptografar senhas), e a infraestrutura decide COMO fazer (qual
 * algoritmo utilizar). Isso permite trocar o algoritmo de criptografia sem alterar o dominio ou os
 * use cases.
 *
 * @see com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario
 * @see UsuarioGateway
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public interface CriptografiaSenhaGateway {

    /**
     * Criptografa uma senha em texto aberto.
     *
     * @param senhaAberta senha em texto aberto a ser criptografada
     * @return hash da senha criptografada
     */
    String criptografar(String senhaAberta);
}
