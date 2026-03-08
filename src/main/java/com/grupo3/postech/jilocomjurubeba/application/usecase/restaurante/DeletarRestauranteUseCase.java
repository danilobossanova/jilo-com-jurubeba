package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

public class DeletarRestauranteUseCase implements UseCaseSemSaida<Long> {

    private final RestauranteGateway restauranteGateway;

    public DeletarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    @Override
    public void executar(Long input) {
        // ✅ o gateway já faz exclusão lógica e lança EntidadeNaoEncontradaException se não existir
        restauranteGateway.deleteRestaurante(input);
    }
}
