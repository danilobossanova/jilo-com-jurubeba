/**
 * Repositórios Spring Data.
 *
 * Interfaces que estendem JpaRepository ou MongoRepository.
 * O Spring Data gera a implementação automaticamente.
 *
 * Convenções:
 * - [Entidade]Repository.java (ex: UsuarioRepository)
 * - Estender JpaRepository<Entity, ID>
 * - Métodos de query personalizados quando necessário
 *
 * Exemplo:
 * <pre>
 * public interface UsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {
 *     Optional<UsuarioJpaEntity> findByEmail(String email);
 *     List<UsuarioJpaEntity> findByTipoUsuario(TipoUsuario tipo);
 * }
 * </pre>
 *
 * Nota: Repositórios são usados APENAS pelos Gateways, nunca diretamente.
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.repository;
