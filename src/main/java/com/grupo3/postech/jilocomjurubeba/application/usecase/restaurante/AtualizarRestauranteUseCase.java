package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;


public class AtualizarRestauranteUseCase implements UseCase<AtualizarRestauranteInput, RestauranteOutput> {

    private RestauranteGatewayDomain restauranteGatewayDomain;

    AtualizarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
    }

    @Override
    public RestauranteOutput executar(AtualizarRestauranteInput input) {
       Restaurante restaurante = restauranteGatewayDomain.findByIdRestaurante(input.id())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", input.id()));

       restaurante.atualizarDados(
           input.nome(),
           input.endereco(),
           input.typeCozinha(),
           input.horaAbertura(),
           input.horaFechamento(),
           input.dono()
       );

       Restaurante atualizado = restauranteGatewayDomain.saveRestaurante(restaurante);
       return toOutput(atualizado);
    }

    private RestauranteOutput toOutput(Restaurante restaurante){
        return new RestauranteOutput(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getEndereco(),
            restaurante.getTypeCozinha(),
            restaurante.getHoraAbertura(),
            restaurante.getHoraFechamento(),
            restaurante.getDono(),
            restaurante.isAtivo()
        );
    }


}
