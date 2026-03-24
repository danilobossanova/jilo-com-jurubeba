package com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario;

/**
 * DTO de saida para TipoUsuario.
 *
 * <p>Representa os dados de um tipo de usuario retornados pelos casos de uso. E uma "foto" dos
 * dados da entidade, desacoplando a camada de application da entidade de dominio.
 *
 * @param id identificador unico
 * @param nome nome do tipo (ex: "MASTER")
 * @param descricao descricao legivel do tipo
 * @param ativo indica se o tipo esta ativo
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record TipoUsuarioOutput(Long id, String nome, String descricao, boolean ativo) {}
