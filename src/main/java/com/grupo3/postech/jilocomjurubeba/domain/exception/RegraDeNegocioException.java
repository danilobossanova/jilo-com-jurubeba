package com.grupo3.postech.jilocomjurubeba.domain.exception;

/**
 * Excecao lancada quando uma regra de negocio e violada.
 *
 * <p>Mapeada para HTTP 422 (Unprocessable Entity) no {@code GlobalExceptionHandler}. Diferente da
 * {@link ValidacaoException} (que trata dados de entrada invalidos), esta excecao indica que os
 * dados sao sintaticamente validos mas violam uma regra de negocio do dominio.
 *
 * <p>Exemplos de situacoes que lancam esta excecao:
 *
 * <ul>
 *   <li>Tentativa de criar um tipo de usuario com nome duplicado
 *   <li>Tentativa de associar um restaurante a um usuario que nao e DONO_RESTAURANTE
 *   <li>Credenciais de autenticacao invalidas
 * </ul>
 *
 * <p>Exemplo de uso:
 *
 * <pre>{@code
 * throw new RegraDeNegocioException("Ja existe um tipo de usuario com o nome MASTER");
 * }</pre>
 *
 * @see DominioException
 * @see ValidacaoException
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class RegraDeNegocioException extends DominioException {

    /**
     * Cria uma excecao de regra de negocio com mensagem descritiva.
     *
     * @param mensagem mensagem descritiva da regra de negocio violada
     */
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }

    /**
     * Cria uma excecao de regra de negocio com mensagem descritiva e causa original.
     *
     * <p>Util para encadear excecoes quando a violacao da regra de negocio e resultado de um erro
     * em camada inferior (ex: falha de unicidade no banco de dados).
     *
     * @param mensagem mensagem descritiva da regra de negocio violada
     * @param causa excecao original que desencadeou a violacao
     */
    public RegraDeNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
