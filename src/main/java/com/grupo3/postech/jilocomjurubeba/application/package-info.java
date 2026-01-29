/**
 * Camada de Application - Casos de Uso.
 *
 * <p>Esta camada orquestra o fluxo de dados entre as interfaces e o domínio. Contém a lógica de
 * aplicação (não confundir com lógica de negócio).
 *
 * <p>Componentes: - UseCase: Classes que implementam casos de uso específicos - DTO: Objetos de
 * transferência de dados (entrada/saída) - Mapper: Conversores entre DTOs e entidades de domínio
 *
 * <p>REGRAS IMPORTANTES: 1. Pode depender APENAS da camada domain 2. NÃO pode depender de
 * infrastructure nem interfaces 3. NÃO deve conhecer detalhes de frameworks (HTTP, JPA, etc.) 4.
 * Cada UseCase deve ter responsabilidade única
 *
 * <p>Padrão de nomenclatura: - [Ação][Entidade]UseCase.java (ex: CriarUsuarioUseCase,
 * BuscarRestauranteUseCase) - [Entidade]Input.java para entrada - [Entidade]Output.java para saída
 *
 * @author Danilo Fernando
 * @see com.grupo3.postech.jilocomjurubeba.application.usecase Implementações de casos de uso
 * @see com.grupo3.postech.jilocomjurubeba.application.dto Objetos de transferência
 */
package com.grupo3.postech.jilocomjurubeba.application;
