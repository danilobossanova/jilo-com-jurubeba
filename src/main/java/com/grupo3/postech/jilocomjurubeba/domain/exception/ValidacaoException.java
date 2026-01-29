package com.grupo3.postech.jilocomjurubeba.domain.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Exceção lançada quando dados de entrada são inválidos.
 *
 * Mapeada para HTTP 400 (Bad Request) no ControllerAdvice.
 * Suporta múltiplos erros de validação por campo.
 *
 * Exemplo de uso:
 * <pre>
 * Map<String, String> erros = new HashMap<>();
 * erros.put("email", "Email inválido");
 * erros.put("cpf", "CPF deve conter 11 dígitos");
 * throw new ValidacaoException("Dados inválidos", erros);
 * </pre>
 *
 * @author Danilo Fernando
 */
public class ValidacaoException extends DominioException {

    private final Map<String, String> errosPorCampo;

    public ValidacaoException(String mensagem) {
        super(mensagem);
        this.errosPorCampo = Collections.emptyMap();
    }

    public ValidacaoException(String mensagem, Map<String, String> errosPorCampo) {
        super(mensagem);
        this.errosPorCampo = new HashMap<>(errosPorCampo);
    }

    public Map<String, String> getErrosPorCampo() {
        return Collections.unmodifiableMap(errosPorCampo);
    }
}
