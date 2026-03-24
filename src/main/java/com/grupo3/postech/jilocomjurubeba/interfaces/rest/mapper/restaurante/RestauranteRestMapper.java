package com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.restaurante;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante.AtualizarRestauranteRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante.CriarRestauranteRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante.RestauranteResponse;

/**
 * Mapper para conversao entre DTOs REST e DTOs Application de Restaurante.
 *
 * <p>Converte Request (interfaces) -> Input (application) e Output (application) -> Response
 * (interfaces). MapStruct gera a implementacao em tempo de compilacao.
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
@Mapper(componentModel = "spring")
public interface RestauranteRestMapper {

    /**
     * Converte Output do caso de uso para Response da API.
     *
     * @param output output do caso de uso contendo dados do restaurante
     * @return response para a API REST
     */
    RestauranteResponse toResponse(RestauranteOutput output);

    /**
     * Converte lista de Outputs para lista de Responses.
     *
     * @param outputs lista de outputs do caso de uso
     * @return lista de responses para a API REST
     */
    List<RestauranteResponse> toResponseList(List<RestauranteOutput> outputs);

    /**
     * Converte Request de criacao para Input do caso de uso.
     *
     * @param request request HTTP de criacao do restaurante
     * @return input para o caso de uso de criacao
     */
    CriarRestauranteInput toInput(CriarRestauranteRequest request);

    /**
     * Converte Request de atualizacao para Input, incluindo donoId para validacao de propriedade.
     *
     * @param id identificador do restaurante (path variable)
     * @param donoId identificador do usuario autenticado (extraido do JWT)
     * @param request dados da atualizacao
     * @return input para o caso de uso
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "donoId", target = "donoId")
    @Mapping(source = "request.nome", target = "nome")
    @Mapping(source = "request.endereco", target = "endereco")
    @Mapping(source = "request.tipoCozinha", target = "tipoCozinha")
    @Mapping(source = "request.horaAbertura", target = "horaAbertura")
    @Mapping(source = "request.horaFechamento", target = "horaFechamento")
    AtualizarRestauranteInput toInput(Long id, Long donoId, AtualizarRestauranteRequest request);
}
