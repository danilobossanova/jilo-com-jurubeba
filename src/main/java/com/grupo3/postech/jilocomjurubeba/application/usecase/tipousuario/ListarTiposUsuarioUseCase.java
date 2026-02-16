package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para listagem de todos os TiposUsuario.
 *
 * <p>Retorna todos os tipos de usuario cadastrados no sistema.
 *
 * @author Danilo Fernando
 */
public class ListarTiposUsuarioUseCase implements UseCaseSemEntrada<List<TipoUsuarioOutput>> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public ListarTiposUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public List<TipoUsuarioOutput> executar() {
        return tipoUsuarioGateway.listarTodos().stream().map(this::toOutput).toList();
    }

    private TipoUsuarioOutput toOutput(TipoUsuario tipoUsuario) {
        return new TipoUsuarioOutput(
                tipoUsuario.getId(),
                tipoUsuario.getNome(),
                tipoUsuario.getDescricao(),
                tipoUsuario.isAtivo());
    }
}
