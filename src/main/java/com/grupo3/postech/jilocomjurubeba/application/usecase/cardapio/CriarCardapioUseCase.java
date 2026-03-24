package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

/**
 * Caso de uso para criacao de um novo item de cardapio no sistema.
 *
 * <p>Este caso de uso orquestra o fluxo de criacao de item de cardapio:
 *
 * <ol>
 *   <li>Busca o restaurante associado pelo ID informado via {@link RestauranteGateway}
 *   <li>Cria a entidade de dominio {@link Cardapio} (validacoes no construtor)
 *   <li>Persiste via {@link CardapioGateway#salvar(Cardapio)}
 *   <li>Retorna o {@link CardapioOutput} com os dados do item criado
 * </ol>
 *
 * <p>Na Clean Architecture, este caso de uso pertence a camada Application e depende apenas de
 * interfaces do Domain (gateways). Nao possui dependencia de framework.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class CriarCardapioUseCase implements UseCase<CriarCardapioInput, CardapioOutput> {

    private final CardapioGateway cardapioGateway;
    private final RestauranteGateway restauranteGateway;

    /**
     * Construtor com injecao dos gateways necessarios.
     *
     * @param cardapioGateway gateway de persistencia de itens de cardapio
     * @param restauranteGateway gateway de persistencia de restaurantes (para buscar o restaurante
     *     associado)
     */
    public CriarCardapioUseCase(
            CardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
        this.cardapioGateway = cardapioGateway;
        this.restauranteGateway = restauranteGateway;
    }

    /**
     * Executa a criacao de um novo item de cardapio.
     *
     * <p>Busca o restaurante associado pelo ID, cria a entidade de dominio com os dados fornecidos
     * e persiste no banco de dados.
     *
     * @param input dados de entrada contendo nome, descricao, preco, disponibilidade, foto e
     *     restaurante
     * @return {@link CardapioOutput} com os dados do item de cardapio criado
     * @throws EntidadeNaoEncontradaException se o restaurante associado nao for encontrado
     */
    @Override
    public CardapioOutput executar(CriarCardapioInput input) {

        Restaurante restaurante =
                restauranteGateway
                        .buscarPorId(input.restauranteId())
                        .orElseThrow(
                                () ->
                                        new EntidadeNaoEncontradaException(
                                                "Restaurante", input.restauranteId()));

        Cardapio cardapio =
                new Cardapio(
                        input.nome(),
                        input.descricao(),
                        input.preco(),
                        input.apenasNoLocal(),
                        input.caminhoFoto(),
                        restaurante);

        Cardapio salvo = cardapioGateway.salvar(cardapio);

        return toOutput(salvo);
    }

    /**
     * Converte uma entidade de dominio {@link Cardapio} para o DTO de saida.
     *
     * @param c entidade de dominio a ser convertida
     * @return {@link CardapioOutput} com os dados da entidade
     */
    private static CardapioOutput toOutput(Cardapio c) {
        return new CardapioOutput(
                c.getId(),
                c.getNome(),
                c.getDescricao(),
                c.getPreco(),
                c.isApenasNoLocal(),
                c.getCaminhoFoto(),
                c.getRestaurante() != null ? c.getRestaurante().getId() : null,
                c.isAtivo());
    }
}
