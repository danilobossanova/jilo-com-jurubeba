package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

/**
 * Caso de uso para listagem de todos os restaurantes cadastrados no sistema.
 *
 * <p>Este caso de uso consulta o {@link RestauranteGateway} para recuperar todos os restaurantes e
 * os converte para DTOs de saida. Nao aplica filtros ou paginacao, retornando todos os registros
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
public class ListarRestauranteUseCase implements UseCaseSemEntrada<List<RestauranteOutput>> {

    private final RestauranteGateway restauranteGateway;

    /**
     * Construtor com injecao do gateway de restaurantes.
     *
     * @param restauranteGateway gateway de persistencia de restaurantes
     */
    public ListarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    /**
     * Executa a listagem de todos os restaurantes.
     *
     * <p>Recupera todas as entidades de dominio via gateway e converte cada uma para {@link
     * RestauranteOutput} usando stream e mapeamento interno.
     *
     * @return lista de {@link RestauranteOutput} com os dados de todos os restaurantes cadastrados
     */
    @Override
    public List<RestauranteOutput> executar() {

        return restauranteGateway.listarTodos().stream()
                .map(ListarRestauranteUseCase::toOutput)
                .toList();
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
