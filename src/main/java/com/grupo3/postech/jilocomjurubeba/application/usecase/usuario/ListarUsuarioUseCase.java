package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

/**
 * Caso de uso para listagem de todos os usuarios cadastrados no sistema.
 *
 * <p>Este caso de uso consulta o {@link UsuarioGateway} para recuperar todos os usuarios e os
 * converte para DTOs de saida. Nao aplica filtros ou paginacao, retornando todos os registros
 * existentes.
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
public class ListarUsuarioUseCase implements UseCaseSemEntrada<List<UsuarioOutput>> {

    private final UsuarioGateway usuarioGateway;

    /**
     * Construtor com injecao do gateway de usuarios.
     *
     * @param usuarioGateway gateway de persistencia de usuarios
     */
    public ListarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * Executa a listagem de todos os usuarios.
     *
     * <p>Recupera todas as entidades de dominio via gateway e converte cada uma para {@link
     * UsuarioOutput} usando stream e mapeamento interno.
     *
     * @return lista de {@link UsuarioOutput} com os dados de todos os usuarios cadastrados
     */
    @Override
    public List<UsuarioOutput> executar() {

        return usuarioGateway.listarTodos().stream().map(ListarUsuarioUseCase::toOutput).toList();
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
