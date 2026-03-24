package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para criacao de um novo restaurante.
 *
 * <p>Utilizado no endpoint {@code POST /v1/restaurantes}. Os campos sao validados com Bean
 * Validation antes de serem convertidos para {@code CriarRestauranteInput} pelo {@code
 * RestauranteRestMapper}.
 *
 * @param nome nome do restaurante (obrigatorio, max 100 caracteres)
 * @param endereco endereco completo do restaurante (obrigatorio, max 255 caracteres)
 * @param tipoCozinha tipo de cozinha do restaurante (obrigatorio, ex: BRASILEIRA, ITALIANA)
 * @param horaAbertura horario de abertura no formato HH:mm (obrigatorio)
 * @param horaFechamento horario de fechamento no formato HH:mm (obrigatorio)
 * @param donoId identificador do usuario dono do restaurante (obrigatorio). Deve referenciar um
 *     usuario com tipo DONO_RESTAURANTE
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados para criacao de um novo restaurante")
public record CriarRestauranteRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
                @Schema(description = "Nome do restaurante", example = "Restaurante Bom Sabor")
                String nome,
        @NotBlank(message = "Endereco e obrigatorio")
                @Size(max = 255, message = "Endereco deve ter no maximo 255 caracteres")
                @Schema(
                        description = "Endereco completo do restaurante",
                        example = "Rua das Flores, 123 - Centro")
                String endereco,
        @NotBlank(message = "Tipo de cozinha e obrigatorio")
                @Schema(description = "Tipo de cozinha do restaurante", example = "BRASILEIRA")
                String tipoCozinha,
        @NotNull(message = "Hora de abertura e obrigatoria")
                @Schema(description = "Horario de abertura", example = "08:00")
                LocalTime horaAbertura,
        @NotNull(message = "Hora de fechamento e obrigatoria")
                @Schema(description = "Horario de fechamento", example = "22:00")
                LocalTime horaFechamento,
        @NotNull(message = "Identificador do dono e obrigatorio")
                @Schema(description = "Identificador do usuario dono do restaurante", example = "1")
                Long donoId) {}
