package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO para representacao de um tipo de usuario na API REST.
 *
 * <p>Retornado pelos endpoints {@code GET}, {@code POST} e {@code PUT} de {@code
 * /v1/tipos-usuario}. Convertido a partir de {@code TipoUsuarioOutput} pelo {@code
 * TipoUsuarioRestMapper}.
 *
 * @param id identificador unico do tipo de usuario
 * @param nome nome do tipo de usuario (ex: MASTER, DONO_RESTAURANTE, CLIENTE)
 * @param descricao descricao legivel do tipo de usuario
 * @param ativo indica se o tipo de usuario esta ativo ({@code false} apos soft delete)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
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
