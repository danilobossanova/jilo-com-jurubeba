package com.grupo3.postech.jilocomjurubeba.application.dto.saude;

import java.time.LocalDateTime;

/**
 * Output do caso de uso de verificação de saúde.
 *
 * @param status    Status atual da aplicação (ex: "UP", "DOWN")
 * @param versao    Versão da aplicação
 * @param timestamp Data/hora da verificação
 *
 * @author Danilo Fernando
 */
public record SaudeOutput(
        String status,
        String versao,
        LocalDateTime timestamp
) {
    /**
     * Cria um SaudeOutput com status UP.
     *
     * @param versao versão da aplicação
     * @return SaudeOutput com status UP
     */
    public static SaudeOutput up(String versao) {
        return new SaudeOutput("UP", versao, LocalDateTime.now());
    }
}
