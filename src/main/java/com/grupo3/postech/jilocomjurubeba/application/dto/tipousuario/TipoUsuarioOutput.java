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
 * @author Danilo Fernando
 */
public record TipoUsuarioOutput(Long id, String nome, String descricao, boolean ativo) {}
