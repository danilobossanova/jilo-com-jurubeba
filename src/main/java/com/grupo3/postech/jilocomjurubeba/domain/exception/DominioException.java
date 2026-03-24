package com.grupo3.postech.jilocomjurubeba.domain.exception;

/**
 * Excecao base abstrata para todas as excecoes de dominio.
 *
 * <p>Esta classe serve como raiz da hierarquia de excecoes de dominio, permitindo tratamento
 * centralizado no {@code GlobalExceptionHandler} (ControllerAdvice) usando RFC 7807 {@code
 * ProblemDetail}.
 *
 * <p>Estende {@link RuntimeException} para permitir lancamento sem declaracao explicita na
 * assinatura dos metodos, seguindo o padrao de excecoes unchecked comum em camadas de dominio.
 *
 * <p>Subclasses concretas:
 *
 * <ul>
 *   <li>{@link ValidacaoException} - dados de entrada invalidos (HTTP 400)
 *   <li>{@link EntidadeNaoEncontradaException} - entidade nao encontrada (HTTP 404)
 *   <li>{@link RegraDeNegocioException} - violacao de regra de negocio (HTTP 422)
 * </ul>
 *
 * @see ValidacaoException
 * @see EntidadeNaoEncontradaException
 * @see RegraDeNegocioException
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public abstract class DominioException extends RuntimeException {

    /**
     * Cria uma nova excecao de dominio com a mensagem descritiva.
     *
     * @param mensagem mensagem descritiva do erro ocorrido
     */
    protected DominioException(String mensagem) {
        super(mensagem);
    }

    /**
     * Cria uma nova excecao de dominio com mensagem descritiva e causa original.
     *
     * <p>Util para encadear excecoes, preservando a causa raiz do erro.
     *
     * @param mensagem mensagem descritiva do erro ocorrido
     * @param causa excecao original que causou este erro
     */
    protected DominioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
