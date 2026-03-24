package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio.Cardapio;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.cardapio.CardapioGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity.CardapioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.mapper.CardapioPersistenceMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.repository.CardapioRepository;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.repository.RestauranteRepository;

/**
 * Implementacao JPA do CardapioGateway.
 *
 * <p>Traduz chamadas do dominio para operacoes JPA, usando o PersistenceMapper para converter entre
 * entidade de dominio e JPA entity.
 *
 * <p>Na Clean Architecture, esta classe e o "adapter" que conecta o port (CardapioGateway) ao
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
public class CardapioGatewayJpa implements CardapioGateway {

    private final CardapioRepository repository;
    private final CardapioPersistenceMapper mapper;
    private final RestauranteRepository restauranteRepository;

    /**
     * Construtor com injecao de dependencia do repositorio, mapper e repositorio de Restaurante.
     *
     * @param repository repositorio JPA de Cardapio
     * @param mapper mapper de conversao entre dominio e JPA entity
     * @param restauranteRepository repositorio JPA de Restaurante para resolucao de referencias
     */
    public CardapioGatewayJpa(
            CardapioRepository repository,
            CardapioPersistenceMapper mapper,
            RestauranteRepository restauranteRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.restauranteRepository = restauranteRepository;
    }

    /**
     * Persiste um item de cardapio no banco de dados.
     *
     * <p>Converte a entidade de dominio para JPA entity, resolve a referencia JPA do Restaurante
     * via {@code findById} para evitar {@code LazyInitializationException}, e salva o registro.
     *
     * @param cardapio entidade de dominio a ser salva
     * @return entidade de dominio com ID persistido
     * @see CardapioGateway#salvar(Cardapio)
     */
    @Override
    public Cardapio salvar(Cardapio cardapio) {
        CardapioJpaEntity jpaEntity = mapper.toJpaEntity(cardapio);

        // Resolve a referencia JPA do Restaurante (findById para evitar
        // LazyInitializationException)
        if (cardapio.getRestaurante() != null && cardapio.getRestaurante().getId() != null) {
            RestauranteJpaEntity restauranteJpa =
                    restauranteRepository.findById(cardapio.getRestaurante().getId()).orElse(null);
            jpaEntity.setRestaurante(restauranteJpa);
        }

        CardapioJpaEntity salvo = repository.save(jpaEntity);
        return mapper.toDomain(salvo);
    }

    /**
     * Busca um item de cardapio pelo identificador unico.
     *
     * @param id identificador do item de cardapio
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see CardapioGateway#buscarPorId(Long)
     */
    @Override
    public Optional<Cardapio> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    /**
     * Busca um item de cardapio pelo nome (case insensitive).
     *
     * @param nome nome do item de cardapio
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see CardapioGateway#buscarPorNome(String)
     */
    @Override
    public Optional<Cardapio> buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome).map(mapper::toDomain);
    }

    /**
     * Lista todos os itens de cardapio cadastrados no banco de dados.
     *
     * @return lista de entidades de dominio (pode ser vazia)
     * @see CardapioGateway#listarTodos()
     */
    @Override
    public List<Cardapio> listarTodos() {
        return mapper.toDomainList(repository.findAll());
    }

    /**
     * Lista todos os itens de cardapio de um determinado restaurante.
     *
     * @param restauranteId identificador do restaurante
     * @return lista de itens de cardapio do restaurante (pode ser vazia)
     * @see CardapioGateway#listarPorRestaurante(Long)
     */
    @Override
    public List<Cardapio> listarPorRestaurante(Long restauranteId) {
        return mapper.toDomainList(repository.findByRestauranteId(restauranteId));
    }

    /**
     * Remove fisicamente um item de cardapio pelo ID.
     *
     * <p>Nota: na pratica, o sistema usa soft delete via {@code desativar()}. Este metodo existe
     * para compatibilidade com o contrato do gateway.
     *
     * @param id identificador do item de cardapio a ser removido
     * @see CardapioGateway#deletar(Long)
     */
    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica se existe um item de cardapio com o nome informado (case insensitive).
     *
     * @param nome nome a ser verificado
     * @return {@code true} se ja existe um registro com esse nome
     * @see CardapioGateway#existePorNome(String)
     */
    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }
}
