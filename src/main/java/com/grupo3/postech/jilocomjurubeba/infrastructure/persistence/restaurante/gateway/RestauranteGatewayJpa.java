package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.gateway;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.mapper.RestauranteMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.repository.RestauranteJpaRepository;

@Component
public class RestauranteGatewayJpa implements RestauranteGateway {

    private final RestauranteJpaRepository repository;
    private final RestauranteMapper mapper;

    public RestauranteGatewayJpa(RestauranteJpaRepository repository, RestauranteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurante> findByIdRestaurante(Long id) {
        return repository.findByIdFetchDono(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Restaurante saveRestaurante(Restaurante restaurante) {
        var entity = mapper.toEntity(restaurante);
        var saved = repository.save(entity);

        repository.flush(); // ✅ Força o Hibernate a sincronizar com o banco

        // ✅ Re-busca profunda para preencher Dono e TipoUsuario
        var completo = repository.findByIdFetchDono(saved.getId())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", saved.getId()));

        return mapper.toDomain(completo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurante> findByNome(String nome) {
        if (nome == null) return Optional.empty();
        return repository.findByNomeIgnoreCaseFetchDono(nome.trim()).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> findAllRestaurante() {
        return repository.findAllByAtivoTrueFetchDono().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteRestaurante(Long id) {
        var entity = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", id));
        entity.setAtivo(false); // dirty checking faz o resto
    }
}
