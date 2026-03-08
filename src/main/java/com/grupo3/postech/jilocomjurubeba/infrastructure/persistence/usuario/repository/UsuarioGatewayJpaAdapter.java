// UsuarioGatewayJpaAdapter.java
package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.mapper.UsuarioMapper;

import lombok.RequiredArgsConstructor;

@Primary
@Repository
@RequiredArgsConstructor
public class UsuarioGatewayJpaAdapter implements UsuarioGateway {

    private final UsuarioJpaRepository repository;
    private final UsuarioMapper mapper;

    @Override
    @Transactional
    public Usuario saveUsuario(Usuario usuario) {
        var saved = repository.save(mapper.toEntity(usuario));
        var reloaded = repository.findByIdFetchTipoUsuario(saved.getId()).orElse(saved);
        return mapper.toDomain(reloaded);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByIdUsuario(Long id) {
        return repository.findByIdFetchTipoUsuario(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByCpf(String cpf) {
        return repository.findByCpfFetchTipoUsuario(cpf).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmailFetchTipoUsuario(email).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllUsuario() {
        return repository.findAllFetchTipoUsuario().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteUsuario(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deletarUsuario(Long id) {
        repository.deleteById(id);
    }
}
