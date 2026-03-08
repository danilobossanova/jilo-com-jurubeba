package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.*;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

@Configuration
public class RestauranteUseCaseConfig {

    @Bean
    public CriarRestauranteUseCase criarRestauranteUseCase(RestauranteGateway restauranteGateway,
                                                           UsuarioGateway usuarioGateway) {
        return new CriarRestauranteUseCase(restauranteGateway, usuarioGateway);
    }

    @Bean
    public AtualizarRestauranteUseCase atualizarRestauranteUseCase(RestauranteGateway restauranteGateway,
                                                                   UsuarioGateway usuarioGateway) {
        return new AtualizarRestauranteUseCase(restauranteGateway, usuarioGateway);
    }

    @Bean
    public BuscarRestauranteUseCase buscarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        return new BuscarRestauranteUseCase(restauranteGateway);
    }

    @Bean
    public ListarRestauranteUseCase listarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        return new ListarRestauranteUseCase(restauranteGateway);
    }

    @Bean
    public DeletarRestauranteUseCase deletarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        return new DeletarRestauranteUseCase(restauranteGateway);
    }
}
