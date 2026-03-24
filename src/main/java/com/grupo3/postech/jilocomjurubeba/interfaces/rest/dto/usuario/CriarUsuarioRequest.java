package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para criacao de um novo usuario.
 *
 * <p>Utilizado no endpoint {@code POST /v1/usuarios}. Os campos sao validados com Bean Validation
 * antes de serem convertidos para {@code CriarUsuarioInput} pelo {@code UsuarioRestMapper}.
 *
 * @param nome nome completo do usuario (obrigatorio, max 100 caracteres)
 * @param cpf CPF do usuario com somente numeros (obrigatorio, exatamente 11 caracteres). Deve ser
 *     unico no sistema
 * @param email email do usuario (obrigatorio, formato valido). Deve ser unico no sistema
 * @param telefone telefone do usuario (opcional, somente numeros)
 * @param tipoUsuarioId identificador do tipo de usuario associado (obrigatorio). Deve referenciar
 *     um TipoUsuario existente
 * @param senha senha de acesso (obrigatoria, minimo 6 caracteres)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados para criacao de um novo usuario")
public record CriarUsuarioRequest(
        @NotBlank(message = "Nome e obrigatorio")
                @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
                @Schema(description = "Nome completo do usuario", example = "Joao da Silva")
                String nome,
        @NotBlank(message = "CPF e obrigatorio")
                @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 caracteres")
                @Schema(description = "CPF do usuario (somente numeros)", example = "12345678901")
                String cpf,
        @NotBlank(message = "Email e obrigatorio")
                @Email(message = "Email deve ser valido")
                @Schema(description = "Email do usuario", example = "joao@email.com")
                String email,
        @Schema(description = "Telefone do usuario (opcional)", example = "11999998888")
                String telefone,
        @NotNull(message = "Tipo de usuario e obrigatorio")
                @Schema(description = "Identificador do tipo de usuario", example = "1")
                Long tipoUsuarioId,
        @NotBlank(message = "Senha e obrigatoria")
                @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
                @Schema(description = "Senha de acesso", example = "senha123")
                String senha) {}
