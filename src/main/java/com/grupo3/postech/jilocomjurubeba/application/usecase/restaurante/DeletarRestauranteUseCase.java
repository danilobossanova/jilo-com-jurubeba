package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;

import java.util.Optional;

public class DeletarRestauranteUseCase implements UseCaseSemSaida<Long> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;

    public DeletarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
    }

    @Override
    public void executar(Long input) {
        Restaurante restaurante = restauranteGatewayDomain.findByIdRestaurante(input)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", input));

        restaurante.desativar();
        restauranteGatewayDomain.saveRestaurante(restaurante);

    }
}
