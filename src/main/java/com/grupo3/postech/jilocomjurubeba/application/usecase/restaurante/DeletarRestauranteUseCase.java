package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

/**
 * Caso de uso para desativacao (soft delete) de um restaurante.
 *
 * <p>Nao remove fisicamente o registro do banco de dados. Marca o restaurante como inativo atraves
 * do metodo {@code desativar()} da entidade de dominio {@link Restaurante}, preservando o historico
 * e permitindo reativacao futura.
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
public class DeletarRestauranteUseCase implements UseCaseSemSaida<Long> {

    private final RestauranteGateway restauranteGateway;

    /**
     * Construtor com injecao do gateway de restaurantes.
     *
     * @param restauranteGateway gateway de persistencia de restaurantes
     */
    public DeletarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    /**
     * Executa a desativacao (soft delete) de um restaurante.
     *
     * <p>Busca o restaurante pelo ID, invoca o metodo {@code desativar()} da entidade de dominio e
     * persiste o estado atualizado.
     *
     * @param id identificador unico do restaurante a ser desativado
     * @throws EntidadeNaoEncontradaException se nenhum restaurante for encontrado com o ID
     *     informado
     */
    @Override
    public void executar(Long id) {
        Restaurante restaurante =
                restauranteGateway
                        .buscarPorId(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", id));

        restaurante.desativar();
        restauranteGateway.salvar(restaurante);
    }
}
