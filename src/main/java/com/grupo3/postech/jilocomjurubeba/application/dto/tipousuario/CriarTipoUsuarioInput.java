package com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario;

/**
 * DTO de entrada para criacao de TipoUsuario.
 *
 * <p>Transporta os dados necessarios do Controller para o CriarTipoUsuarioUseCase, sem acoplar a
 * camada application ao formato HTTP (Request).
 *
 * @param nome nome do tipo a ser criado (ex: "DONO_RESTAURANTE")
 * @param descricao descricao legivel do tipo
 * @author Danilo Fernando
 */
public record CriarTipoUsuarioInput(String nome, String descricao) {}
