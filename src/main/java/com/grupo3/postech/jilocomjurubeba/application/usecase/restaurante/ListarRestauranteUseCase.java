package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.restaurante.RestauranteMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

public class ListarRestauranteUseCase implements UseCaseSemEntrada<List<RestauranteOutput>> {

    private final RestauranteGateway restauranteGateway;

    public ListarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    @Override
    public List<RestauranteOutput> executar() {
        return restauranteGateway
            .findAllRestaurante()
            .stream()
            .map(RestauranteMapper::paraOutput)
            .toList();
    }
}
