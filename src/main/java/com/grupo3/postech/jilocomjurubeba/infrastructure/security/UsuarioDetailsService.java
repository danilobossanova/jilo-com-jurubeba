package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioJpaRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = usuarioRepo.findByEmailFetchTipoUsuario(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        String role = toRoleName(user.getTipoUsuario().getNome());

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getSenhaHash())
            .authorities(new SimpleGrantedAuthority(role))
            .disabled(!user.isAtivo())
            .build();
    }

    private String toRoleName(String nomeTipoUsuario) {
        return "ROLE_" + nomeTipoUsuario.trim().toUpperCase().replace(' ', '_');
    }
}