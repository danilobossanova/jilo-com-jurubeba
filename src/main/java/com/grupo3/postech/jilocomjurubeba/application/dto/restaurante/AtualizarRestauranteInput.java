package com.grupo3.postech.jilocomjurubeba.application.dto.restaurante;

import java.time.LocalTime;

/**
 * DTO de entrada para atualizacao de restaurante.
 *
 * @param id identificador do restaurante
 * @param nome novo nome (opcional, null para manter)
 * @param endereco novo endereco (opcional)
 * @param tipoCozinha novo tipo de cozinha como String (opcional)
 * @param horaAbertura novo horario de abertura (opcional)
 * @param horaFechamento novo horario de fechamento (opcional)
 * @param donoId id do usuario que esta realizando a atualizacao (para validacao de propriedade)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AtualizarRestauranteInput(
        Long id,
        String nome,
        String endereco,
        String tipoCozinha,
        LocalTime horaAbertura,
        LocalTime horaFechamento,
        Long donoId) {}
