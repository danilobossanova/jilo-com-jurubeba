package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import java.math.BigDecimal;

/**
 * DTO de entrada para atualizacao de item de cardapio.
 *
 * @param id identificador do item
 * @param nome novo nome (opcional, null para manter)
 * @param descricao nova descricao (opcional)
 * @param preco novo preco (opcional)
 * @param apenasNoLocal nova disponibilidade
 * @param caminhoFoto novo caminho da foto (opcional)
 * @param donoId id do usuario que esta realizando a atualizacao (para validacao de propriedade)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AtualizarCardapioInput(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        boolean apenasNoLocal,
        String caminhoFoto,
        Long donoId) {}
