package com.grupo3.postech.jilocomjurubeba.domain.exception;

/**
 * Exceção base para todas as exceções de domínio.
 *
 * <p>Esta classe serve como raiz da hierarquia de exceções de domínio, permitindo tratamento
 * centralizado no ControllerAdvice.
 *
 * <p>Subclasses: - {@link EntidadeNaoEncontradaException} - quando uma entidade não existe - {@link
 * RegraDeNegocioException} - violação de regra de negócio - {@link ValidacaoException} - dados
 * inválidos
 *
 * @author Danilo Fernando
 */
public abstract class DominioException extends RuntimeException {

    protected DominioException(String mensagem) {
        super(mensagem);
    }

    protected DominioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
