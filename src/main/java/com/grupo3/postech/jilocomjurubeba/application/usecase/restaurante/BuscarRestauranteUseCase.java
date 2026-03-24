package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

/**
 * Caso de uso para busca de um restaurante por identificador.
 *
 * <p>Este caso de uso consulta o {@link RestauranteGateway} para localizar um restaurante pelo seu
 * ID unico. Caso o restaurante nao seja encontrado, lanca uma excecao apropriada.
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
public class BuscarRestauranteUseCase implements UseCase<Long, RestauranteOutput> {

    private final RestauranteGateway restauranteGateway;

    /**
     * Construtor com injecao do gateway de restaurantes.
     *
     * @param restauranteGateway gateway de persistencia de restaurantes
     */
    public BuscarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    /**
     * Executa a busca de um restaurante pelo seu identificador.
     *
     * <p>Consulta o gateway e converte a entidade de dominio encontrada em DTO de saida.
     *
     * @param id identificador unico do restaurante a ser buscado
     * @return {@link RestauranteOutput} com os dados do restaurante encontrado
     * @throws EntidadeNaoEncontradaException se nenhum restaurante for encontrado com o ID
     *     informado
     */
    @Override
    public RestauranteOutput executar(Long id) {

        return restauranteGateway
                .buscarPorId(id)
                .map(BuscarRestauranteUseCase::toOutput)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", id));
    }

    /**
     * Converte uma entidade de dominio {@link Restaurante} para o DTO de saida.
     *
     * @param r entidade de dominio a ser convertida
     * @return {@link RestauranteOutput} com os dados da entidade
     */
    private static RestauranteOutput toOutput(Restaurante r) {
        return new RestauranteOutput(
                r.getId(),
                r.getNome(),
                r.getEndereco(),
                r.getTipoCozinha() != null ? r.getTipoCozinha().name() : null,
                r.getHoraAbertura(),
                r.getHoraFechamento(),
                r.getDono() != null ? r.getDono().getId() : null,
                r.isAtivo());
    }
}
