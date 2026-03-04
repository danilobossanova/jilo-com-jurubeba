package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.mapper;

import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;

@Component
public class TipoUsuarioMapper {

    public TipoUsuarioJpaEntity toEntity(TipoUsuario domain) {
        if (domain == null) return null;

        TipoUsuarioJpaEntity entity = new TipoUsuarioJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setAtivo(domain.isAtivo());
        return entity;
    }

    public TipoUsuario toDomain(TipoUsuarioJpaEntity entity) {
        if (entity == null) return null;

        // ✅ Blindagem contra dado inválido do BD
        String nome = entity.getNome();
        if (nome == null || nome.isBlank()) {
            nome = "DESCONHECIDO";
        }

        String descricao = entity.getDescricao();
        if (descricao == null || descricao.isBlank()) {
            descricao = "SEM_DESCRICAO";
        }

        return new TipoUsuario(entity.getId(), nome, descricao, entity.isAtivo());
    }
}