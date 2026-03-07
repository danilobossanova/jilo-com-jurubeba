package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
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

        Restaurante.RestauranteSnapshot dados = domain.snapshot();

        RestauranteJpaEntity entity = new RestauranteJpaEntity();
        entity.setId(dados.id());
        entity.setNome(dados.nome());
        entity.setEndereco(dados.endereco());
        entity.setTypeCozinha(dados.typeCozinha());
        entity.setHoraAbertura(dados.horaAbertura());
        entity.setHoraFechamento(dados.horaFechamento());
        entity.setAtivo(dados.ativo());

        if (dados.donoId() == null) {
            throw new IllegalStateException("Restaurante precisa ter dono com id válido para persistir.");
        }

        entity.setDono(em.getReference(UsuarioJpaEntity.class, dados.donoId()));
        return entity;
    }

    private Usuario mapDonoToDomain(UsuarioJpaEntity entity) {
        if (entity == null) return null;

        TipoUsuario tipoDomain = null;
        if (entity.getTipoUsuario() != null) {
            var tu = entity.getTipoUsuario();
            tipoDomain = new TipoUsuario(
                    tu.getId(),
                    tu.getNome(),
                    tu.getDescricao(),
                    tu.isAtivo()
            );
        }

        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getCpf() != null ? new Cpf(entity.getCpf()) : null,
                entity.getEmail() != null ? new Email(entity.getEmail()) : null,
                entity.getTelefone(),
                tipoDomain,
                entity.isAtivo(),
                new ArrayList<>(),
                entity.getSenhaHash()
        );
    }
}