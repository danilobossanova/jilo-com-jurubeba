package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;

public class ListarRestauranteUseCase implements UseCaseSemEntrada<List<RestauranteOutput>> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;

    public ListarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
    }

    @Override
    public List<RestauranteOutput> executar() {
        return restauranteGatewayDomain.findAllRestaurante()
                .stream()
                .map(restaurante -> restaurante.paraOutput())
                .toList();
    }
}