package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import java.time.LocalTime;

/**
 * DTO de entrada para criacao de um novo restaurante.
 *
 * <p>Transporta os dados necessarios do Controller para o {@link
 * com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.CriarRestauranteUseCase}, sem
 * acoplar a camada Application ao formato HTTP (Request). O tipo de cozinha e recebido como String
 * e convertido para enum pelo caso de uso.
 *
 * @param nome nome do restaurante
 * @param endereco endereco completo do restaurante
 * @param tipoCozinha tipo de cozinha como String (sera convertido para enum TipoCozinha)
 * @param horaAbertura horario de abertura do restaurante
 * @param horaFechamento horario de fechamento do restaurante
 * @param donoId identificador do usuario dono do restaurante (deve ser DONO_RESTAURANTE)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record CriarRestauranteInput(
        String nome,
        String endereco,
        String tipoCozinha,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Long donoId) {}
