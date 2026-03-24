package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para atualizacao de um tipo de usuario existente.
 *
 * <p>Utilizado no endpoint {@code PUT /v1/tipos-usuario/{id}}. Os campos sao validados com Bean
 * Validation antes de serem convertidos para {@code AtualizarTipoUsuarioInput} pelo {@code
 * TipoUsuarioRestMapper}. O {@code id} e fornecido via path variable.
 *
 * @param nome novo nome do tipo de usuario (obrigatorio, max 50 caracteres). Sera normalizado para
 *     UPPERCASE no dominio
 * @param descricao nova descricao do tipo de usuario (obrigatoria, max 255 caracteres)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
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
