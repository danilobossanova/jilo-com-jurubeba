/**
 * Gateways (Portas) do Domínio.
 *
 * Interfaces que definem contratos para acesso a recursos externos.
 * A implementação fica na camada de infrastructure.
 *
 * Padrão de nomenclatura:
 * - [Entidade]Gateway.java (ex: UsuarioGateway, RestauranteGateway)
 *
 * Estas interfaces seguem o padrão Ports and Adapters (Hexagonal Architecture),
 * permitindo que o domínio defina O QUE precisa, sem saber COMO será implementado.
 *
 * Exemplos futuros:
 * - UsuarioGateway
 * - RestauranteGateway
 * - ItemCardapioGateway
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.domain.gateway;
