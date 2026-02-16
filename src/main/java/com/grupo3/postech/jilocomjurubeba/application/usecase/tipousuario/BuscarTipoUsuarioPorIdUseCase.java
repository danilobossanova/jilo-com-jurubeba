package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para busca de TipoUsuario por identificador.
 *
 * <p>Busca um tipo de usuario pelo id. Lanca excecao se nao encontrado.
 *
 * @author Danilo Fernando
 */
public class BuscarTipoUsuarioPorIdUseCase implements UseCase<Long, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public BuscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

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
