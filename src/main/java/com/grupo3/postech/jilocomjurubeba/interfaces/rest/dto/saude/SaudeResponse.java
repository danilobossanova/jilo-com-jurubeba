package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.saude;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO para endpoint de saúde.
 *
 * @param status Status da aplicação
 * @param versao Versão da aplicação
 * @param timestamp Data/hora da verificação
 * @author Danilo Fernando
 */
@Schema(description = "Resposta do endpoint de verificação de saúde")
public record SaudeResponse(
        @Schema(description = "Status da aplicação", example = "UP") String status,
        @Schema(description = "Versão da aplicação", example = "1.0.0") String versao,
        @Schema(description = "Data/hora da verificação", example = "2024-01-15T10:30:00")
                LocalDateTime timestamp) {}
