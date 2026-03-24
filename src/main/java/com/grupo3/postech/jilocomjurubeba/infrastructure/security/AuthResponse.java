package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

/**
 * DTO de resposta de autenticacao (login).
 *
 * <p>Retorna o token JWT gerado apos autenticacao bem-sucedida via endpoint {@code POST
 * /v1/auth/login}.
 *
 * @param token token JWT assinado para uso em requisicoes autenticadas (header Authorization)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AuthResponse(String token) {}
