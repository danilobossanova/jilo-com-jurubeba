package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para atualizacao de um item de cardapio existente.
 *
 * <p>Utilizado no endpoint {@code PUT /v1/cardapios/{id}?donoId={donoId}}. Os campos sao validados
 * com Bean Validation antes de serem convertidos para {@code AtualizarCardapioInput} pelo {@code
 * CardapioRestMapper}. O {@code id} e {@code donoId} sao fornecidos via path variable e query
 * param.
 *
 * @param nome novo nome do item (obrigatorio, max 100 caracteres)
 * @param descricao nova descricao detalhada do item (opcional, max 500 caracteres)
 * @param preco novo preco do item em reais (deve ser maior que zero se informado)
 * @param apenasNoLocal indica se o item e servido apenas no local (sem delivery)
 * @param caminhoFoto novo caminho relativo da foto do item (opcional)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados para atualizacao de um item de cardapio existente")
public record AtualizarCardapioRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
                @Schema(description = "Novo nome do item", example = "Feijoada Especial")
                String nome,
        @Size(max = 500, message = "Descricao deve ter no maximo 500 caracteres")
                @Schema(
                        description = "Nova descricao do item",
                        example = "Feijoada especial com carnes nobres")
                String descricao,
        @Positive(message = "Preco deve ser maior que zero")
                @Schema(description = "Novo preco do item", example = "55.90")
                BigDecimal preco,
        @Schema(description = "Indica se o item e servido apenas no local", example = "true")
                boolean apenasNoLocal,
        @Schema(description = "Novo caminho da foto", example = "/imagens/feijoada-especial.jpg")
                String caminhoFoto) {}
