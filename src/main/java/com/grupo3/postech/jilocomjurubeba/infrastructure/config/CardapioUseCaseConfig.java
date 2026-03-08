package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.AtualizarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.BuscarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.CriarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.DeletarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.ListarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;

@Configuration
public class CardapioUseCaseConfig {

    @Bean
    public CriarCardapioUseCase criarCardapioUseCase(
            CardapioGateway cardapioGateway,
            RestauranteGateway restauranteGateway
    ) {
        return new CriarCardapioUseCase(cardapioGateway, restauranteGateway);
    }

    @Bean
    public ListarCardapioUseCase listarCardapioUseCase(CardapioGateway gateway) {
        return new ListarCardapioUseCase(gateway);
    }

    @Bean
    public BuscarCardapioUseCase buscarCardapioUseCase(CardapioGateway gateway) {
        return new BuscarCardapioUseCase(gateway);
    }

    @Bean
    public AtualizarCardapioUseCase atualizarCardapioUseCase(
            CardapioGateway cardapioGateway,
            RestauranteGateway restauranteGateway
    ) {
        return new AtualizarCardapioUseCase(cardapioGateway, restauranteGateway);
    }

    @Bean
    public DeletarCardapioUseCase deletarCardapioUseCase(CardapioGateway gateway) {
        return new DeletarCardapioUseCase(gateway);
    }
}
