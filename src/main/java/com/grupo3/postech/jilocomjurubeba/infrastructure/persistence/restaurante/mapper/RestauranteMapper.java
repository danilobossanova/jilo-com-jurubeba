package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.mapper;

import java.util.ArrayList;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;

@Component
public class RestauranteMapper {

    private final EntityManager em;

    public RestauranteMapper(EntityManager em) {
        this.em = em;
    }

    public Restaurante toDomain(RestauranteJpaEntity entity) {
        if (entity == null) return null;

        return new Restaurante(
            entity.getId(),
            entity.getNome(),
            entity.getEndereco(),
            entity.getTypeCozinha(),
            entity.getHoraAbertura(),
            entity.getHoraFechamento(),
            mapDonoToDomain(entity.getDono()),
            entity.isAtivo()
        );
    }

    public RestauranteJpaEntity toEntity(Restaurante domain) {
        if (domain == null) return null;

        RestauranteJpaEntity entity = new RestauranteJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEndereco(domain.getEndereco());
        entity.setTypeCozinha(domain.getTypeCozinha());
        entity.setHoraAbertura(domain.getHoraAbertura());
        entity.setHoraFechamento(domain.getHoraFechamento());
        entity.setAtivo(domain.isAtivo());

        if (domain.getDono() == null || domain.getDono().getId() == null) {
            throw new IllegalStateException("Restaurante precisa ter dono com id válido para persistir.");
        }

        // ✅ proxy controlado (normal)
        entity.setDono(em.getReference(UsuarioJpaEntity.class, domain.getDono().getId()));

        return entity;
    }

    private Usuario mapDonoToDomain(UsuarioJpaEntity entity) {
        if (entity == null) return null;

        // ✅ Sem Hibernate.isInitialized aqui: você já garante o carregamento no Repository com JOIN FETCH
        TipoUsuario tipoDomain = null;
        if (entity.getTipoUsuario() != null) {
            TipoUsuarioJpaEntity tu = entity.getTipoUsuario();
            tipoDomain = new TipoUsuario(
                tu.getId(),
                tu.getNome(),
                tu.getDescricao(),
                tu.isAtivo()
            );
        }

        // ✅ Usuario de domínio “enxuto”, sem tentar navegar relacionamentos
        return new Usuario(
            entity.getId(),
            entity.getNome(),
            new Cpf(entity.getCpf()),
            new Email(entity.getEmail()),
            entity.getTelefone(),
            tipoDomain,
            entity.isAtivo(),
            new ArrayList<>(), // sempre vazio aqui, evita serialização/grafo
            entity.getSenhaHash()
        );
    }
}
