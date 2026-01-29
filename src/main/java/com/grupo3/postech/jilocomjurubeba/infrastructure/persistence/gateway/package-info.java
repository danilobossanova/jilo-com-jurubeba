/**
 * Implementações dos Gateways de Domínio.
 *
 * <p>Classes que implementam as interfaces de Gateway definidas no domínio, utilizando os
 * repositórios Spring Data.
 *
 * <p>Convenções: - [Entidade]GatewayJpa.java (ex: UsuarioGatewayJpa) - Implementa interface do
 * domínio (ex: UsuarioGateway) - Anotado com @Component ou @Repository
 *
 * <p>Responsabilidades: - Converter Domain Entity -> JPA Entity (para salvar) - Converter JPA
 * Entity -> Domain Entity (para retornar) - Usar repositório para operações de banco
 *
 * <p>Exemplo:
 *
 * <pre>
 * {@literal @}Component
 * public class UsuarioGatewayJpa implements UsuarioGateway {
 *     private final UsuarioRepository repository;
 *     private final UsuarioPersistenceMapper mapper;
 *
 *     {@literal @}Override
 *     public Usuario salvar(Usuario usuario) {
 *         UsuarioJpaEntity entity = mapper.toJpaEntity(usuario);
 *         UsuarioJpaEntity salvo = repository.save(entity);
 *         return mapper.toDomain(salvo);
 *     }
 * }
 * </pre>
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.gateway;
