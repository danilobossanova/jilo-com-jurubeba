package com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper;

import org.mapstruct.Mapper;

import com.grupo3.postech.jilocomjurubeba.application.dto.saude.SaudeOutput;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.saude.SaudeResponse;

/**
 * Mapper para conversão entre DTOs de saúde.
 *
 * <p>Converte SaudeOutput (application) para SaudeResponse (interfaces/rest).
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
public interface SaudeRestMapper {

    /**
     * Converte output do caso de uso para response da API.
     *
     * @param output output do caso de uso
     * @return response para a API REST
     */
    SaudeResponse toResponse(SaudeOutput output);
}
