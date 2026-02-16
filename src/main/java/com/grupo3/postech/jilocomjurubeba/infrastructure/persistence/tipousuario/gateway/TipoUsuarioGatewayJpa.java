package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.mapper.TipoUsuarioPersistenceMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository.TipoUsuarioRepository;

/**
 * Implementacao JPA do TipoUsuarioGateway.
 *
 * <p>Traduz chamadas do dominio para operacoes JPA, usando o PersistenceMapper para converter entre
 * entidade de dominio e JPA entity.
 *
 * <p>Na Clean Architecture, esta classe e o "adapter" que conecta o port (TipoUsuarioGateway) ao
 * framework (Spring Data JPA).
 *
 * @author Danilo Fernando
 */
@Component
public class TipoUsuarioGatewayJpa implements TipoUsuarioGateway {

    private final TipoUsuarioRepository repository;
    private final TipoUsuarioPersistenceMapper mapper;

    public TipoUsuarioGatewayJpa(
            TipoUsuarioRepository repository, TipoUsuarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioJpaEntity jpaEntity = mapper.toJpaEntity(tipoUsuario);
        TipoUsuarioJpaEntity salvo = repository.save(jpaEntity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<TipoUsuario> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<TipoUsuario> buscarPorNome(String nome) {
        return repository.findByNome(nome).map(mapper::toDomain);
    }

    @Override
    public List<TipoUsuario> listarTodos() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNome(nome);
    }

    @Override
    public boolean existePorNomeEIdDiferente(String nome, Long id) {
        return repository.existsByNomeAndIdNot(nome, id);
    }
}
