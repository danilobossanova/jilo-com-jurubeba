package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import java.time.LocalTime;

/**
 * DTO de saida para Restaurante.
 *
 * <p>Representa os dados de um restaurante retornados pelos casos de uso. E uma "foto" imutavel dos
 * dados da entidade de dominio, desacoplando a camada Application da entidade {@link
 * com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante}. O tipo de cozinha e
 * representado como String para facilitar a serializacao.
 *
 * @param id identificador unico do restaurante
 * @param nome nome do restaurante
 * @param endereco endereco completo do restaurante
 * @param tipoCozinha tipo de cozinha como String (ex: "BRASILEIRA", "JAPONESA")
 * @param horaAbertura horario de abertura do restaurante
 * @param horaFechamento horario de fechamento do restaurante
 * @param donoId identificador do usuario dono do restaurante
 * @param ativo indica se o restaurante esta ativo no sistema
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record RestauranteOutput(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Long donoId,
        boolean ativo) {}
