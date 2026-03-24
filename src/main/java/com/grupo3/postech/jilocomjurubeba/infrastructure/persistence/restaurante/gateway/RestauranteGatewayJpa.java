package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.mapper.RestaurantePersistenceMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.repository.RestauranteRepository;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository.UsuarioRepository;

/**
 * Implementacao JPA do RestauranteGateway.
 *
 * <p>Traduz chamadas do dominio para operacoes JPA, usando o PersistenceMapper para converter entre
 * entidade de dominio e JPA entity.
 *
 * <p>Na Clean Architecture, esta classe e o "adapter" que conecta o port (RestauranteGateway) ao
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
public class RestauranteGatewayJpa implements RestauranteGateway {

    private final RestauranteRepository repository;
    private final RestaurantePersistenceMapper mapper;
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor com injecao de dependencia do repositorio, mapper e repositorio de Usuario.
     *
     * @param repository repositorio JPA de Restaurante
     * @param mapper mapper de conversao entre dominio e JPA entity
     * @param usuarioRepository repositorio JPA de Usuario para resolucao da referencia do dono
     */
    public RestauranteGatewayJpa(
            RestauranteRepository repository,
            RestaurantePersistenceMapper mapper,
            UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Persiste um restaurante no banco de dados.
     *
     * <p>Converte a entidade de dominio para JPA entity, resolve a referencia JPA do dono via
     * {@code findById} para evitar {@code LazyInitializationException}, e salva o registro.
     *
     * @param restaurante entidade de dominio a ser salva
     * @return entidade de dominio com ID persistido
     * @see RestauranteGateway#salvar(Restaurante)
     */
    @Override
    public Restaurante salvar(Restaurante restaurante) {
        RestauranteJpaEntity jpaEntity = mapper.toJpaEntity(restaurante);

        // Resolve a referencia JPA do dono (findById para evitar LazyInitializationException)
        if (restaurante.getDono() != null && restaurante.getDono().getId() != null) {
            UsuarioJpaEntity donoJpa =
                    usuarioRepository.findById(restaurante.getDono().getId()).orElse(null);
            jpaEntity.setDono(donoJpa);
        }

        RestauranteJpaEntity salvo = repository.save(jpaEntity);
        return mapper.toDomain(salvo);
    }

    /**
     * Busca um restaurante pelo identificador unico.
     *
     * @param id identificador do restaurante
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see RestauranteGateway#buscarPorId(Long)
     */
    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    /**
     * Busca um restaurante pelo nome (case insensitive).
     *
     * @param nome nome do restaurante
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see RestauranteGateway#buscarPorNome(String)
     */
    @Override
    public Optional<Restaurante> buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome).map(mapper::toDomain);
    }

    /**
     * Lista todos os restaurantes cadastrados no banco de dados.
     *
     * @return lista de entidades de dominio (pode ser vazia)
     * @see RestauranteGateway#listarTodos()
     */
    @Override
    public List<Restaurante> listarTodos() {
        return mapper.toDomainList(repository.findAll());
    }

    /**
     * Lista todos os restaurantes de um determinado dono.
     *
     * @param donoId identificador do usuario dono
     * @return lista de restaurantes pertencentes ao dono (pode ser vazia)
     * @see RestauranteGateway#listarPorDono(Long)
     */
    @Override
    public List<Restaurante> listarPorDono(Long donoId) {
        return mapper.toDomainList(repository.findByDonoId(donoId));
    }

    /**
     * Remove fisicamente um restaurante pelo ID.
     *
     * <p>Nota: na pratica, o sistema usa soft delete via {@code desativar()}. Este metodo existe
     * para compatibilidade com o contrato do gateway.
     *
     * @param id identificador do restaurante a ser removido
     * @see RestauranteGateway#deletar(Long)
     */
    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica se existe um restaurante com o nome informado (case insensitive).
     *
     * @param nome nome a ser verificado
     * @return {@code true} se ja existe um registro com esse nome
     * @see RestauranteGateway#existePorNome(String)
     */
    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }

    /**
     * Verifica se existe um restaurante com o nome informado, excluindo um ID especifico.
     *
     * <p>Utilizado na atualizacao para garantir unicidade de nome sem conflitar com o proprio
     * registro. A comparacao e case insensitive.
     *
     * @param nome nome a ser verificado
     * @param id identificador a ser excluido da verificacao
     * @return {@code true} se existe outro registro com o mesmo nome
     * @see RestauranteGateway#existePorNomeEIdDiferente(String, Long)
     */
    @Override
    public boolean existePorNomeEIdDiferente(String nome, Long id) {
        return repository.existsByNomeIgnoreCaseAndIdNot(nome, id);
    }
}
