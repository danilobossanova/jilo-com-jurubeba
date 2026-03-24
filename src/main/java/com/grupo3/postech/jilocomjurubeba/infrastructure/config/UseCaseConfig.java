package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo3.postech.jilocomjurubeba.application.usecase.autenticacao.AutenticarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.AtualizarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.BuscarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.CriarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.DeletarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.ListarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.AtualizarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.BuscarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.CriarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.DeletarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.ListarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.saude.VerificarSaudeUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.AtualizarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.BuscarTipoUsuarioPorIdUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.CriarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.DeletarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.ListarTiposUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.BuscarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.CriarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.DeletarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.ListarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.AutenticacaoGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.CriptografiaSenhaGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

/**
 * Configuracao de beans para casos de uso.
 *
 * <p>Esta classe e responsavel por registrar os casos de uso como beans Spring, injetando suas
 * dependencias (Gateways, configs, etc).
 *
 * <p>Por que nao usar @Component nos UseCases? - Manter a camada application livre de anotacoes
 * Spring - Centralizar a criacao de beans - Facilitar testes unitarios sem Spring
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Configuration
public class UseCaseConfig {

    @Value("${info.app.version:1.0.0}")
    private String versaoAplicacao;

    // ========== Autenticacao Use Case ==========

    /**
     * Cria o bean para o caso de uso de autenticacao de usuario.
     *
     * <p>Injeta o {@link AutenticacaoGateway} que encapsula a logica de autenticacao (JWT via
     * Spring Security).
     *
     * @param autenticacaoGateway gateway de autenticacao do dominio
     * @return instancia de {@link AutenticarUsuarioUseCase}
     */
    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase(
            AutenticacaoGateway autenticacaoGateway) {
        return new AutenticarUsuarioUseCase(autenticacaoGateway);
    }

    // ========== Saude ==========

    /**
     * Cria o bean para o caso de uso de verificacao de saude da aplicacao.
     *
     * <p>Recebe a versao da aplicacao configurada via propriedade {@code info.app.version}.
     *
     * @return instancia de {@link VerificarSaudeUseCase}
     */
    @Bean
    public VerificarSaudeUseCase verificarSaudeUseCase() {
        return new VerificarSaudeUseCase(versaoAplicacao);
    }

    // ========== TipoUsuario Use Cases ==========

    /**
     * Cria o bean para o caso de uso de criacao de tipo de usuario.
     *
     * @param gateway gateway de persistencia de TipoUsuario
     * @return instancia de {@link CriarTipoUsuarioUseCase}
     */
    @Bean
    public CriarTipoUsuarioUseCase criarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new CriarTipoUsuarioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de busca de tipo de usuario por ID.
     *
     * @param gateway gateway de persistencia de TipoUsuario
     * @return instancia de {@link BuscarTipoUsuarioPorIdUseCase}
     */
    @Bean
    public BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway gateway) {
        return new BuscarTipoUsuarioPorIdUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de listagem de todos os tipos de usuario.
     *
     * @param gateway gateway de persistencia de TipoUsuario
     * @return instancia de {@link ListarTiposUsuarioUseCase}
     */
    @Bean
    public ListarTiposUsuarioUseCase listarTiposUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new ListarTiposUsuarioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de atualizacao de tipo de usuario.
     *
     * @param gateway gateway de persistencia de TipoUsuario
     * @return instancia de {@link AtualizarTipoUsuarioUseCase}
     */
    @Bean
    public AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new AtualizarTipoUsuarioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de delecao (soft delete) de tipo de usuario.
     *
     * @param gateway gateway de persistencia de TipoUsuario
     * @return instancia de {@link DeletarTipoUsuarioUseCase}
     */
    @Bean
    public DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new DeletarTipoUsuarioUseCase(gateway);
    }

    // ========== Usuario Use Cases ==========

    /**
     * Cria o bean para o caso de uso de criacao de usuario.
     *
     * <p>Requer tres gateways: UsuarioGateway para persistencia, TipoUsuarioGateway para validar o
     * tipo informado, e CriptografiaSenhaGateway para hash da senha.
     *
     * @param usuarioGateway gateway de persistencia de Usuario
     * @param tipoUsuarioGateway gateway de persistencia de TipoUsuario
     * @param criptografiaSenhaGateway gateway de criptografia de senha
     * @return instancia de {@link CriarUsuarioUseCase}
     */
    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(
            UsuarioGateway usuarioGateway,
            TipoUsuarioGateway tipoUsuarioGateway,
            CriptografiaSenhaGateway criptografiaSenhaGateway) {
        return new CriarUsuarioUseCase(
                usuarioGateway, tipoUsuarioGateway, criptografiaSenhaGateway);
    }

    /**
     * Cria o bean para o caso de uso de busca de usuario por ID.
     *
     * @param gateway gateway de persistencia de Usuario
     * @return instancia de {@link BuscarUsuarioUseCase}
     */
    @Bean
    public BuscarUsuarioUseCase buscarUsuarioUseCase(UsuarioGateway gateway) {
        return new BuscarUsuarioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de listagem de todos os usuarios.
     *
     * @param gateway gateway de persistencia de Usuario
     * @return instancia de {@link ListarUsuarioUseCase}
     */
    @Bean
    public ListarUsuarioUseCase listarUsuarioUseCase(UsuarioGateway gateway) {
        return new ListarUsuarioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de atualizacao de usuario.
     *
     * @param gateway gateway de persistencia de Usuario
     * @return instancia de {@link AtualizarUsuarioUseCase}
     */
    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioGateway gateway) {
        return new AtualizarUsuarioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de delecao (soft delete) de usuario.
     *
     * @param gateway gateway de persistencia de Usuario
     * @return instancia de {@link DeletarUsuarioUseCase}
     */
    @Bean
    public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioGateway gateway) {
        return new DeletarUsuarioUseCase(gateway);
    }

    // ========== Restaurante Use Cases ==========

    /**
     * Cria o bean para o caso de uso de criacao de restaurante.
     *
     * <p>Requer dois gateways: RestauranteGateway para persistencia e UsuarioGateway para validar o
     * dono informado.
     *
     * @param restauranteGateway gateway de persistencia de Restaurante
     * @param usuarioGateway gateway de persistencia de Usuario
     * @return instancia de {@link CriarRestauranteUseCase}
     */
    @Bean
    public CriarRestauranteUseCase criarRestauranteUseCase(
            RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        return new CriarRestauranteUseCase(restauranteGateway, usuarioGateway);
    }

    /**
     * Cria o bean para o caso de uso de busca de restaurante por ID.
     *
     * @param gateway gateway de persistencia de Restaurante
     * @return instancia de {@link BuscarRestauranteUseCase}
     */
    @Bean
    public BuscarRestauranteUseCase buscarRestauranteUseCase(RestauranteGateway gateway) {
        return new BuscarRestauranteUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de listagem de todos os restaurantes.
     *
     * @param gateway gateway de persistencia de Restaurante
     * @return instancia de {@link ListarRestauranteUseCase}
     */
    @Bean
    public ListarRestauranteUseCase listarRestauranteUseCase(RestauranteGateway gateway) {
        return new ListarRestauranteUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de atualizacao de restaurante.
     *
     * @param gateway gateway de persistencia de Restaurante
     * @return instancia de {@link AtualizarRestauranteUseCase}
     */
    @Bean
    public AtualizarRestauranteUseCase atualizarRestauranteUseCase(RestauranteGateway gateway) {
        return new AtualizarRestauranteUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de delecao (soft delete) de restaurante.
     *
     * @param gateway gateway de persistencia de Restaurante
     * @return instancia de {@link DeletarRestauranteUseCase}
     */
    @Bean
    public DeletarRestauranteUseCase deletarRestauranteUseCase(RestauranteGateway gateway) {
        return new DeletarRestauranteUseCase(gateway);
    }

    // ========== Cardapio Use Cases ==========

    /**
     * Cria o bean para o caso de uso de criacao de item de cardapio.
     *
     * <p>Requer dois gateways: CardapioGateway para persistencia e RestauranteGateway para validar
     * o restaurante associado.
     *
     * @param cardapioGateway gateway de persistencia de Cardapio
     * @param restauranteGateway gateway de persistencia de Restaurante
     * @return instancia de {@link CriarCardapioUseCase}
     */
    @Bean
    public CriarCardapioUseCase criarCardapioUseCase(
            CardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
        return new CriarCardapioUseCase(cardapioGateway, restauranteGateway);
    }

    /**
     * Cria o bean para o caso de uso de busca de item de cardapio por ID.
     *
     * @param gateway gateway de persistencia de Cardapio
     * @return instancia de {@link BuscarCardapioUseCase}
     */
    @Bean
    public BuscarCardapioUseCase buscarCardapioUseCase(CardapioGateway gateway) {
        return new BuscarCardapioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de listagem de todos os itens de cardapio.
     *
     * @param gateway gateway de persistencia de Cardapio
     * @return instancia de {@link ListarCardapioUseCase}
     */
    @Bean
    public ListarCardapioUseCase listarCardapioUseCase(CardapioGateway gateway) {
        return new ListarCardapioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de atualizacao de item de cardapio.
     *
     * @param gateway gateway de persistencia de Cardapio
     * @return instancia de {@link AtualizarCardapioUseCase}
     */
    @Bean
    public AtualizarCardapioUseCase atualizarCardapioUseCase(CardapioGateway gateway) {
        return new AtualizarCardapioUseCase(gateway);
    }

    /**
     * Cria o bean para o caso de uso de delecao (soft delete) de item de cardapio.
     *
     * @param gateway gateway de persistencia de Cardapio
     * @return instancia de {@link DeletarCardapioUseCase}
     */
    @Bean
    public DeletarCardapioUseCase deletarCardapioUseCase(CardapioGateway gateway) {
        return new DeletarCardapioUseCase(gateway);
    }
}
