/**
 * Camada de Persistência.
 *
 * <p>Implementações de acesso a dados usando Spring Data JPA/MongoDB. Os Gateways definidos no
 * domínio são implementados aqui.
 *
 * <p>Estrutura: - entity/: Entidades JPA (@Entity) - representação no banco - repository/:
 * Interfaces Spring Data (JpaRepository) - mapper/: Conversores entre entidades JPA e domínio -
 * gateway/: Implementações dos Gateways de domínio
 *
 * <p>Separação Entity vs Domain: - JpaEntity: mapeamento para tabelas (anotações JPA) - Domain
 * Entity: regras de negócio (sem anotações de framework)
 *
 * <p>Os mappers fazem a conversão entre as duas representações.
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence;
