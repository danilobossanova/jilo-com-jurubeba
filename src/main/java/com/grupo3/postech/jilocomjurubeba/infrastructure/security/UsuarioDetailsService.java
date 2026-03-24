package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository.UsuarioRepository;

/**
 * Implementacao de UserDetailsService que busca usuarios no banco de dados.
 *
 * <p>Utiliza o UsuarioRepository para buscar o usuario pelo email e construir o UserDetails para
 * autenticacao Spring Security.
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
@Service
@Profile("!test")
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    /**
     * Construtor com injecao de dependencia do repositorio de usuarios.
     *
     * @param usuarioRepo repositorio JPA de Usuario
     */
    public UsuarioDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    /**
     * Carrega os detalhes do usuario pelo email para autenticacao Spring Security.
     *
     * <p>Busca o usuario no banco pelo email (case insensitive), extrai o nome do tipo de usuario
     * para construir a authority (role) e monta o {@link UserDetails} com email, senha hash, role e
     * status ativo/inativo.
     *
     * @param username email do usuario (usado como identificador de login)
     * @return detalhes do usuario para autenticacao
     * @throws UsernameNotFoundException se o usuario nao for encontrado pelo email
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        var user =
                usuarioRepo
                        .findByEmailIgnoreCase(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "Usuario nao encontrado: " + username));

        String role = toRoleName(user.getTipoUsuario().getNome());

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getSenha())
                .authorities(new SimpleGrantedAuthority(role))
                .disabled(!user.isAtivo())
                .build();
    }

    /**
     * Converte o nome do tipo de usuario para o formato de role do Spring Security.
     *
     * <p>Aplica o prefixo "ROLE_", converte para uppercase e substitui espacos por underscores.
     * Exemplo: "Dono Restaurante" se torna "ROLE_DONO_RESTAURANTE".
     *
     * @param nomeTipoUsuario nome do tipo de usuario do dominio
     * @return nome da role no formato Spring Security (ex: "ROLE_MASTER")
     */
    private String toRoleName(String nomeTipoUsuario) {
        return "ROLE_" + nomeTipoUsuario.trim().toUpperCase().replace(' ', '_');
    }
}
