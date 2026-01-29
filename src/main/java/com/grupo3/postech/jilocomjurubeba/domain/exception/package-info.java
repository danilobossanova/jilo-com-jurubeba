/**
 * Exceções de Domínio.
 *
 * Exceções específicas do domínio que representam violações de regras de negócio.
 * Todas as exceções de domínio devem estender DominioException.
 *
 * Hierarquia:
 * - DominioException (base)
 *   - EntidadeNaoEncontradaException
 *   - RegraDeNegocioException
 *   - ValidacaoException
 *
 * Estas exceções são capturadas pelo ControllerAdvice e convertidas em ProblemDetail.
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.domain.exception;
