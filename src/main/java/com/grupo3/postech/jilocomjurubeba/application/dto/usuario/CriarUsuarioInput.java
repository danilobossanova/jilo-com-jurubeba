package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;

/**
 * DTO de entrada para criacao de um novo usuario.
 *
 * <p>Transporta os dados necessarios do Controller para o {@link
 * com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.CriarUsuarioUseCase}, sem acoplar
 * a camada Application ao formato HTTP (Request). A senha sera criptografada pelo caso de uso antes
 * da persistencia.
 *
 * @param nome nome completo do usuario
 * @param cpf CPF do usuario (sera validado como value object no dominio)
 * @param email endereco de email do usuario (sera validado como value object no dominio)
 * @param telefone numero de telefone do usuario
 * @param tipoUsuarioId identificador do tipo de usuario associado (MASTER, DONO_RESTAURANTE,
 *     CLIENTE)
 * @param senha senha em texto puro (sera criptografada antes da persistencia)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record CriarUsuarioInput(
        String nome, String cpf, String email, String telefone, Long tipoUsuarioId, String senha) {}
