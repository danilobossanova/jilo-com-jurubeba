package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.restaurante.RestauranteMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

public class CriarRestauranteUseCase implements UseCase<CriarRestauranteInput, RestauranteOutput> {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;

    public CriarRestauranteUseCase(
        RestauranteGateway restauranteGateway,
        UsuarioGateway usuarioGateway
    ) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public RestauranteOutput executar(CriarRestauranteInput input) {

        if (restauranteGateway.findByNome(input.nome().trim()).isPresent()) {
            throw new RegraDeNegocioException("Restaurante ja cadastrado");
        }

        Long donoId = input.donoId();

        Usuario dono = usuarioGateway
            .findByIdUsuario(donoId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", donoId));

        Restaurante restaurante = new Restaurante(
            input.nome(),
            input.endereco(),
            input.typeCozinha(),
            input.horaAbertura(),
            input.horaFechamento(),
            dono
        );

        Restaurante salvo = restauranteGateway.saveRestaurante(restaurante);

        return RestauranteMapper.paraOutput(salvo);
    }
}
