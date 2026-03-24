package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import java.math.BigDecimal;

/**
 * DTO de saida para item de Cardapio.
 *
 * <p>Representa os dados de um item de cardapio retornados pelos casos de uso. E uma "foto"
 * imutavel dos dados da entidade de dominio, desacoplando a camada Application da entidade {@link
 * com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio}.
 *
 * @param id identificador unico do item de cardapio
 * @param nome nome do item (ex: "Feijoada Completa")
 * @param descricao descricao detalhada do item
 * @param preco preco do item em reais
 * @param apenasNoLocal indica se o item esta disponivel apenas para consumo no local
 * @param caminhoFoto caminho ou URL da foto do item
 * @param restauranteId identificador do restaurante ao qual o item pertence
 * @param ativo indica se o item esta ativo no sistema
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record CardapioOutput(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        boolean apenasNoLocal,
        String caminhoFoto,
        Long restauranteId,
        boolean ativo) {}
