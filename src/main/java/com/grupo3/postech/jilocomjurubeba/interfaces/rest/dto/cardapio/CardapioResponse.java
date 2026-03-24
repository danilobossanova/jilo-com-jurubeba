package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO para representacao de um item de cardapio na API REST.
 *
 * <p>Retornado pelos endpoints {@code GET}, {@code POST} e {@code PUT} de {@code /v1/cardapios}.
 * Convertido a partir de {@code CardapioOutput} pelo {@code CardapioRestMapper}.
 *
 * @param id identificador unico do item de cardapio
 * @param nome nome do item do cardapio
 * @param descricao descricao detalhada do item
 * @param preco preco do item em reais
 * @param apenasNoLocal indica se o item e servido apenas no local (sem delivery)
 * @param caminhoFoto caminho relativo da foto do item (pode ser nulo)
 * @param restauranteId identificador do restaurante ao qual o item pertence
 * @param ativo indica se o item de cardapio esta ativo ({@code false} apos soft delete)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados de retorno de um item de cardapio")
public record CardapioResponse(
        @Schema(description = "Identificador unico", example = "1") Long id,
        @Schema(description = "Nome do item do cardapio", example = "Feijoada Completa")
                String nome,
        @Schema(
                        description = "Descricao do item",
                        example = "Feijoada completa com arroz, couve e farofa")
                String descricao,
        @Schema(description = "Preco do item", example = "45.90") BigDecimal preco,
        @Schema(description = "Indica se o item e servido apenas no local", example = "false")
                boolean apenasNoLocal,
        @Schema(description = "Caminho da foto do item", example = "/imagens/feijoada.jpg")
                String caminhoFoto,
        @Schema(description = "Identificador do restaurante", example = "1") Long restauranteId,
        @Schema(description = "Indica se o item esta ativo", example = "true") boolean ativo) {}
