package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

/**
 * Caso de uso para busca de um usuario por identificador.
 *
 * <p>Este caso de uso consulta o {@link UsuarioGateway} para localizar um usuario pelo seu ID
 * unico. Caso o usuario nao seja encontrado, lanca uma excecao apropriada.
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
public class BuscarUsuarioUseCase implements UseCase<Long, UsuarioOutput> {

    private final UsuarioGateway usuarioGateway;

    /**
     * Construtor com injecao do gateway de usuarios.
     *
     * @param usuarioGateway gateway de persistencia de usuarios
     */
    public BuscarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * Executa a busca de um usuario pelo seu identificador.
     *
     * <p>Consulta o gateway e converte a entidade de dominio encontrada em DTO de saida.
     *
     * @param id identificador unico do usuario a ser buscado
     * @return {@link UsuarioOutput} com os dados do usuario encontrado
     * @throws EntidadeNaoEncontradaException se nenhum usuario for encontrado com o ID informado
     */
    @Override
    public UsuarioOutput executar(Long id) {

        return usuarioGateway
                .buscarPorId(id)
                .map(BuscarUsuarioUseCase::toOutput)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));
    }

    /**
     * Converte uma entidade de dominio {@link Usuario} para o DTO de saida.
     *
     * @param u entidade de dominio a ser convertida
     * @return {@link UsuarioOutput} com os dados da entidade
     */
    private static UsuarioOutput toOutput(Usuario u) {
        return new UsuarioOutput(
                u.getId(),
                u.getNome(),
                u.getCpf().getNumero(),
                u.getEmail().getEndereco(),
                u.getTelefone(),
                u.getTipoUsuario() != null ? u.getTipoUsuario().getNome() : null,
                u.isAtivo());
    }
}
