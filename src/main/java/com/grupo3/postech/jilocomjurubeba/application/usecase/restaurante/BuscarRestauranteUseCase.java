package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;

public class BuscarRestauranteUseCase implements UseCase<Long, RestauranteOutput> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;

    public BuscarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
    }

    @Override
    public RestauranteOutput executar(Long input) {
        return restauranteGatewayDomain
                .findByIdRestaurante(input)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", input))
                .paraOutput();
    }
}