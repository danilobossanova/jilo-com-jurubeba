package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.AutenticacaoGateway;

/**
 * Implementacao do AutenticacaoGateway usando Spring Security + JWT.
 *
 * <p>Autentica as credenciais via AuthenticationManager do Spring Security e gera o token JWT via
 * JwtService.
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
@Profile("!test")
@Component
public class JwtAutenticacaoGateway implements AutenticacaoGateway {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Construtor com injecao de dependencia do gerenciador de autenticacao e servico JWT.
     *
     * @param authenticationManager gerenciador de autenticacao do Spring Security
     * @param jwtService servico de geracao de tokens JWT
     */
    public JwtAutenticacaoGateway(
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Autentica um usuario com email e senha e retorna um token JWT.
     *
     * <p>Delega a autenticacao ao {@link AuthenticationManager} do Spring Security. Se as
     * credenciais forem validas, gera e retorna um token JWT. Caso contrario, lanca {@link
     * RegraDeNegocioException} com mensagem de credenciais invalidas.
     *
     * @param email email do usuario (usado como username)
     * @param senha senha em texto aberto para validacao
     * @return token JWT assinado como String compacta
     * @throws RegraDeNegocioException se as credenciais forem invalidas
     * @see AutenticacaoGateway#autenticar(String, String)
     */
    @Override
    public String autenticar(String email, String senha) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(email, senha));

            return jwtService.generateToken(authentication);
        } catch (BadCredentialsException e) {
            throw new RegraDeNegocioException("Credenciais invalidas");
        }
    }
}
