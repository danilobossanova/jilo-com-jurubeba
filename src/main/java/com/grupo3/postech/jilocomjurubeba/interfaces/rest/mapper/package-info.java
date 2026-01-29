/**
 * Mappers REST - Conversores entre DTOs.
 *
 * Convertem entre DTOs da API REST e DTOs da camada Application.
 * Utilizamos MapStruct para geração automática de código.
 *
 * Convenções:
 * - [Entidade]RestMapper.java (ex: UsuarioRestMapper)
 * - Interface com @Mapper(componentModel = "spring")
 * - Métodos: toInput(), toResponse()
 *
 * Exemplo:
 * <pre>
 * {@literal @}Mapper(componentModel = "spring")
 * public interface UsuarioRestMapper {
 *     CriarUsuarioInput toInput(CriarUsuarioRequest request);
 *     UsuarioResponse toResponse(UsuarioOutput output);
 * }
 * </pre>
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper;
