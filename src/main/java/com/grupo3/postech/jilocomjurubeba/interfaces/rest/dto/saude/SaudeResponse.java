package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.saude;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO para representacao do estado de saude da aplicacao na API REST.
 *
 * <p>Retornado pelo endpoint publico {@code GET /v1/health}. Convertido a partir de {@code
 * SaudeOutput} pelo {@code SaudeRestMapper}. Utilizado por load balancers, Kubernetes e ferramentas
 * de monitoramento para verificar a disponibilidade do servico.
 *
 * @param status status da aplicacao (ex: UP, DOWN)
 * @param versao versao atual da aplicacao (ex: 1.0.0)
 * @param timestamp data e hora da verificacao de saude
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Resposta do endpoint de verificação de saúde")
public record SaudeResponse(
        @Schema(description = "Status da aplicação", example = "UP") String status,
        @Schema(description = "Versão da aplicação", example = "1.0.0") String versao,
        @Schema(description = "Data/hora da verificação", example = "2024-01-15T10:30:00")
                LocalDateTime timestamp) {}
