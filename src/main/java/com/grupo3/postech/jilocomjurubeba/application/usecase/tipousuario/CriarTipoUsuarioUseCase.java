package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.CriarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

/**
 * Caso de uso para criacao de um novo TipoUsuario.
 *
 * <p>Orquestra a criacao verificando unicidade do nome antes de persistir.
 *
 * @author Danilo Fernando
 */
public class CriarTipoUsuarioUseCase implements UseCase<CriarTipoUsuarioInput, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public CriarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public TipoUsuarioOutput executar(CriarTipoUsuarioInput input) {

        if (tipoUsuarioGateway.existePorNome(input.nome().trim().toUpperCase())) {
            throw new RegraDeNegocioException(
                    "Ja existe um tipo de usuario com o nome '" + input.nome() + "'");
        }

        TipoUsuario tipoUsuario = new TipoUsuario(input.nome(), input.descricao());
        TipoUsuario salvo = tipoUsuarioGateway.salvar(tipoUsuario);

        return toOutput(salvo);
    }

    private TipoUsuarioOutput toOutput(TipoUsuario tipoUsuario) {
        return new TipoUsuarioOutput(
                tipoUsuario.getId(),
                tipoUsuario.getNome(),
                tipoUsuario.getDescricao(),
                tipoUsuario.isAtivo());
    }
}
