package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;

public class DeletarRestauranteUseCase implements UseCaseSemSaida<Long> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;

    public DeletarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
    }

    @Override
    public void executar(Long input) {
        // ✅ o gateway já faz exclusão lógica e lança EntidadeNaoEncontradaException se não existir
        restauranteGatewayDomain.deleteRestaurante(input);
    }
}
