package com.grupo3.postech.jilocomjurubeba.application.dto.autenticacao;

/**
 * DTO de entrada para autenticacao de usuario.
 *
 * <p>Transporta as credenciais do usuario do Controller para o {@link
 * com.grupo3.postech.jilocomjurubeba.application.usecase.autenticacao.AutenticarUsuarioUseCase},
 * sem acoplar a camada Application ao formato HTTP (Request). A senha e recebida em texto puro e
 * sera validada pela implementacao do gateway de autenticacao.
 *
 * @param email endereco de email utilizado como identificador de login
 * @param senha senha em texto puro para validacao de credenciais
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AutenticarUsuarioInput(String email, String senha) {}
