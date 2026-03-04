package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.*;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

@Configuration
public class RestauranteUseCaseConfig {

    @Bean
    public CriarRestauranteUseCase criarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain,
                                                           UsuarioGatewayDomain usuarioGatewayDomain) {
        return new CriarRestauranteUseCase(restauranteGatewayDomain, usuarioGatewayDomain);
    }

    @Bean
    public AtualizarRestauranteUseCase atualizarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain,
                                                                   UsuarioGatewayDomain usuarioGatewayDomain) {
        return new AtualizarRestauranteUseCase(restauranteGatewayDomain, usuarioGatewayDomain);
    }

    @Bean
    public BuscarRestauranteUseCase buscarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        return new BuscarRestauranteUseCase(restauranteGatewayDomain);
    }

    @Bean
    public ListarRestauranteUseCase listarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        return new ListarRestauranteUseCase(restauranteGatewayDomain);
    }

    @Bean
    public DeletarRestauranteUseCase deletarRestauranteUseCase(RestauranteGatewayDomain restauranteGatewayDomain) {
        return new DeletarRestauranteUseCase(restauranteGatewayDomain);
    }
}
