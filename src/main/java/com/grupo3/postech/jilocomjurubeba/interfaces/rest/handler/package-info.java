/**
 * Handlers de Erro - Tratamento Centralizado de Exceções.
 *
 * Contém o ControllerAdvice que captura exceções e as converte
 * em respostas HTTP padronizadas usando RFC 7807 (Problem Detail).
 *
 * Mapeamento de exceções para HTTP:
 * - ValidacaoException -> 400 Bad Request
 * - EntidadeNaoEncontradaException -> 404 Not Found
 * - RegraDeNegocioException -> 422 Unprocessable Entity
 * - Exception genérica -> 500 Internal Server Error
 *
 * @author Danilo Fernando
 * @see com.grupo3.postech.jilocomjurubeba.interfaces.rest.handler.GlobalExceptionHandler
 */
package com.grupo3.postech.jilocomjurubeba.interfaces.rest.handler;
