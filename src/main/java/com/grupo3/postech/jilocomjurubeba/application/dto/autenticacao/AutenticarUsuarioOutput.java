package com.grupo3.postech.jilocomjurubeba.application.dto.autenticacao;

/**
 * DTO de saida da autenticacao de usuario.
 *
 * <p>Encapsula o token de acesso gerado apos a autenticacao bem-sucedida. O formato do token (JWT,
 * OAuth, etc) e definido pela implementacao do gateway de autenticacao na camada Infrastructure,
 * mantendo a camada Application agnositica ao mecanismo utilizado.
 *
 * @param token token de acesso gerado (ex: JWT Bearer token)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AutenticarUsuarioOutput(String token) {}
