package com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario;

/**
 * DTO de entrada para atualizacao de TipoUsuario.
 *
 * <p>Transporta os dados necessarios do Controller para o AtualizarTipoUsuarioUseCase. O campo
 * {@code id} vem do path variable da URL e o {@code nome}/{@code descricao} vem do body do request.
 * O Controller monta esse Input combinando ambos.
 *
 * @param id identificador do tipo a ser atualizado
 * @param nome novo nome do tipo
 * @param descricao nova descricao do tipo
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record AtualizarTipoUsuarioInput(Long id, String nome, String descricao) {}
