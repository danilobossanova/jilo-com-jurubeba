package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.CriptografiaSenhaGateway;

/**
 * Implementacao Spring Security do CriptografiaSenhaGateway.
 *
 * <p>Utiliza o {@link PasswordEncoder} do Spring Security (BCrypt) para criptografar senhas de
 * usuarios.
 *
 * <p>Na Clean Architecture, esta classe e o "adapter" que conecta o port (CriptografiaSenhaGateway)
 * ao framework (Spring Security).
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
@Component
@Profile("!test")
public class SpringPasswordHashGateway implements CriptografiaSenhaGateway {

    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor com injecao de dependencia do codificador de senhas.
     *
     * @param passwordEncoder codificador de senhas do Spring Security (BCrypt)
     */
    public SpringPasswordHashGateway(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Criptografa uma senha em texto aberto usando BCrypt.
     *
     * <p>Delega ao {@link PasswordEncoder} do Spring Security para gerar o hash BCrypt da senha.
     *
     * @param senhaAberta senha em texto puro a ser criptografada
     * @return hash BCrypt da senha
     * @see CriptografiaSenhaGateway#criptografar(String)
     */
    @Override
    public String criptografar(String senhaAberta) {
        return passwordEncoder.encode(senhaAberta);
    }
}
