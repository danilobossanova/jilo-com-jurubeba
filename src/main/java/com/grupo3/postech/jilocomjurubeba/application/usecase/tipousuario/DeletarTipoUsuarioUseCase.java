package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para desativacao (soft delete) de um TipoUsuario.
 *
 * <p>Nao remove fisicamente o registro do banco de dados. Marca o tipo de usuario como inativo
 * atraves do metodo {@code desativar()} da entidade de dominio {@link TipoUsuario}, preservando o
 * historico e permitindo reativacao futura.
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
public class DeletarTipoUsuarioUseCase implements UseCaseSemSaida<Long> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    /**
     * Construtor com injecao do gateway de tipos de usuario.
     *
     * @param tipoUsuarioGateway gateway de persistencia de tipos de usuario
     */
    public DeletarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    /**
     * Executa a desativacao (soft delete) de um tipo de usuario.
     *
     * <p>Busca o tipo de usuario pelo ID, invoca o metodo {@code desativar()} da entidade de
     * dominio e persiste o estado atualizado.
     *
     * @param id identificador unico do tipo de usuario a ser desativado
     * @throws EntidadeNaoEncontradaException se nenhum tipo de usuario for encontrado com o ID
     *     informado
     */
    @Override
    public void executar(Long id) {
        TipoUsuario tipoUsuario =
                tipoUsuarioGateway
                        .buscarPorId(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("TipoUsuario", id));

        tipoUsuario.desativar();
        tipoUsuarioGateway.salvar(tipoUsuario);
    }
}
