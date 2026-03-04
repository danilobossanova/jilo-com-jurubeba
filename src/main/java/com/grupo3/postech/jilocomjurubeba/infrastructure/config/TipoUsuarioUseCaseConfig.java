package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.AtualizarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.BuscarTipoUsuarioPorIdUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.CriarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.DeletarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.ListarTiposUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

@Configuration
public class TipoUsuarioUseCaseConfig {

    @Bean
    public CriarTipoUsuarioUseCase criarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new CriarTipoUsuarioUseCase(gateway);
    }

    @Bean
    public ListarTiposUsuarioUseCase listarTiposUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new ListarTiposUsuarioUseCase(gateway);
    }

    @Bean
    public BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway gateway) {
        return new BuscarTipoUsuarioPorIdUseCase(gateway);
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
