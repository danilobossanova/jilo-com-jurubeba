package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;

/**
 * Caso de uso para listagem de todos os itens de cardapio cadastrados no sistema.
 *
 * <p>Este caso de uso consulta o {@link CardapioGateway} para recuperar todos os itens de cardapio
 * e os converte para DTOs de saida. Nao aplica filtros ou paginacao, retornando todos os registros
 * existentes.
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
public class ListarCardapioUseCase implements UseCaseSemEntrada<List<CardapioOutput>> {

    private final CardapioGateway cardapioGateway;

    /**
     * Construtor com injecao do gateway de cardapio.
     *
     * @param cardapioGateway gateway de persistencia de itens de cardapio
     */
    public ListarCardapioUseCase(CardapioGateway cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    /**
     * Executa a listagem de todos os itens de cardapio.
     *
     * <p>Recupera todas as entidades de dominio via gateway e converte cada uma para {@link
     * CardapioOutput} usando stream e mapeamento interno.
     *
     * @return lista de {@link CardapioOutput} com os dados de todos os itens cadastrados
     */
    @Override
    public List<CardapioOutput> executar() {

        return cardapioGateway.listarTodos().stream().map(ListarCardapioUseCase::toOutput).toList();
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
