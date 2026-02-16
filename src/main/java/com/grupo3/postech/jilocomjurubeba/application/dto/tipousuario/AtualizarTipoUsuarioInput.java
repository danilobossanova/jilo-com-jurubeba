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
 * @author Danilo Fernando
 */
public record AtualizarTipoUsuarioInput(Long id, String nome, String descricao) {}
