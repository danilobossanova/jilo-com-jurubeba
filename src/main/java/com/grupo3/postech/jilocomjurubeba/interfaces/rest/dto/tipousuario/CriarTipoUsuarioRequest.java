package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para criacao de um novo tipo de usuario.
 *
 * <p>Utilizado no endpoint {@code POST /v1/tipos-usuario}. Os campos sao validados com Bean
 * Validation antes de serem convertidos para {@code CriarTipoUsuarioInput} pelo {@code
 * TipoUsuarioRestMapper}.
 *
 * @param nome nome unico do tipo de usuario (obrigatorio, max 50 caracteres). Sera normalizado para
 *     UPPERCASE no dominio
 * @param descricao descricao legivel do tipo de usuario (obrigatoria, max 255 caracteres)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
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
