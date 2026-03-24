package com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario;

/**
 * DTO de entrada para criacao de TipoUsuario.
 *
 * <p>Transporta os dados necessarios do Controller para o CriarTipoUsuarioUseCase, sem acoplar a
 * camada application ao formato HTTP (Request).
 *
 * @param nome nome do tipo a ser criado (ex: "DONO_RESTAURANTE")
 * @param descricao descricao legivel do tipo
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record CriarTipoUsuarioInput(String nome, String descricao) {}
