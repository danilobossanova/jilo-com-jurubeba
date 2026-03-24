package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;

/**
 * DTO de entrada para atualizacao parcial de um usuario existente.
 *
 * <p>Transporta os dados necessarios do Controller para o {@link
 * com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.AtualizarUsuarioUseCase}. O campo
 * {@code id} vem do path variable da URL e os demais campos vem do body do request. Campos com
 * valor {@code null} preservam os valores atuais do usuario (atualizacao parcial).
 *
 * @param id identificador unico do usuario a ser atualizado
 * @param nome novo nome do usuario (opcional, null para manter o atual)
 * @param email novo email do usuario (opcional, null para manter o atual)
 * @param telefone novo telefone do usuario (opcional, null para manter o atual)
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AtualizarUsuarioInput(Long id, String nome, String email, String telefone) {}
