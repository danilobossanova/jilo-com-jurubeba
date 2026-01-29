package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.saude.VerificarSaudeUseCase;

/**
 * Configuração de beans para casos de uso.
 *
 * <p>Esta classe é responsável por registrar os casos de uso como beans Spring, injetando suas
 * dependências (Gateways, configs, etc).
 *
 * <p>Por que não usar @Component nos UseCases? - Manter a camada application livre de anotações
 * Spring - Centralizar a criação de beans - Facilitar testes unitários sem Spring
 *
 * @author Danilo Fernando
 */
@Configuration
public class UseCaseConfig {

    @Value("${info.app.version:1.0.0}")
    private String versaoAplicacao;

    /**
     * Bean para o caso de uso de verificação de saúde.
     *
     * @return instância de VerificarSaudeUseCase
     */
    @Bean
    public VerificarSaudeUseCase verificarSaudeUseCase() {
        return new VerificarSaudeUseCase(versaoAplicacao);
    }

    // Futuros casos de uso serão registrados aqui:
    //
    // @Bean
    // public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioGateway gateway) {
    //     return new CriarUsuarioUseCase(gateway);
    // }
    //
    // @Bean
    // public BuscarRestauranteUseCase buscarRestauranteUseCase(RestauranteGateway gateway) {
    //     return new BuscarRestauranteUseCase(gateway);
    // }
}
