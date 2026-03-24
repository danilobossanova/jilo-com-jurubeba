package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository.TipoUsuarioRepository;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.mapper.UsuarioPersistenceMapper;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository.UsuarioRepository;

/**
 * Implementacao JPA do UsuarioGateway.
 *
 * <p>Traduz chamadas do dominio para operacoes JPA, usando o PersistenceMapper para converter entre
 * entidade de dominio e JPA entity.
 *
 * <p>Na Clean Architecture, esta classe e o "adapter" que conecta o port (UsuarioGateway) ao
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
public class UsuarioGatewayJpa implements UsuarioGateway {

    private final UsuarioRepository repository;
    private final UsuarioPersistenceMapper mapper;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    /**
     * Construtor com injecao de dependencia do repositorio, mapper e repositorio de TipoUsuario.
     *
     * @param repository repositorio JPA de Usuario
     * @param mapper mapper de conversao entre dominio e JPA entity
     * @param tipoUsuarioRepository repositorio JPA de TipoUsuario para resolucao de referencias
     */
    public UsuarioGatewayJpa(
            UsuarioRepository repository,
            UsuarioPersistenceMapper mapper,
            TipoUsuarioRepository tipoUsuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    /**
     * Persiste um usuario no banco de dados.
     *
     * <p>Converte a entidade de dominio para JPA entity, resolve a referencia JPA do TipoUsuario
     * via {@code findById} para evitar {@code LazyInitializationException}, salva o registro e
     * re-busca para garantir que associacoes lazy estejam carregadas.
     *
     * @param usuario entidade de dominio a ser salva
     * @return entidade de dominio com ID persistido e associacoes carregadas
     * @see UsuarioGateway#salvar(Usuario)
     */
    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity jpaEntity = mapper.toJpaEntity(usuario);

        // Resolve a referencia JPA do TipoUsuario (findById para evitar
        // LazyInitializationException)
        if (usuario.getTipoUsuario() != null && usuario.getTipoUsuario().getId() != null) {
            TipoUsuarioJpaEntity tipoUsuarioJpa =
                    tipoUsuarioRepository.findById(usuario.getTipoUsuario().getId()).orElse(null);
            jpaEntity.setTipoUsuario(tipoUsuarioJpa);
        }

        UsuarioJpaEntity salvo = repository.save(jpaEntity);
        // Re-busca para garantir que associacoes lazy estejam carregadas
        UsuarioJpaEntity completo = repository.findById(salvo.getId()).orElse(salvo);
        return mapper.toDomain(completo);
    }

    /**
     * Busca um usuario pelo identificador unico.
     *
     * @param id identificador do usuario
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see UsuarioGateway#buscarPorId(Long)
     */
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    /**
     * Busca um usuario pelo CPF.
     *
     * @param cpf CPF do usuario (somente digitos)
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see UsuarioGateway#buscarPorCpf(String)
     */
    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf).map(mapper::toDomain);
    }

    /**
     * Busca um usuario pelo email (case insensitive).
     *
     * @param email email do usuario
     * @return {@link Optional} contendo a entidade de dominio ou vazio se nao encontrado
     * @see UsuarioGateway#buscarPorEmail(String)
     */
    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(mapper::toDomain);
    }

    /**
     * Lista todos os usuarios cadastrados no banco de dados.
     *
     * @return lista de entidades de dominio (pode ser vazia)
     * @see UsuarioGateway#listarTodos()
     */
    @Override
    public List<Usuario> listarTodos() {
        return mapper.toDomainList(repository.findAll());
    }

    /**
     * Remove fisicamente um usuario pelo ID.
     *
     * <p>Nota: na pratica, o sistema usa soft delete via {@code desativar()}. Este metodo existe
     * para compatibilidade com o contrato do gateway.
     *
     * @param id identificador do usuario a ser removido
     * @see UsuarioGateway#deletar(Long)
     */
    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica se existe um usuario com o CPF informado.
     *
     * @param cpf CPF a ser verificado (somente digitos)
     * @return {@code true} se ja existe um registro com esse CPF
     * @see UsuarioGateway#existePorCpf(String)
     */
    @Override
    public boolean existePorCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    /**
     * Verifica se existe um usuario com o email informado (case insensitive).
     *
     * @param email email a ser verificado
     * @return {@code true} se ja existe um registro com esse email
     * @see UsuarioGateway#existePorEmail(String)
     */
    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    /**
     * Verifica se existe um usuario com o CPF informado, excluindo um ID especifico.
     *
     * <p>Utilizado na atualizacao para garantir unicidade de CPF sem conflitar com o proprio
     * registro.
     *
     * @param cpf CPF a ser verificado
     * @param id identificador a ser excluido da verificacao
     * @return {@code true} se existe outro registro com o mesmo CPF
     * @see UsuarioGateway#existePorCpfEIdDiferente(String, Long)
     */
    @Override
    public boolean existePorCpfEIdDiferente(String cpf, Long id) {
        return repository.existsByCpfAndIdNot(cpf, id);
    }

    /**
     * Verifica se existe um usuario com o email informado, excluindo um ID especifico.
     *
     * <p>Utilizado na atualizacao para garantir unicidade de email sem conflitar com o proprio
     * registro. A comparacao e case insensitive.
     *
     * @param email email a ser verificado
     * @param id identificador a ser excluido da verificacao
     * @return {@code true} se existe outro registro com o mesmo email
     * @see UsuarioGateway#existePorEmailEIdDiferente(String, Long)
     */
    @Override
    public boolean existePorEmailEIdDiferente(String email, Long id) {
        return repository.existsByEmailIgnoreCaseAndIdNot(email, id);
    }
}
