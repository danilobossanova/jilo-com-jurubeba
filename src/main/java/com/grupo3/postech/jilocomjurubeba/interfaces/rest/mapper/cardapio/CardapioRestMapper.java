package com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.cardapio;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio.AtualizarCardapioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio.CardapioResponse;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio.CriarCardapioRequest;

/**
 * Mapper para conversao entre DTOs REST e DTOs Application de Cardapio.
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
public interface CardapioRestMapper {

    /**
     * Converte Output do caso de uso para Response da API.
     *
     * @param output output do caso de uso contendo dados do item de cardapio
     * @return response para a API REST
     */
    CardapioResponse toResponse(CardapioOutput output);

    /**
     * Converte lista de Outputs para lista de Responses.
     *
     * @param outputs lista de outputs do caso de uso
     * @return lista de responses para a API REST
     */
    List<CardapioResponse> toResponseList(List<CardapioOutput> outputs);

    /**
     * Converte Request de criacao para Input do caso de uso.
     *
     * @param request request HTTP de criacao do item de cardapio
     * @return input para o caso de uso de criacao
     */
    CriarCardapioInput toInput(CriarCardapioRequest request);

    /**
     * Converte Request de atualizacao para Input, incluindo donoId para validacao de propriedade.
     *
     * @param id identificador do item (path variable)
     * @param donoId identificador do usuario autenticado (extraido do JWT)
     * @param request dados da atualizacao
     * @return input para o caso de uso
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "donoId", target = "donoId")
    @Mapping(source = "request.nome", target = "nome")
    @Mapping(source = "request.descricao", target = "descricao")
    @Mapping(source = "request.preco", target = "preco")
    @Mapping(source = "request.apenasNoLocal", target = "apenasNoLocal")
    @Mapping(source = "request.caminhoFoto", target = "caminhoFoto")
    AtualizarCardapioInput toInput(Long id, Long donoId, AtualizarCardapioRequest request);
}
