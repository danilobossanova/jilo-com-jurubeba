package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para criacao de um novo item de cardapio.
 *
 * <p>Utilizado no endpoint {@code POST /v1/cardapios}. Os campos sao validados com Bean Validation
 * antes de serem convertidos para {@code CriarCardapioInput} pelo {@code CardapioRestMapper}.
 *
 * @param nome nome do item do cardapio (obrigatorio, max 100 caracteres)
 * @param descricao descricao detalhada do item (opcional, max 500 caracteres)
 * @param preco preco do item em reais (obrigatorio, deve ser maior que zero)
 * @param apenasNoLocal indica se o item e servido apenas no local (sem delivery)
 * @param caminhoFoto caminho relativo da foto do item (opcional)
 * @param restauranteId identificador do restaurante ao qual o item pertence (obrigatorio). Deve
 *     referenciar um restaurante existente
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados para criacao de um novo item de cardapio")
public record CriarCardapioRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
                @Schema(description = "Nome do item do cardapio", example = "Feijoada Completa")
                String nome,
        @Size(max = 500, message = "Descricao deve ter no maximo 500 caracteres")
                @Schema(
                        description = "Descricao do item",
                        example = "Feijoada completa com arroz, couve e farofa")
                String descricao,
        @NotNull(message = "Preco e obrigatorio")
                @Positive(message = "Preco deve ser maior que zero")
                @Schema(description = "Preco do item", example = "45.90")
                BigDecimal preco,
        @Schema(description = "Indica se o item e servido apenas no local", example = "false")
                boolean apenasNoLocal,
        @Schema(description = "Caminho da foto do item", example = "/imagens/feijoada.jpg")
                String caminhoFoto,
        @NotNull(message = "Identificador do restaurante e obrigatorio")
                @Schema(description = "Identificador do restaurante", example = "1")
                Long restauranteId) {}
