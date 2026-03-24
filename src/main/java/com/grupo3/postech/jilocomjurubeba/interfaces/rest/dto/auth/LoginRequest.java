package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO para autenticacao de usuario via login.
 *
 * <p>Utilizado no endpoint {@code POST /auth/login}. Os campos sao validados com Bean Validation
 * antes de serem convertidos para {@code AutenticarUsuarioInput} no {@code AuthController}.
 *
 * @param email email do usuario registrado (obrigatorio, formato valido)
 * @param senha senha de acesso do usuario (obrigatoria)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados para autenticacao de usuario")
public record LoginRequest(
        @NotBlank(message = "Email e obrigatorio")
                @Email(message = "Email deve ser valido")
                @Schema(description = "Email do usuario", example = "admin@jilocomjurubeba.com")
                String email,
        @NotBlank(message = "Senha e obrigatoria")
                @Schema(description = "Senha do usuario", example = "admin123")
                String senha) {}
