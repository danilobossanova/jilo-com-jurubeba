package com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario;

/**
 * Port de saida (Gateway) para autenticacao de usuarios.
 *
 * <p>Define o contrato que a camada de dominio exige para autenticar credenciais e gerar tokens de
 * acesso. A implementacao concreta fica na camada de infrastructure e pode utilizar qualquer
 * mecanismo de autenticacao (ex: JWT, OAuth2, sessao, etc).
 *
 * <p>Na Clean Architecture, este e o mecanismo de <strong>inversao de dependencia</strong>: o
 * dominio define O QUE precisa (autenticar e gerar token), e a infraestrutura decide COMO fazer.
 *
 * @see com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario
 * @see UsuarioGateway
 * @see CriptografiaSenhaGateway
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public interface AutenticacaoGateway {

    /**
     * Autentica um usuario com email e senha, retornando um token de acesso.
     *
     * @param email email do usuario
     * @param senha senha do usuario
     * @return token de acesso gerado
     * @throws com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException se as
     *     credenciais forem invalidas
     */
    String autenticar(String email, String senha);
}
