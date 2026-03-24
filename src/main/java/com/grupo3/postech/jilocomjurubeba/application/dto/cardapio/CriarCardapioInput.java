package com.grupo3.postech.jilocomjurubeba.application.dto.cardapio;

import java.math.BigDecimal;

/**
 * DTO de entrada para criacao de um novo item de cardapio.
 *
 * <p>Transporta os dados necessarios do Controller para o {@link
 * com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.CriarCardapioUseCase}, sem
 * acoplar a camada Application ao formato HTTP (Request).
 *
 * @param nome nome do item de cardapio (ex: "Feijoada Completa")
 * @param descricao descricao detalhada do item
 * @param preco preco do item em reais
 * @param apenasNoLocal indica se o item esta disponivel apenas para consumo no local
 * @param caminhoFoto caminho ou URL da foto do item (pode ser nulo)
 * @param restauranteId identificador do restaurante ao qual o item pertence
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record CriarCardapioInput(
        String nome,
        String descricao,
        BigDecimal preco,
        boolean apenasNoLocal,
        String caminhoFoto,
        Long restauranteId) {}
