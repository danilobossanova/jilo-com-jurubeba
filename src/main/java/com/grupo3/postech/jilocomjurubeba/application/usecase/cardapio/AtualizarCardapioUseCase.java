package com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio;

import java.math.BigDecimal;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;

/**
 * Caso de uso para atualizacao de dados de um item de cardapio existente.
 *
 * <p>Este caso de uso orquestra o fluxo de atualizacao de item de cardapio:
 *
 * <ol>
 *   <li>Busca o item de cardapio pelo ID informado no input
 *   <li>Verifica se o usuario solicitante e o dono do restaurante ao qual o item pertence
 *   <li>Aplica atualizacao parcial: campos nulos no input preservam os valores atuais
 *   <li>Delega a atualizacao para o metodo {@code atualizarDados()} da entidade de dominio
 *   <li>Persiste a entidade atualizada via {@link CardapioGateway#salvar(Cardapio)}
 *   <li>Retorna o {@link CardapioOutput} com os dados atualizados
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
public class AtualizarCardapioUseCase implements UseCase<AtualizarCardapioInput, CardapioOutput> {

    private final CardapioGateway cardapioGateway;

    /**
     * Construtor com injecao do gateway de cardapio.
     *
     * @param cardapioGateway gateway de persistencia de itens de cardapio
     */
    public AtualizarCardapioUseCase(CardapioGateway cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    /**
     * Executa a atualizacao parcial de um item de cardapio existente.
     *
     * <p>Valida a propriedade do restaurante (apenas o dono pode atualizar seus itens) e aplica
     * atualizacao parcial: campos {@code null} no input preservam os valores atuais.
     *
     * @param input dados de entrada contendo o ID do item, ID do dono e campos a atualizar
     * @return {@link CardapioOutput} com os dados do item apos a atualizacao
     * @throws EntidadeNaoEncontradaException se nenhum item for encontrado com o ID informado
     * @throws RegraDeNegocioException se o usuario nao for o dono do restaurante ao qual o item
     *     pertence
     */
    @Override
    public CardapioOutput executar(AtualizarCardapioInput input) {

        Cardapio cardapio =
                cardapioGateway
                        .buscarPorId(input.id())
                        .orElseThrow(
                                () -> new EntidadeNaoEncontradaException("Cardapio", input.id()));

        Restaurante restaurante = cardapio.getRestaurante();
        if (restaurante == null || !restaurante.pertenceAoDono(input.donoId())) {
            throw new RegraDeNegocioException(
                    "Apenas o dono do restaurante pode atualizar o cardapio");
        }

        String nome = input.nome() != null ? input.nome() : cardapio.getNome();
        String descricao = input.descricao() != null ? input.descricao() : cardapio.getDescricao();
        BigDecimal preco = input.preco() != null ? input.preco() : cardapio.getPreco();
        String caminhoFoto =
                input.caminhoFoto() != null ? input.caminhoFoto() : cardapio.getCaminhoFoto();

        cardapio.atualizarDados(nome, descricao, preco, input.apenasNoLocal(), caminhoFoto);

        Cardapio atualizado = cardapioGateway.salvar(cardapio);

        return toOutput(atualizado);
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
