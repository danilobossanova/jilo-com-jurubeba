package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO para TipoUsuario.
 *
 * @param id identificador unico
 * @param nome nome do tipo
 * @param descricao descricao legivel do tipo
 * @param ativo indica se o tipo esta ativo
 * @author Danilo Fernando
 */
@Schema(description = "Dados de retorno de um tipo de usuario")
public record TipoUsuarioResponse(
        @Schema(description = "Identificador unico", example = "1") Long id,
        @Schema(description = "Nome do tipo de usuario", example = "DONO_RESTAURANTE") String nome,
        @Schema(
                        description = "Descricao do tipo de usuario",
                        example = "Proprietario de restaurante")
                String descricao,
        @Schema(description = "Indica se o tipo esta ativo", example = "true") boolean ativo) {}
