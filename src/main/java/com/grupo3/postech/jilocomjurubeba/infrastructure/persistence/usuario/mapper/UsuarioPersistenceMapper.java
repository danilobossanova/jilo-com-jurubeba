package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;

/**
 * Mapper para conversao entre entidade de dominio Usuario e JPA entity.
 *
 * <p>Converte entre o mundo puro do dominio e o mundo JPA. Os metodos {@code toDomain} e {@code
 * toJpaEntity} usam implementacao manual para evitar ambiguidade de construtores no MapStruct (a
 * entidade de dominio tem 2 construtores e nao pode ter anotacoes de framework).
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
public interface UsuarioPersistenceMapper {

    /**
     * Converte JPA entity para entidade de dominio.
     *
     * <p>Implementacao manual para usar o construtor de reconstituicao (9 argumentos), evitando
     * ambiguidade do MapStruct. Reconstitui TipoUsuario e Value Objects (Cpf, Email) a partir dos
     * campos da JPA entity.
     *
     * @param jpaEntity entidade JPA
     * @return entidade de dominio
     */
    default Usuario toDomain(UsuarioJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        TipoUsuario tipoUsuario = null;
        if (jpaEntity.getTipoUsuario() != null) {
            tipoUsuario =
                    new TipoUsuario(
                            jpaEntity.getTipoUsuario().getId(),
                            jpaEntity.getTipoUsuario().getNome(),
                            jpaEntity.getTipoUsuario().getDescricao(),
                            jpaEntity.getTipoUsuario().isAtivo());
        }

        return new Usuario(
                jpaEntity.getId(),
                jpaEntity.getNome(),
                new Cpf(jpaEntity.getCpf()),
                new Email(jpaEntity.getEmail()),
                jpaEntity.getTelefone(),
                tipoUsuario,
                jpaEntity.getSenha(),
                jpaEntity.isAtivo(),
                new ArrayList<>());
    }

    /**
     * Converte entidade de dominio para JPA entity.
     *
     * <p>Implementacao manual para extrair valores de Value Objects (Cpf, Email). O campo
     * tipoUsuario da JPA entity NAO e preenchido aqui — deve ser definido separadamente no Gateway
     * para evitar problemas com proxies JPA.
     *
     * @param domain entidade de dominio
     * @return entidade JPA (sem tipoUsuario preenchido)
     */
    default UsuarioJpaEntity toJpaEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }

        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCpf(domain.getCpf().getNumero());
        entity.setEmail(domain.getEmail().getEndereco());
        entity.setTelefone(domain.getTelefone());
        entity.setSenha(domain.getSenha());
        entity.setAtivo(domain.isAtivo());
        // tipoUsuario e definido separadamente no gateway
        return entity;
    }

    /**
     * Converte lista de JPA entities para lista de entidades de dominio.
     *
     * @param jpaEntities lista de entidades JPA
     * @return lista de entidades de dominio
     */
    default List<Usuario> toDomainList(List<UsuarioJpaEntity> jpaEntities) {
        if (jpaEntities == null) {
            return new ArrayList<>();
        }
        return jpaEntities.stream().map(this::toDomain).toList();
    }
}
