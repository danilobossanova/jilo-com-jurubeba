package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.AtualizarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.BuscarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.CriarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.DeletarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.ListarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;

@Configuration
public class CardapioUseCaseConfig {

    @Bean
    public CriarCardapioUseCase criarCardapioUseCase(
            CardapioGatewayDomain cardapioGateway,
            RestauranteGatewayDomain restauranteGateway
    ) {
        return new CriarCardapioUseCase(cardapioGateway, restauranteGateway);
    }

    @Bean
    public ListarCardapioUseCase listarCardapioUseCase(CardapioGatewayDomain gateway) {
        return new ListarCardapioUseCase(gateway);
    }

    @Bean
    public BuscarCardapioUseCase buscarCardapioUseCase(CardapioGatewayDomain gateway) {
        return new BuscarCardapioUseCase(gateway);
    }

    @Bean
    public AtualizarCardapioUseCase atualizarCardapioUseCase(
            CardapioGatewayDomain cardapioGateway,
            RestauranteGatewayDomain restauranteGateway
    ) {
        return new AtualizarCardapioUseCase(cardapioGateway, restauranteGateway);
    }

    @Bean
    public DeletarCardapioUseCase deletarCardapioUseCase(CardapioGatewayDomain gateway) {
        return new DeletarCardapioUseCase(gateway);
    }
}