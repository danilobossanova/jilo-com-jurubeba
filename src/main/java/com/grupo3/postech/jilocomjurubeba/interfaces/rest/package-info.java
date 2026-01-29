/**
 * Controllers REST da API.
 *
 * <p>Endpoints HTTP que expõem a API do sistema. Cada controller deve ser responsável por um
 * recurso/entidade.
 *
 * <p>Convenções: - [Entidade]Controller.java (ex: UsuarioController) - Anotações OpenAPI
 * (@Tag, @Operation, @ApiResponse) - Versionamento no path: /v1/[recurso] - Métodos HTTP semânticos
 * (GET, POST, PUT, DELETE)
 *
 * <p>Responsabilidades: - Receber requisição HTTP - Validar entrada (@Valid) - Converter Request ->
 * Input - Chamar UseCase - Converter Output -> Response - Retornar resposta HTTP apropriada
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.interfaces.rest;
