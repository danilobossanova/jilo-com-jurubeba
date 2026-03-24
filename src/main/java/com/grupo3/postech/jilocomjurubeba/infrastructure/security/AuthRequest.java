package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * DTO de requisicao de autenticacao (login).
 *
 * <p>Recebe as credenciais do usuario para autenticacao via endpoint {@code POST /v1/auth/login}. O
 * campo email aceita aliases JSON: {@code username}, {@code user} e {@code login} para
 * flexibilidade de integracao.
 *
 * @param email email do usuario (aceita aliases: username, user, login)
 * @param senha senha em texto aberto para validacao
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AuthRequest(@JsonAlias({"username", "user", "login"}) String email, String senha) {}
