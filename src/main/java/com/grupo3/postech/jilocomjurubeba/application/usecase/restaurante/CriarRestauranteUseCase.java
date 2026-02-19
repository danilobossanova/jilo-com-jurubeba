package com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

import java.util.ArrayList;

public class CriarRestauranteUseCase implements UseCase<CriarRestauranteInput, RestauranteOutput> {

    private final RestauranteGatewayDomain restauranteGatewayDomain;

    public CriarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        this.restauranteGatewayDomain = restauranteGatewayDomain;
    }


    @Override
    public RestauranteOutput executar(CriarRestauranteInput input) {
        if (restauranteGatewayDomain.findByNome(input.nome().trim().toUpperCase()).isPresent()) {
            throw new RegraDeNegocioException("Restaurante ja cadastrado");
        }

        Restaurante restaurante = new Restaurante(
            input.nome(),
            input.endereco(),
            input.typeCozinha(),
            input.horaAbertura(),
            input.horaFechamento(),
            input.dono()
        );

        Restaurante salvo = restauranteGatewayDomain.saveRestaurante(restaurante);

        return toOutput(salvo);
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
