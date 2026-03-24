package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para atualizacao de um usuario existente.
 *
 * <p>Utilizado no endpoint {@code PUT /v1/usuarios/{id}}. Os campos sao validados com Bean
 * Validation antes de serem convertidos para {@code AtualizarUsuarioInput} pelo {@code
 * UsuarioRestMapper}. O {@code id} e fornecido via path variable.
 *
 * <p>CPF e tipo de usuario nao sao alteraveis apos a criacao.
 *
 * @param nome novo nome completo do usuario (obrigatorio, max 100 caracteres)
 * @param email novo email do usuario (formato valido). Deve ser unico no sistema
 * @param telefone novo telefone do usuario (opcional, somente numeros)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados para atualizacao de um usuario existente")
public record AtualizarUsuarioRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
                @Schema(description = "Novo nome do usuario", example = "Joao da Silva")
                String nome,
        @Email(message = "Email deve ser valido")
                @Schema(description = "Novo email do usuario", example = "joao@email.com")
                String email,
        @Schema(description = "Novo telefone do usuario", example = "11999998888")
                String telefone) {}
