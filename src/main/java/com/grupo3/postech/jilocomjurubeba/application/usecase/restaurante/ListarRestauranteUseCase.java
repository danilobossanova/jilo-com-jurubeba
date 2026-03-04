package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;

public class ListarRestauranteUseCase implements UseCaseSemEntrada<List<RestauranteOutput>> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;

    public ListarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
    }

    @Override
    public List<RestauranteOutput> executar() {
        return restauranteGatewayDomain.findAllRestaurante().stream()
            .map(this::toOutput)
            .toList();
    }

    private RestauranteOutput toOutput(Restaurante restaurante) {
        Long donoId = (restaurante.getDono() == null) ? null : restaurante.getDono().getId();

        return new RestauranteOutput(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getEndereco(),
            restaurante.getTypeCozinha(),
            restaurante.getHoraAbertura(),
            restaurante.getHoraFechamento(),
            donoId,
            restaurante.isAtivo()
        );
    }
}
