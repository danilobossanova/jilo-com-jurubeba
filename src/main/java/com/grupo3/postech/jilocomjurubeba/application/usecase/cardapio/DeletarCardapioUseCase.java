package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;

/**
 * Caso de uso para desativacao (soft delete) de um item de cardapio.
 *
 * <p>Nao remove fisicamente o registro do banco de dados. Marca o item como inativo atraves do
 * metodo {@code desativar()} da entidade de dominio {@link Cardapio}, preservando o historico e
 * permitindo reativacao futura.
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
public class DeletarCardapioUseCase implements UseCaseSemSaida<Long> {

    private final CardapioGateway cardapioGateway;

    /**
     * Construtor com injecao do gateway de cardapio.
     *
     * @param cardapioGateway gateway de persistencia de itens de cardapio
     */
    public DeletarCardapioUseCase(CardapioGateway cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    /**
     * Executa a desativacao (soft delete) de um item de cardapio.
     *
     * <p>Busca o item pelo ID, invoca o metodo {@code desativar()} da entidade de dominio e
     * persiste o estado atualizado.
     *
     * @param id identificador unico do item de cardapio a ser desativado
     * @throws EntidadeNaoEncontradaException se nenhum item for encontrado com o ID informado
     */
    @Override
    public void executar(Long id) {
        Cardapio cardapio =
                cardapioGateway
                        .buscarPorId(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Cardapio", id));

        cardapio.desativar();
        cardapioGateway.salvar(cardapio);
    }
}
