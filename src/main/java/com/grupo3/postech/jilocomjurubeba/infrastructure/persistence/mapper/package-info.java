/**
 * Mappers de Persistência.
 *
 * Conversores entre entidades JPA e entidades de domínio.
 * Utilizamos MapStruct para geração automática.
 *
 * Convenções:
 * - [Entidade]PersistenceMapper.java (ex: UsuarioPersistenceMapper)
 * - Interface com @Mapper(componentModel = "spring")
 * - Métodos: toDomain(), toJpaEntity()
 *
 * Exemplo:
 * <pre>
 * {@literal @}Mapper(componentModel = "spring")
 * public interface UsuarioPersistenceMapper {
 *     Usuario toDomain(UsuarioJpaEntity entity);
 *     UsuarioJpaEntity toJpaEntity(Usuario domain);
 * }
 * </pre>
 *
 * Importante:
 * - Lidar com Value Objects na conversão
 * - Mapear relacionamentos corretamente
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.mapper;
