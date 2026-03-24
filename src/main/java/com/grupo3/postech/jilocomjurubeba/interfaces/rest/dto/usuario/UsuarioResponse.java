package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO para representacao de um usuario na API REST.
 *
 * <p>Retornado pelos endpoints {@code GET}, {@code POST} e {@code PUT} de {@code /v1/usuarios}.
 * Convertido a partir de {@code UsuarioOutput} pelo {@code UsuarioRestMapper}.
 *
 * @param id identificador unico do usuario
 * @param nome nome completo do usuario
 * @param cpf CPF do usuario (somente numeros, 11 digitos)
 * @param email email do usuario
 * @param telefone telefone do usuario (pode ser nulo se nao informado)
 * @param tipoUsuario nome do tipo de usuario associado (ex: DONO_RESTAURANTE)
 * @param ativo indica se o usuario esta ativo ({@code false} apos soft delete)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Schema(description = "Dados de retorno de um usuario")
public record UsuarioResponse(
        @Schema(description = "Identificador unico", example = "1") Long id,
        @Schema(description = "Nome completo do usuario", example = "Joao da Silva") String nome,
        @Schema(description = "CPF do usuario", example = "12345678901") String cpf,
        @Schema(description = "Email do usuario", example = "joao@email.com") String email,
        @Schema(description = "Telefone do usuario", example = "11999998888") String telefone,
        @Schema(description = "Nome do tipo de usuario", example = "DONO_RESTAURANTE")
                String tipoUsuario,
        @Schema(description = "Indica se o usuario esta ativo", example = "true") boolean ativo) {}
