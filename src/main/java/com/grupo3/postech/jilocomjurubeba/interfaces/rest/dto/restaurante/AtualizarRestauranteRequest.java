package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para atualizacao de um restaurante existente.
 *
 * <p>Utilizado no endpoint {@code PUT /v1/restaurantes/{id}?donoId={donoId}}. Os campos sao
 * validados com Bean Validation antes de serem convertidos para {@code AtualizarRestauranteInput}
 * pelo {@code RestauranteRestMapper}. O {@code id} e {@code donoId} sao fornecidos via path
 * variable e query param.
 *
 * @param nome novo nome do restaurante (obrigatorio, max 100 caracteres)
 * @param endereco novo endereco do restaurante (obrigatorio, max 255 caracteres)
 * @param tipoCozinha novo tipo de cozinha (ex: BRASILEIRA, ITALIANA, JAPONESA)
 * @param horaAbertura novo horario de abertura no formato HH:mm
 * @param horaFechamento novo horario de fechamento no formato HH:mm
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados para atualizacao de um restaurante existente")
public record AtualizarRestauranteRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
                @Schema(description = "Novo nome do restaurante", example = "Restaurante Bom Sabor")
                String nome,
        @NotBlank(message = "Endereco e obrigatorio")
                @Size(max = 255, message = "Endereco deve ter no maximo 255 caracteres")
                @Schema(
                        description = "Novo endereco do restaurante",
                        example = "Rua das Flores, 456 - Centro")
                String endereco,
        @Schema(description = "Novo tipo de cozinha", example = "ITALIANA") String tipoCozinha,
        @Schema(description = "Novo horario de abertura", example = "09:00") LocalTime horaAbertura,
        @Schema(description = "Novo horario de fechamento", example = "23:00")
                LocalTime horaFechamento) {}
