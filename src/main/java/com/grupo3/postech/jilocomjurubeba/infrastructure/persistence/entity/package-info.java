/**
 * Entidades JPA (mapeamento de banco de dados).
 *
 * <p>Classes anotadas com @Entity que representam tabelas no banco. São diferentes das entidades de
 * domínio (que não têm anotações JPA).
 *
 * <p>Convenções: - [Entidade]JpaEntity.java (ex: UsuarioJpaEntity) - Usar @Entity, @Table, @Column
 * - Não colocar lógica de negócio aqui - Mapear relacionamentos com JPA
 *
 * <p>Exemplo:
 *
 * <pre>
 * {@literal @}Entity
 * {@literal @}Table(name = "usuarios")
 * public class UsuarioJpaEntity {
 *     {@literal @}Id
 *     {@literal @}GeneratedValue(strategy = GenerationType.IDENTITY)
 *     private Long id;
 *
 *     {@literal @}Column(nullable = false)
 *     private String nome;
 *     // ...
 * }
 * </pre>
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.entity;
