package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO contendo o token JWT apos autenticacao bem-sucedida.
 *
 * <p>Retornado pelo endpoint {@code POST /auth/login} quando as credenciais sao validas. O token
 * deve ser enviado no header {@code Authorization: Bearer {token}} nas requisicoes subsequentes a
 * endpoints autenticados.
 *
 * @param token token JWT de acesso para autorizacao nas demais requisicoes da API
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Resposta de autenticacao com token JWT")
public record LoginResponse(
        @Schema(description = "Token JWT para autorizacao", example = "eyJhbGciOiJIUzI1...")
                String token) {}
