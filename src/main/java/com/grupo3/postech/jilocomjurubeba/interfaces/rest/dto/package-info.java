/**
 * DTOs específicos da API REST.
 *
 * Request e Response objects que definem o contrato da API.
 * Separados dos DTOs de Application para permitir evolução independente.
 *
 * Convenções:
 * - [Ação][Entidade]Request.java (ex: CriarUsuarioRequest)
 * - [Entidade]Response.java (ex: UsuarioResponse)
 * - Usar Java Records
 * - Validações com Bean Validation (@NotNull, @Size, etc.)
 * - Documentação OpenAPI (@Schema)
 *
 * Estrutura por recurso:
 * - dto/
 *   - usuario/
 *     - CriarUsuarioRequest.java
 *     - AtualizarUsuarioRequest.java
 *     - UsuarioResponse.java
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto;
