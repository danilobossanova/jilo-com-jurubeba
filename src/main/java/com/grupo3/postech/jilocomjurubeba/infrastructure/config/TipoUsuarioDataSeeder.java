package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository.TipoUsuarioRepository;

/**
 * Seeder que popula os tipos de usuario padrao no startup da aplicacao.
 *
 * <p>Cria os 3 tipos iniciais (MASTER, DONO_RESTAURANTE, CLIENTE) caso nao existam. E idempotente:
 * pode rodar multiplas vezes sem duplicar dados.
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
@Order(1)
public class TipoUsuarioDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TipoUsuarioDataSeeder.class);

    private final TipoUsuarioRepository tipoUsuarioRepository;

    /**
     * Construtor com injecao de dependencia do repositorio de TipoUsuario.
     *
     * @param tipoUsuarioRepository repositorio JPA de TipoUsuario
     */
    public TipoUsuarioDataSeeder(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    /**
     * Executa a carga inicial dos tipos de usuario padrao na inicializacao da aplicacao.
     *
     * <p>Cria os tipos MASTER, DONO_RESTAURANTE e CLIENTE caso ainda nao existam no banco. E
     * idempotente e pode ser executado multiplas vezes sem duplicar dados.
     *
     * @param args argumentos da linha de comando (nao utilizados)
     */
    @Override
    public void run(String... args) {
        criarSeNaoExistir("MASTER", "Administrador do sistema com acesso total");
        criarSeNaoExistir("DONO_RESTAURANTE", "Proprietario de restaurante");
        criarSeNaoExistir("CLIENTE", "Cliente consumidor do restaurante");

        log.info("Tipos de usuario padrao verificados/criados com sucesso");
    }

    /**
     * Cria um tipo de usuario no banco caso nao exista um com o mesmo nome.
     *
     * <p>Verifica a existencia pelo nome antes de criar, garantindo idempotencia. Registra log
     * informativo de cada criacao ou pulo.
     *
     * @param nome nome do tipo de usuario (ex: MASTER, DONO_RESTAURANTE, CLIENTE)
     * @param descricao descricao textual do tipo de usuario
     */
    private void criarSeNaoExistir(String nome, String descricao) {
        if (!tipoUsuarioRepository.existsByNome(nome)) {
            TipoUsuarioJpaEntity entity = new TipoUsuarioJpaEntity();
            entity.setNome(nome);
            entity.setDescricao(descricao);
            entity.setAtivo(true);
            tipoUsuarioRepository.save(entity);
            log.info("Tipo de usuario '{}' criado", nome);
        } else {
            log.debug("Tipo de usuario '{}' ja existe, pulando criacao", nome);
        }
    }
}
