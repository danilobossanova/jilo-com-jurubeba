package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.restaurante.RestauranteMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

public class BuscarRestauranteUseCase implements UseCase<Long, RestauranteOutput> {

    private final RestauranteGateway restauranteGateway;

    public BuscarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    @Override
    public RestauranteOutput executar(Long input) {
        return restauranteGateway
            .findByIdRestaurante(input)
            .map(RestauranteMapper::paraOutput)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", input));
    }
}
