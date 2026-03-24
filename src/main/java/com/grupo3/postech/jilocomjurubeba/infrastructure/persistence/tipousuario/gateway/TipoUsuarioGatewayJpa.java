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
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Component
public class TipoUsuarioGatewayJpa implements TipoUsuarioGateway {

    private final TipoUsuarioRepository repository;
    private final TipoUsuarioPersistenceMapper mapper;

    /**
     * Construtor com injecao de dependencia do repositorio e mapper.
     *
     * @param repository repositorio JPA de TipoUsuario
     * @param mapper mapper de conversao entre dominio e JPA entity
     */
    public TipoUsuarioGatewayJpa(
            TipoUsuarioRepository repository, TipoUsuarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Persiste um tipo de usuario no banco de dados.
     *
     * <p>Converte a entidade de dominio para JPA entity, salva via repositorio e retorna a entidade
     * de dominio reconstituida com o ID gerado.
     *
     * @param tipoUsuario entidade de dominio a ser salva
     * @return entidade de dominio com ID persistido
     * @see TipoUsuarioGateway#salvar(TipoUsuario)
     */
    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioJpaEntity jpaEntity = mapper.toJpaEntity(tipoUsuario);
        TipoUsuarioJpaEntity salvo = repository.save(jpaEntity);
        return mapper.toDomain(salvo);
    }

    /**
     * Busca um tipo de usuario pelo identificador unico.
     *
     * @param id identificador do tipo de usuario
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see TipoUsuarioGateway#buscarPorId(Long)
     */
    @Override
    public Optional<TipoUsuario> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    /**
     * Busca um tipo de usuario pelo nome exato.
     *
     * @param nome nome do tipo de usuario
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see TipoUsuarioGateway#buscarPorNome(String)
     */
    @Override
    public Optional<TipoUsuario> buscarPorNome(String nome) {
        return repository.findByNome(nome).map(mapper::toDomain);
    }

    /**
     * Lista todos os tipos de usuario cadastrados no banco de dados.
     *
     * @return lista de entidades de dominio (pode ser vazia)
     * @see TipoUsuarioGateway#listarTodos()
     */
    @Override
    public List<TipoUsuario> listarTodos() {
        return mapper.toDomainList(repository.findAll());
    }

    /**
     * Remove fisicamente um tipo de usuario pelo ID.
     *
     * <p>Nota: na pratica, o sistema usa soft delete via {@code desativar()}. Este metodo existe
     * para compatibilidade com o contrato do gateway.
     *
     * @param id identificador do tipo de usuario a ser removido
     * @see TipoUsuarioGateway#deletar(Long)
     */
    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica se existe um tipo de usuario com o nome informado.
     *
     * @param nome nome a ser verificado
     * @return {@code true} se ja existe um registro com esse nome
     * @see TipoUsuarioGateway#existePorNome(String)
     */
    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNome(nome);
    }

    /**
     * Verifica se existe um tipo de usuario com o nome informado, excluindo um ID especifico.
     *
     * <p>Utilizado na atualizacao para garantir unicidade de nome sem conflitar com o proprio
     * registro.
     *
     * @param nome nome a ser verificado
     * @param id identificador a ser excluido da verificacao
     * @return {@code true} se existe outro registro com o mesmo nome
     * @see TipoUsuarioGateway#existePorNomeEIdDiferente(String, Long)
     */
    @Override
    public boolean existePorNomeEIdDiferente(String nome, Long id) {
        return repository.existsByNomeAndIdNot(nome, id);
    }
}
