package com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.tipousuario;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.AtualizarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.CriarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario.AtualizarTipoUsuarioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario.CriarTipoUsuarioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario.TipoUsuarioResponse;

/**
 * Mapper para conversao entre DTOs REST e DTOs Application de TipoUsuario.
 *
 * <p>Converte Request (interfaces) → Input (application) e Output (application) → Response
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
public interface TipoUsuarioRestMapper {

    /**
     * Converte Output do caso de uso para Response da API.
     *
     * @param output output do caso de uso
     * @return response para a API REST
     */
    TipoUsuarioResponse toResponse(TipoUsuarioOutput output);

    /**
     * Converte lista de Outputs para lista de Responses.
     *
     * @param outputs lista de outputs
     * @return lista de responses
     */
    List<TipoUsuarioResponse> toResponseList(List<TipoUsuarioOutput> outputs);

    /**
     * Converte Request de criacao para Input do caso de uso.
     *
     * @param request request HTTP de criacao
     * @return input para o caso de uso
     */
    CriarTipoUsuarioInput toInput(CriarTipoUsuarioRequest request);

    /**
     * Converte Request de atualizacao para Input do caso de uso, combinando o id da URL com os
     * dados do body.
     *
     * @param id identificador do path variable
     * @param request request HTTP de atualizacao
     * @return input para o caso de uso
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "request.nome", target = "nome")
    @Mapping(source = "request.descricao", target = "descricao")
    AtualizarTipoUsuarioInput toInput(Long id, AtualizarTipoUsuarioRequest request);
}
