package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para atualizacao de TipoUsuario.
 *
 * @param nome novo nome do tipo
 * @param descricao nova descricao do tipo
 * @author Danilo Fernando
 */
@Schema(description = "Dados para atualizacao de um tipo de usuario existente")
public record AtualizarTipoUsuarioRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 50, message = "Nome deve ter no maximo 50 caracteres")
                @Schema(description = "Novo nome do tipo de usuario", example = "CLIENTE")
                String nome,
        @NotBlank(message = "Descricao e obrigatoria")
                @Size(max = 255, message = "Descricao deve ter no maximo 255 caracteres")
                @Schema(
                        description = "Nova descricao do tipo de usuario",
                        example = "Consumidor do restaurante")
                String descricao) {}
