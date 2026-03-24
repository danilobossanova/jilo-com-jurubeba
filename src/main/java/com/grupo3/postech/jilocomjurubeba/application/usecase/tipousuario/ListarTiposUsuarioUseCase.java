package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para listagem de todos os TiposUsuario cadastrados no sistema.
 *
 * <p>Este caso de uso consulta o {@link TipoUsuarioGateway} para recuperar todos os tipos de
 * usuario e os converte para DTOs de saida. Nao aplica filtros ou paginacao, retornando todos os
 * registros existentes (incluindo tipos padrao como MASTER, DONO_RESTAURANTE e CLIENTE).
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
public class ListarTiposUsuarioUseCase implements UseCaseSemEntrada<List<TipoUsuarioOutput>> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    /**
     * Construtor com injecao do gateway de tipos de usuario.
     *
     * @param tipoUsuarioGateway gateway de persistencia de tipos de usuario
     */
    public ListarTiposUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    /**
     * Executa a listagem de todos os tipos de usuario.
     *
     * <p>Recupera todas as entidades de dominio via gateway e converte cada uma para {@link
     * TipoUsuarioOutput} usando stream e mapeamento interno.
     *
     * @return lista de {@link TipoUsuarioOutput} com os dados de todos os tipos cadastrados
     */
    @Override
    public List<TipoUsuarioOutput> executar() {
        return tipoUsuarioGateway.listarTodos().stream().map(this::toOutput).toList();
    }

    /**
     * Converte uma entidade de dominio {@link TipoUsuario} para o DTO de saida.
     *
     * @param tipoUsuario entidade de dominio a ser convertida
     * @return {@link TipoUsuarioOutput} com os dados da entidade
     */
    private TipoUsuarioOutput toOutput(TipoUsuario tipoUsuario) {
        return new TipoUsuarioOutput(
                tipoUsuario.getId(),
                tipoUsuario.getNome(),
                tipoUsuario.getDescricao(),
                tipoUsuario.isAtivo());
    }
}
