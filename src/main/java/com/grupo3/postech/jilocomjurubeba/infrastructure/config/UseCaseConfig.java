package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.saude.VerificarSaudeUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.AtualizarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.BuscarTipoUsuarioPorIdUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.CriarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.DeletarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.ListarTiposUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

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

    // ========== TipoUsuario Use Cases ==========

    @Bean
    public CriarTipoUsuarioUseCase criarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new CriarTipoUsuarioUseCase(gateway);
    }

    @Bean
    public BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway gateway) {
        return new BuscarTipoUsuarioPorIdUseCase(gateway);
    }

    @Bean
    public ListarTiposUsuarioUseCase listarTiposUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new ListarTiposUsuarioUseCase(gateway);
    }

    @Bean
    public AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new AtualizarTipoUsuarioUseCase(gateway);
    }

    @Bean
    public DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new DeletarTipoUsuarioUseCase(gateway);
    }
}
