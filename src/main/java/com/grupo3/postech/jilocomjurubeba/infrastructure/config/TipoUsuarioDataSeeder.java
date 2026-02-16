package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository.TipoUsuarioRepository;

/**
 * Seeder que popula os tipos de usuario padrao no startup da aplicacao.
 *
 * <p>Cria os 3 tipos iniciais (MASTER, DONO_RESTAURANTE, CLIENTE) caso nao existam. E idempotente:
 * pode rodar multiplas vezes sem duplicar dados.
 *
 * @author Danilo Fernando
 */
@Component
public class TipoUsuarioDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TipoUsuarioDataSeeder.class);

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioDataSeeder(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public void run(String... args) {
        criarSeNaoExistir("MASTER", "Administrador do sistema com acesso total");
        criarSeNaoExistir("DONO_RESTAURANTE", "Proprietario de restaurante");
        criarSeNaoExistir("CLIENTE", "Cliente consumidor do restaurante");

        log.info("Tipos de usuario padrao verificados/criados com sucesso");
    }

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
