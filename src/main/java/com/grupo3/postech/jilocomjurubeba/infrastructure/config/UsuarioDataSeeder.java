package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.repository.TipoUsuarioRepository;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository.UsuarioRepository;

/**
 * Seeder que popula o usuario administrador MASTER no startup da aplicacao.
 *
 * <p>Cria o usuario admin padrao caso nao exista, utilizando as credenciais de desenvolvimento. E
 * idempotente: pode rodar multiplas vezes sem duplicar dados.
 *
 * <p>Depende do {@link TipoUsuarioDataSeeder} para que o tipo MASTER ja exista. A anotacao
 * {@code @Order(2)} garante execucao apos o seeder de tipos (Order 1).
 *
 * <p><b>Credenciais de desenvolvimento:</b>
 *
 * <ul>
 *   <li>Email: {@code admin@jilocomjurubeba.com}
 *   <li>Senha: {@code admin123}
 * </ul>
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
@Order(2)
public class UsuarioDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UsuarioDataSeeder.class);

    private static final String ADMIN_EMAIL = "admin@jilocomjurubeba.com";
    private static final String ADMIN_SENHA = "admin123";
    private static final String ADMIN_NOME = "ADMIN MASTER";
    private static final String ADMIN_CPF = "52998224725";
    private static final String ADMIN_TELEFONE = "11999999999";

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor com injecao de dependencias.
     *
     * @param usuarioRepository repositorio JPA de Usuario
     * @param tipoUsuarioRepository repositorio JPA de TipoUsuario
     * @param passwordEncoder encoder de senhas (BCrypt)
     */
    public UsuarioDataSeeder(
            UsuarioRepository usuarioRepository,
            TipoUsuarioRepository tipoUsuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria o usuario administrador MASTER caso nao exista no banco.
     *
     * @param args argumentos da linha de comando (nao utilizados)
     */
    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByEmailIgnoreCase(ADMIN_EMAIL)) {
            log.debug("Usuario admin '{}' ja existe, pulando criacao", ADMIN_EMAIL);
            return;
        }

        TipoUsuarioJpaEntity tipoMaster = tipoUsuarioRepository.findByNome("MASTER").orElse(null);

        if (tipoMaster == null) {
            log.warn("Tipo MASTER nao encontrado. " + "Usuario admin nao sera criado.");
            return;
        }

        UsuarioJpaEntity admin = new UsuarioJpaEntity();
        admin.setNome(ADMIN_NOME);
        admin.setCpf(ADMIN_CPF);
        admin.setEmail(ADMIN_EMAIL);
        admin.setTelefone(ADMIN_TELEFONE);
        admin.setTipoUsuario(tipoMaster);
        admin.setSenha(passwordEncoder.encode(ADMIN_SENHA));
        admin.setAtivo(true);

        usuarioRepository.save(admin);
        log.info("Usuario admin '{}' criado com sucesso (tipo: MASTER)", ADMIN_EMAIL);
    }
}
