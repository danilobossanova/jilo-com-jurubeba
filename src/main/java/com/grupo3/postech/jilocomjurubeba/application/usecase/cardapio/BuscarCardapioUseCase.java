package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;

/**
 * Caso de uso para busca de um item de cardapio por identificador.
 *
 * <p>Este caso de uso consulta o {@link CardapioGateway} para localizar um item de cardapio pelo
 * seu ID unico. Caso o item nao seja encontrado, lanca uma excecao apropriada.
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
public class BuscarCardapioUseCase implements UseCase<Long, CardapioOutput> {

    private final CardapioGateway cardapioGateway;

    /**
     * Construtor com injecao do gateway de cardapio.
     *
     * @param cardapioGateway gateway de persistencia de itens de cardapio
     */
    public BuscarCardapioUseCase(CardapioGateway cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    /**
     * Executa a busca de um item de cardapio pelo seu identificador.
     *
     * <p>Consulta o gateway e converte a entidade de dominio encontrada em DTO de saida.
     *
     * @param id identificador unico do item de cardapio a ser buscado
     * @return {@link CardapioOutput} com os dados do item encontrado
     * @throws EntidadeNaoEncontradaException se nenhum item for encontrado com o ID informado
     */
    @Override
    public CardapioOutput executar(Long id) {

        return cardapioGateway
                .buscarPorId(id)
                .map(BuscarCardapioUseCase::toOutput)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", id));
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
