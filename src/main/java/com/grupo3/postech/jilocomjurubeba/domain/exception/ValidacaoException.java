package com.grupo3.postech.jilocomjurubeba.domain.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Excecao lancada quando dados de entrada sao invalidos.
 *
 * <p>Mapeada para HTTP 400 (Bad Request) no {@code GlobalExceptionHandler}. Suporta dois modos de
 * uso:
 *
 * <ul>
 *   <li><strong>Mensagem simples:</strong> para validacoes de campo unico (ex: {@code new
 *       ValidacaoException("Nome e obrigatorio")})
 *   <li><strong>Mapa de erros por campo:</strong> para validacoes multiplas (ex: erros em
 *       formularios com diversos campos invalidos)
 * </ul>
 *
 * <p>Exemplo de uso com mensagem simples:
 *
 * <pre>{@code
 * throw new ValidacaoException("Nome do usuario e obrigatorio");
 * }</pre>
 *
 * <p>Exemplo de uso com mapa de erros:
 *
 * <pre>{@code
 * Map<String, String> erros = new HashMap<>();
 * erros.put("email", "Email invalido");
 * erros.put("cpf", "CPF deve conter 11 digitos");
 * throw new ValidacaoException("Dados invalidos", erros);
 * }</pre>
 *
 * @see DominioException
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class ValidacaoException extends DominioException {

    private final Map<String, String> errosPorCampo;

    /**
     * Cria uma excecao de validacao com mensagem simples e sem erros por campo.
     *
     * <p>Utilizado para validacoes de campo unico, como campos obrigatorios nulos ou vazios. O mapa
     * de erros por campo sera um mapa vazio imutavel.
     *
     * @param mensagem mensagem descritiva do erro de validacao
     */
    public ValidacaoException(String mensagem) {
        super(mensagem);
        this.errosPorCampo = Collections.emptyMap();
    }

    /**
     * Cria uma excecao de validacao com mensagem e mapa detalhado de erros por campo.
     *
     * <p>Utilizado quando multiplos campos possuem erros simultaneamente. O mapa e copiado
     * internamente para garantir imutabilidade (copia defensiva).
     *
     * @param mensagem mensagem geral descritiva do erro de validacao
     * @param errosPorCampo mapa de nome do campo para mensagem de erro especifica
     */
    public ValidacaoException(String mensagem, Map<String, String> errosPorCampo) {
        super(mensagem);
        this.errosPorCampo = new HashMap<>(errosPorCampo);
    }

    /**
     * Retorna o mapa imutavel de erros de validacao organizados por campo.
     *
     * <p>Cada chave do mapa e o nome do campo com erro e o valor e a mensagem de erro
     * correspondente. Retorna um mapa vazio (nao {@code null}) quando a excecao foi criada com o
     * construtor de mensagem simples.
     *
     * @return mapa imutavel de nome do campo para mensagem de erro (nunca {@code null})
     */
    public Map<String, String> getErrosPorCampo() {
        return Collections.unmodifiableMap(errosPorCampo);
    }
}
