/**
 * Camada de Domínio - O coração da aplicação.
 *
 * <p>Esta camada contém: - Entidades de domínio (regras de negócio encapsuladas) - Value Objects
 * (objetos imutáveis que representam conceitos do domínio) - Interfaces de Gateway (portas para o
 * mundo externo) - Exceções de domínio - Enums de domínio
 *
 * <p>REGRAS IMPORTANTES: 1. NÃO pode depender de frameworks (Spring, JPA, etc.) 2. NÃO pode
 * depender de outras camadas (application, infrastructure, interfaces) 3. Deve ser 100% testável
 * sem mocks de framework 4. Entidades devem proteger seus invariantes
 *
 * @author Danilo Fernando
 * @see com.grupo3.postech.jilocomjurubeba.domain.entity Entidades de negócio
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway Interfaces para acesso externo
 * @see com.grupo3.postech.jilocomjurubeba.domain.exception Exceções de domínio
 */
package com.grupo3.postech.jilocomjurubeba.domain;
