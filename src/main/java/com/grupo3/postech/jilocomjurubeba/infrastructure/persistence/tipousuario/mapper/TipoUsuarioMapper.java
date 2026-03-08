package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.mapper;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TipoUsuarioMapper {

  public TipoUsuarioJpaEntity toEntity(TipoUsuario domain) {
    if (domain == null) return null;

    TipoUsuario.TipoUsuarioSnapshot dados = domain.snapshot();

    TipoUsuarioJpaEntity entity = new TipoUsuarioJpaEntity();
    entity.setId(dados.id());
    entity.setNome(dados.nome());
    entity.setDescricao(dados.descricao());
    entity.setAtivo(dados.ativo());

    return entity;
  }

  public TipoUsuario toDomain(TipoUsuarioJpaEntity entity) {
    if (entity == null) return null;

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
