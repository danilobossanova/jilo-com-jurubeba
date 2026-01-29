package com.grupo3.postech.jilocomjurubeba.domain.exception;

/**
 * Exceção lançada quando uma entidade não é encontrada no sistema.
 *
 * Mapeada para HTTP 404 (Not Found) no ControllerAdvice.
 *
 * Exemplo de uso:
 * <pre>
 * throw new EntidadeNaoEncontradaException("Usuario", id);
 * // Mensagem: "Usuario com identificador '123' não foi encontrado"
 * </pre>
 *
 * @author Danilo Fernando
 */
public class EntidadeNaoEncontradaException extends DominioException {

    private final String nomeEntidade;
    private final Object identificador;

    public EntidadeNaoEncontradaException(String nomeEntidade, Object identificador) {
        super(String.format("%s com identificador '%s' não foi encontrado", nomeEntidade, identificador));
        this.nomeEntidade = nomeEntidade;
        this.identificador = identificador;
    }

    public String getNomeEntidade() {
        return nomeEntidade;
    }

    public Object getIdentificador() {
        return identificador;
    }
}
