package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;

/**
 * Mapper para conversao entre entidade de dominio Restaurante e JPA entity.
 *
 * <p>Converte entre o mundo puro do dominio e o mundo JPA. Os metodos usam implementacao manual
 * para evitar ambiguidade de construtores no MapStruct (a entidade de dominio tem 2 construtores e
 * nao pode ter anotacoes de framework).
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Mapper(componentModel = "spring")
public interface RestaurantePersistenceMapper {

    /**
     * Converte JPA entity para entidade de dominio.
     *
     * <p>Implementacao manual para usar o construtor de reconstituicao (8 argumentos). Reconstitui
     * o Usuario dono a partir da referencia JPA.
     *
     * @param jpaEntity entidade JPA
     * @return entidade de dominio
     */
    default Restaurante toDomain(RestauranteJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        Usuario dono = reconstituirDono(jpaEntity.getDono());

        return new Restaurante(
                jpaEntity.getId(),
                jpaEntity.getNome(),
                jpaEntity.getEndereco(),
                jpaEntity.getTipoCozinha(),
                jpaEntity.getHoraAbertura(),
                jpaEntity.getHoraFechamento(),
                dono,
                jpaEntity.isAtivo());
    }

    /**
     * Converte entidade de dominio para JPA entity.
     *
     * <p>Implementacao manual para lidar com o enum TipoCozinha e a referencia ao dono. O campo
     * dono da JPA entity NAO e preenchido aqui — deve ser definido separadamente no Gateway para
     * evitar problemas com proxies JPA.
     *
     * @param domain entidade de dominio
     * @return entidade JPA (sem dono preenchido)
     */
    default RestauranteJpaEntity toJpaEntity(Restaurante domain) {
        if (domain == null) {
            return null;
        }

        RestauranteJpaEntity entity = new RestauranteJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEndereco(domain.getEndereco());
        entity.setTipoCozinha(domain.getTipoCozinha());
        entity.setHoraAbertura(domain.getHoraAbertura());
        entity.setHoraFechamento(domain.getHoraFechamento());
        entity.setAtivo(domain.isAtivo());
        // dono e definido separadamente no gateway
        return entity;
    }

    /**
     * Converte lista de JPA entities para lista de entidades de dominio.
     *
     * @param jpaEntities lista de entidades JPA
     * @return lista de entidades de dominio
     */
    default List<Restaurante> toDomainList(List<RestauranteJpaEntity> jpaEntities) {
        if (jpaEntities == null) {
            return new ArrayList<>();
        }
        return jpaEntities.stream().map(this::toDomain).toList();
    }

    /**
     * Reconstitui a entidade de dominio Usuario a partir da JPA entity do dono.
     *
     * @param donoJpa entidade JPA do dono
     * @return entidade de dominio Usuario ou null
     */
    private Usuario reconstituirDono(UsuarioJpaEntity donoJpa) {
        if (donoJpa == null) {
            return null;
        }

        TipoUsuario tipoUsuario = null;
        if (donoJpa.getTipoUsuario() != null) {
            tipoUsuario =
                    new TipoUsuario(
                            donoJpa.getTipoUsuario().getId(),
                            donoJpa.getTipoUsuario().getNome(),
                            donoJpa.getTipoUsuario().getDescricao(),
                            donoJpa.getTipoUsuario().isAtivo());
        }

        return new Usuario(
                donoJpa.getId(),
                donoJpa.getNome(),
                new Cpf(donoJpa.getCpf()),
                new Email(donoJpa.getEmail()),
                donoJpa.getTelefone(),
                tipoUsuario,
                donoJpa.getSenha(),
                donoJpa.isAtivo(),
                new ArrayList<>());
    }
}
