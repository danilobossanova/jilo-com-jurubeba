package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

/**
 * Caso de uso para desativacao (soft delete) de um usuario.
 *
 * <p>Nao remove fisicamente o registro do banco de dados. Marca o usuario como inativo atraves do
 * metodo {@code desativar()} da entidade de dominio {@link Usuario}, preservando o historico e
 * permitindo reativacao futura.
 *
 * <p>Na Clean Architecture, este caso de uso pertence a camada Application e depende apenas de
 * interfaces do Domain (gateways). Nao possui dependencia de framework.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class DeletarUsuarioUseCase implements UseCaseSemSaida<Long> {

    private final UsuarioGateway usuarioGateway;

    /**
     * Construtor com injecao do gateway de usuarios.
     *
     * @param usuarioGateway gateway de persistencia de usuarios
     */
    public DeletarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * Executa a desativacao (soft delete) de um usuario.
     *
     * <p>Busca o usuario pelo ID, invoca o metodo {@code desativar()} da entidade de dominio e
     * persiste o estado atualizado.
     *
     * @param id identificador unico do usuario a ser desativado
     * @throws EntidadeNaoEncontradaException se nenhum usuario for encontrado com o ID informado
     */
    @Override
    public void executar(Long id) {
        Usuario usuario =
                usuarioGateway
                        .buscarPorId(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));

        usuario.desativar();
        usuarioGateway.salvar(usuario);
    }
}
