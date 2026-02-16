/**
 * Eventos de Domínio (Domain Events).
 *
 * <p>Contém eventos que representam fatos significativos que ocorreram no domínio. Domain Events
 * são o mecanismo pelo qual as entidades comunicam que algo importante aconteceu, sem acoplar-se a
 * quem reagirá a esse fato.
 *
 * <p>Na Clean Architecture, Domain Events pertencem à camada mais interna (Entities) porque são
 * parte fundamental da linguagem ubíqua do domínio. Eles descrevem <strong>o que
 * aconteceu</strong>, não <strong>o que deve ser feito</strong>.
 *
 * <h2>Princípios</h2>
 *
 * <ul>
 *   <li><strong>Imutáveis:</strong> Uma vez criado, um evento não pode ser alterado (todos os
 *       campos {@code final})
 *   <li><strong>No passado:</strong> Nomeados no particípio passado (ex: {@code
 *       UsuarioCriadoEvent}, {@code PedidoConfirmadoEvent})
 *   <li><strong>Auto-contidos:</strong> Carregam todos os dados necessários para que consumidores
 *       processem o evento sem consultar outras fontes
 *   <li><strong>Sem dependência de framework:</strong> POJOs puros, sem anotações Spring, JPA ou
 *       qualquer biblioteca externa
 *   <li><strong>Timestamp obrigatório:</strong> Todo evento deve registrar quando ocorreu
 * </ul>
 *
 * <h2>Estrutura recomendada</h2>
 *
 * <pre>{@code
 * // Interface marcadora base para todos os eventos de domínio
 * public interface DominioEvent {
 *     LocalDateTime ocorridoEm();
 * }
 *
 * // Exemplo de evento concreto (Java Record)
 * public record RestauranteCriadoEvent(
 *     UUID restauranteId,
 *     String nome,
 *     LocalDateTime ocorridoEm
 * ) implements DominioEvent {
 *
 *     public RestauranteCriadoEvent(UUID restauranteId, String nome) {
 *         this(restauranteId, nome, LocalDateTime.now());
 *     }
 * }
 * }</pre>
 *
 * <h2>Fluxo na Clean Architecture</h2>
 *
 * <pre>
 * 1. Entidade executa ação de negócio e produz evento
 *    → Restaurante.criar() retorna RestauranteCriadoEvent
 *
 * 2. Use Case coleta o evento e delega ao publicador (port de saída)
 *    → CriarRestauranteUseCase chama EventPublisherGateway.publicar(evento)
 *
 * 3. Infrastructure implementa a publicação real
 *    → EventPublisherGatewaySpring usa ApplicationEventPublisher do Spring
 *
 * 4. Listeners na camada infrastructure reagem ao evento
 *    → EnviarEmailBoasVindasListener escuta RestauranteCriadoEvent
 * </pre>
 *
 * <p>Exemplos futuros:
 *
 * <ul>
 *   <li>{@code UsuarioCriadoEvent} - Quando um novo usuário é registrado
 *   <li>{@code RestauranteCriadoEvent} - Quando um restaurante é cadastrado
 *   <li>{@code CardapioAtualizadoEvent} - Quando o cardápio de um restaurante é modificado
 *   <li>{@code PedidoRealizadoEvent} - Quando um cliente faz um pedido
 *   <li>{@code AvaliacaoRegistradaEvent} - Quando uma avaliação é enviada
 * </ul>
 *
 * @author Danilo Fernando
 * @see com.grupo3.postech.jilocomjurubeba.domain.entity Entidades que produzem eventos
 * @see com.grupo3.postech.jilocomjurubeba.domain.gateway Ports de saída para publicação de eventos
 */
package com.grupo3.postech.jilocomjurubeba.domain.event;
