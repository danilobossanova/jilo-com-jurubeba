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

        Usuario.UsuarioSnapshot dados = domain.snapshot();

        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setId(dados.id());
        entity.setNome(dados.nome());
        entity.setCpf(dados.cpf());
        entity.setEmail(dados.email());
        entity.setTelefone(dados.telefone());
        entity.setAtivo(dados.ativo());
        entity.setSenhaHash(dados.senhaHash());

        if (dados.tipoUsuarioId() != null) {
            TipoUsuarioJpaEntity tipoRef = new TipoUsuarioJpaEntity();
            tipoRef.setId(dados.tipoUsuarioId());
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