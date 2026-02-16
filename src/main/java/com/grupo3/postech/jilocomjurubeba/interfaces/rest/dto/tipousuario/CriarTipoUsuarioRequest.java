package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para criacao de TipoUsuario.
 *
 * @param nome nome do tipo a ser criado
 * @param descricao descricao legivel do tipo
 * @author Danilo Fernando
 */
@Schema(description = "Dados para criacao de um novo tipo de usuario")
public record CriarTipoUsuarioRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 50, message = "Nome deve ter no maximo 50 caracteres")
                @Schema(description = "Nome unico do tipo de usuario", example = "DONO_RESTAURANTE")
                String nome,
        @NotBlank(message = "Descricao e obrigatoria")
                @Size(max = 255, message = "Descricao deve ter no maximo 255 caracteres")
                @Schema(
                        description = "Descricao do tipo de usuario",
                        example = "Proprietario de restaurante")
                String descricao) {}
