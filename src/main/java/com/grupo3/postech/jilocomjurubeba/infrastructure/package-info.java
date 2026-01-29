/**
 * Camada de Infrastructure - Adaptadores de Saída.
 *
 * <p>Esta camada contém as implementações concretas dos Gateways e configurações de framework.
 *
 * <p>Componentes: - persistence/: Implementações JPA/MongoDB dos Gateways - persistence/entity/:
 * Entidades JPA (@Entity) - persistence/repository/: Repositórios Spring Data -
 * persistence/mapper/: Conversores Entity <-> Domain - config/: Configurações Spring
 * (@Configuration)
 *
 * <p>REGRAS IMPORTANTES: 1. Pode depender de application e domain 2. NÃO pode depender de
 * interfaces 3. Implementa as interfaces de Gateway definidas no domínio 4. Entidades JPA são
 * diferentes das entidades de domínio
 *
 * <p>Padrão de nomenclatura: - [Entidade]JpaEntity.java para entidades JPA -
 * [Entidade]Repository.java para repositórios Spring Data - [Entidade]GatewayJpa.java para
 * implementações de Gateway
 *
 * @author Danilo Fernando
 * @see com.grupo3.postech.jilocomjurubeba.infrastructure.persistence Persistência
 * @see com.grupo3.postech.jilocomjurubeba.infrastructure.config Configurações
 */
package com.grupo3.postech.jilocomjurubeba.infrastructure;
