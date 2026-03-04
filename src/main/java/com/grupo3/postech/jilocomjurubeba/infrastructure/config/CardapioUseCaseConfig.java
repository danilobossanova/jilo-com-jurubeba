package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.*;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGatewayDomain;

@Configuration
public class CardapioUseCaseConfig {

    @Bean
    public CriarCardapioUseCase criarCardapioUseCase(CardapioGatewayDomain gateway) {
        return new CriarCardapioUseCase(gateway);
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
    public AtualizarCardapioUseCase atualizarCardapioUseCase(CardapioGatewayDomain gateway) {
        return new AtualizarCardapioUseCase(gateway);
    }

    @Bean
    public DeletarCardapioUseCase deletarCardapioUseCase(CardapioGatewayDomain gateway) {
        return new DeletarCardapioUseCase(gateway);
    }
}
