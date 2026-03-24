package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;

/**
 * Mapper para conversao entre entidade de dominio TipoUsuario e JPA entity.
 *
 * <p>Converte entre o mundo puro do dominio e o mundo JPA. O metodo {@code toDomain} usa
 * implementacao manual para evitar ambiguidade de construtores no MapStruct (a entidade de dominio
 * tem 2 construtores e nao pode ter anotacoes de framework).
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
public interface TipoUsuarioPersistenceMapper {

    /**
     * Converte JPA entity para entidade de dominio.
     *
     * <p>Implementacao manual para usar o construtor completo de 4 argumentos, evitando ambiguidade
     * do MapStruct.
     *
     * @param jpaEntity entidade JPA
     * @return entidade de dominio
     */
    default TipoUsuario toDomain(TipoUsuarioJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return new TipoUsuario(
                jpaEntity.getId(),
                jpaEntity.getNome(),
                jpaEntity.getDescricao(),
                jpaEntity.isAtivo());
    }

    /**
     * Converte entidade de dominio para JPA entity.
     *
     * <p>Campos de auditoria (criadoEm, atualizadoEm) sao ignorados pois sao preenchidos
     * automaticamente pelo Hibernate via @CreationTimestamp e @UpdateTimestamp.
     *
     * @param tipoUsuario entidade de dominio
     * @return entidade JPA
     */
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    TipoUsuarioJpaEntity toJpaEntity(TipoUsuario tipoUsuario);

    /**
     * Converte lista de JPA entities para lista de entidades de dominio.
     *
     * @param jpaEntities lista de entidades JPA
     * @return lista de entidades de dominio
     */
    List<TipoUsuario> toDomainList(List<TipoUsuarioJpaEntity> jpaEntities);
}
