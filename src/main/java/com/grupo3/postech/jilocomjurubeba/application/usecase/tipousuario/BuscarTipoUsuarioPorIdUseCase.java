package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para busca de TipoUsuario por identificador.
 *
 * <p>Este caso de uso consulta o {@link TipoUsuarioGateway} para localizar um tipo de usuario pelo
 * seu ID unico. Caso nao seja encontrado, lanca uma excecao apropriada.
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
public class BuscarTipoUsuarioPorIdUseCase implements UseCase<Long, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    /**
     * Construtor com injecao do gateway de tipos de usuario.
     *
     * @param tipoUsuarioGateway gateway de persistencia de tipos de usuario
     */
    public BuscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    /**
     * Executa a busca de um tipo de usuario pelo seu identificador.
     *
     * <p>Consulta o gateway e converte a entidade de dominio encontrada em DTO de saida.
     *
     * @param id identificador unico do tipo de usuario a ser buscado
     * @return {@link TipoUsuarioOutput} com os dados do tipo de usuario encontrado
     * @throws EntidadeNaoEncontradaException se nenhum tipo de usuario for encontrado com o ID
     *     informado
     */
    @Override
    public TipoUsuarioOutput executar(Long id) {
        TipoUsuario tipoUsuario =
                tipoUsuarioGateway
                        .buscarPorId(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("TipoUsuario", id));

        return new TipoUsuarioOutput(
                tipoUsuario.getId(),
                tipoUsuario.getNome(),
                tipoUsuario.getDescricao(),
                tipoUsuario.isAtivo());
    }
}
