package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.mapper.TipoUsuarioMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TipoUsuarioGatewayJpaAdapter implements TipoUsuarioGateway {

  private final TipoUsuarioJpaRepository repository;
  private final TipoUsuarioMapper mapper;

  @Override
  public TipoUsuario salvar(TipoUsuario tipoUsuario) {
    var entity = mapper.toEntity(tipoUsuario);
    var saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Optional<TipoUsuario> buscarPorId(Long id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<TipoUsuario> buscarPorNome(String nome) {
    return repository.findByNomeIgnoreCase(nome).map(mapper::toDomain);
  }

  @Override
  public List<TipoUsuario> listarTodos() {
    return repository.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public void deletar(Long id) {
    repository.deleteById(id);
  }

  @Override
  public boolean existePorNome(String nome) {
    return repository.existsByNomeIgnoreCase(nome);
  }

  @Override
  public boolean existePorNomeEIdDiferente(String nome, Long id) {
    return repository.existsByNomeIgnoreCaseAndIdNot(nome, id);
  }
}
