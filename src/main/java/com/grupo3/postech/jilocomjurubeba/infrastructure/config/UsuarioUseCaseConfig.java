package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.BuscarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.CriarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.DeletarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.ListarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

@Configuration
public class UsuarioUseCaseConfig {

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(
            UsuarioGatewayDomain usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        return new CriarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(
            UsuarioGatewayDomain usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        return new AtualizarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway);
    }

    @Bean
    public BuscarUsuarioUseCase buscarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        return new BuscarUsuarioUseCase(usuarioGateway);
    }

    @Bean
    public ListarUsuarioUseCase listarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        return new ListarUsuarioUseCase(usuarioGateway);
    }

    @Bean
    public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        return new DeletarUsuarioUseCase(usuarioGateway);
    }
}
