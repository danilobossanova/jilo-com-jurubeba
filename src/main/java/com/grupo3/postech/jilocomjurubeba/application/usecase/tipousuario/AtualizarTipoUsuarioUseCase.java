package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.AtualizarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

public class AtualizarTipoUsuarioUseCase
        implements UseCase<AtualizarTipoUsuarioInput, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public AtualizarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public TipoUsuarioOutput executar(AtualizarTipoUsuarioInput input) {
        TipoUsuario tipoUsuario =
                tipoUsuarioGateway
                        .buscarPorId(input.id())
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("TipoUsuario", input.id()));

        String nomeNormalizado = input.nome().trim().toUpperCase();

        if (tipoUsuarioGateway.existePorNomeEIdDiferente(nomeNormalizado, input.id())) {
            throw new RegraDeNegocioException(
                    "Ja existe outro tipo de usuario com o nome '" + input.nome() + "'");
        }

        tipoUsuario.atualizarCadastro(input.nome(), input.descricao());
        TipoUsuario atualizado = tipoUsuarioGateway.salvar(tipoUsuario);

        return atualizado.paraOutput();
    }
}