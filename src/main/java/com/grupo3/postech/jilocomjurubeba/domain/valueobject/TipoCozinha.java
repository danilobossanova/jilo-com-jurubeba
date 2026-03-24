package com.grupo3.postech.jilocomjurubeba.domain.valueobject;

/**
 * Enum que representa os tipos de cozinha disponiveis no sistema.
 *
 * <p>Utilizado para classificar restaurantes por tipo de culinaria. Cada restaurante possui
 * exatamente um tipo de cozinha associado, definido na criacao e atualizavel pelo metodo {@link
 * com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante#atualizarDados}.
 *
 * <p>Caso nenhuma das opcoes especificas se aplique, o valor {@link #OUTRA} deve ser utilizado como
 * categoria generica.
 *
 * @see com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public enum TipoCozinha {

    /** Cozinha brasileira (feijoada, churrasco, moqueca, etc). */
    BRASILEIRA,

    /** Cozinha italiana (massas, pizzas, risotos, etc). */
    ITALIANA,

    /** Cozinha japonesa (sushi, sashimi, ramen, etc). */
    JAPONESA,

    /** Cozinha mexicana (tacos, burritos, nachos, etc). */
    MEXICANA,

    /** Cozinha francesa (crepes, quiches, croissants, etc). */
    FRANCESA,

    /** Cozinha chinesa (dim sum, chow mein, kung pao, etc). */
    CHINESA,

    /** Cozinha indiana (curry, tandoori, biryani, etc). */
    INDIANA,

    /** Cozinha arabe (kebab, falafel, homus, etc). */
    ARABE,

    /** Cozinha americana (hamburguer, hot dog, barbecue, etc). */
    AMERICANA,

    /** Categoria generica para tipos de cozinha nao listados. */
    OUTRA
}
