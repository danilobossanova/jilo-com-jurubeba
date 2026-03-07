package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

public class CriarRestauranteUseCase implements UseCase<CriarRestauranteInput, RestauranteOutput> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;
    private final UsuarioGatewayDomain usuarioGatewayDomain;

    public CriarRestauranteUseCase(
            RestauranteGatewayDomain restauranteGatewayDomain,
            UsuarioGatewayDomain usuarioGatewayDomain
    ) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
        this.usuarioGatewayDomain = usuarioGatewayDomain;
    }

    @Override
    public RestauranteOutput executar(CriarRestauranteInput input) {
        if (restauranteGatewayDomain.findByNome(input.nome().trim()).isPresent()) {
            throw new RegraDeNegocioException("Restaurante ja cadastrado");
        }

        Long donoId = input.donoId();
        Usuario dono = usuarioGatewayDomain.findByIdUsuario(donoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", donoId));

        Restaurante restaurante = new Restaurante(
                input.nome(),
                input.endereco(),
                input.typeCozinha(),
                input.horaAbertura(),
                input.horaFechamento(),
                dono
        );

        Restaurante salvo = restauranteGatewayDomain.saveRestaurante(restaurante);
        return salvo.paraOutput();
    }
}