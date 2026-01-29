package com.grupo3.postech.jilocomjurubeba.domain.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 *
 * <p>Mapeada para HTTP 422 (Unprocessable Entity) no ControllerAdvice.
 *
 * <p>Exemplo de uso:
 *
 * <pre>
 * throw new RegraDeNegocioException("Restaurante não pode ter horário de fechamento antes do horário de abertura");
 * </pre>
 *
 * @author Danilo Fernando
 */
public class RegraDeNegocioException extends DominioException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }

    public RegraDeNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
