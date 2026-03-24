package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO para representacao de um restaurante na API REST.
 *
 * <p>Retornado pelos endpoints {@code GET}, {@code POST} e {@code PUT} de {@code /v1/restaurantes}.
 * Convertido a partir de {@code RestauranteOutput} pelo {@code RestauranteRestMapper}.
 *
 * @param id identificador unico do restaurante
 * @param nome nome do restaurante
 * @param endereco endereco completo do restaurante
 * @param tipoCozinha tipo de cozinha (ex: BRASILEIRA, ITALIANA)
 * @param horaAbertura horario de abertura no formato HH:mm
 * @param horaFechamento horario de fechamento no formato HH:mm
 * @param donoId identificador do usuario dono do restaurante
 * @param ativo indica se o restaurante esta ativo ({@code false} apos soft delete)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados de retorno de um restaurante")
public record RestauranteResponse(
        @Schema(description = "Identificador unico", example = "1") Long id,
        @Schema(description = "Nome do restaurante", example = "Restaurante Bom Sabor") String nome,
        @Schema(description = "Endereco do restaurante", example = "Rua das Flores, 123 - Centro")
                String endereco,
        @Schema(description = "Tipo de cozinha", example = "BRASILEIRA") String tipoCozinha,
        @Schema(description = "Horario de abertura", example = "08:00") LocalTime horaAbertura,
        @Schema(description = "Horario de fechamento", example = "22:00") LocalTime horaFechamento,
        @Schema(description = "Identificador do dono", example = "1") Long donoId,
        @Schema(description = "Indica se o restaurante esta ativo", example = "true")
                boolean ativo) {}
