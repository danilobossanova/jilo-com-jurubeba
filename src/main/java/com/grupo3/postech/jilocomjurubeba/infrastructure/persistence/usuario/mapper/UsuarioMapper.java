package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.mapper.TipoUsuarioMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final TipoUsuarioMapper tipoUsuarioMapper;

    public UsuarioJpaEntity toEntity(Usuario domain) {
        if (domain == null) return null;

        UsuarioJpaEntity entity = new UsuarioJpaEntity();

        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCpf(domain.getCpf() != null ? domain.getCpf().getNumero() : null);
        entity.setEmail(domain.getEmail() != null ? domain.getEmail().getEmail() : null);
        entity.setTelefone(domain.getTelefone());
        entity.setAtivo(domain.isAtivo());
        entity.setSenhaHash(domain.getSenha());

        if (domain.getTipoUsuario() != null && domain.getTipoUsuario().getId() != null) {
            TipoUsuarioJpaEntity tipoRef = new TipoUsuarioJpaEntity();
            tipoRef.setId(domain.getTipoUsuario().getId());
            entity.setTipoUsuario(tipoRef);
        }

        return entity;
    }

    public Usuario toDomain(UsuarioJpaEntity entity) {
        if (entity == null) return null;

        return new Usuario(
            entity.getId(),
            entity.getNome(),
            entity.getCpf() != null ? new Cpf(entity.getCpf()) : null,
            entity.getEmail() != null ? new Email(entity.getEmail()) : null,
            entity.getTelefone(),
            tipoUsuarioMapper.toDomain(entity.getTipoUsuario()),
            entity.isAtivo(),
            new ArrayList<>(),
            entity.getSenhaHash()
        );
    }
}