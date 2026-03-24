package com.grupo3.postech.jilocomjurubeba.domain.exception;

/**
 * Excecao lancada quando uma entidade nao e encontrada no sistema.
 *
 * <p>Mapeada para HTTP 404 (Not Found) no {@code GlobalExceptionHandler}. Armazena o nome da
 * entidade e o identificador utilizado na busca para fornecer mensagens de erro contextualizadas ao
 * cliente da API.
 *
 * <p>A mensagem e gerada automaticamente no formato: {@code "[NomeEntidade] com identificador
 * '[id]' nao foi encontrado"}.
 *
 * <p>Exemplo de uso:
 *
 * <pre>{@code
 * throw new EntidadeNaoEncontradaException("Usuario", 42L);
 * // Mensagem: "Usuario com identificador '42' nao foi encontrado"
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
public class EntidadeNaoEncontradaException extends DominioException {

    private final String nomeEntidade;
    private final Object identificador;

    /**
     * Cria uma excecao indicando que uma entidade nao foi encontrada.
     *
     * <p>A mensagem de erro e gerada automaticamente com base nos parametros informados, no
     * formato: {@code "[nomeEntidade] com identificador '[identificador]' nao foi encontrado"}.
     *
     * @param nomeEntidade nome legivel da entidade buscada (ex: {@code "Usuario"}, {@code
     *     "Restaurante"})
     * @param identificador valor do identificador utilizado na busca (ex: {@code 42L}, {@code
     *     "MASTER"})
     */
    public EntidadeNaoEncontradaException(String nomeEntidade, Object identificador) {
        super(
                String.format(
                        "%s com identificador '%s' não foi encontrado",
                        nomeEntidade, identificador));
        this.nomeEntidade = nomeEntidade;
        this.identificador = identificador;
    }

    /**
     * Retorna o nome da entidade que nao foi encontrada.
     *
     * @return nome legivel da entidade (ex: {@code "Usuario"}, {@code "TipoUsuario"})
     */
    public String getNomeEntidade() {
        return nomeEntidade;
    }

    /**
     * Retorna o identificador utilizado na busca que nao retornou resultado.
     *
     * <p>O tipo do retorno e {@link Object} para suportar diferentes tipos de identificador (Long,
     * String, UUID, etc).
     *
     * @return valor do identificador utilizado na busca
     */
    public Object getIdentificador() {
        return identificador;
    }
}
