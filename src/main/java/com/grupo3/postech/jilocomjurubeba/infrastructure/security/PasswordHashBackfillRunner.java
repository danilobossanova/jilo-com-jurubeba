package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository.UsuarioRepository;

/**
 * Runner que verifica e converte senhas armazenadas em texto puro para BCrypt.
 *
 * <p>Executa na inicializacao da aplicacao (exceto em testes) e converte senhas que ainda nao
 * estejam no formato BCrypt. Util para migracao de dados legados.
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
@Profile("!test")
public class PasswordHashBackfillRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PasswordHashBackfillRunner.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor com injecao de dependencia do repositorio de usuarios e codificador de senhas.
     *
     * @param usuarioRepository repositorio JPA de Usuario
     * @param passwordEncoder codificador de senhas do Spring Security (BCrypt)
     */
    public PasswordHashBackfillRunner(
            UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executa o backfill de senhas na inicializacao da aplicacao.
     *
     * <p>Percorre todos os usuarios no banco de dados e converte senhas armazenadas em texto puro
     * para hash BCrypt. Senhas que ja estao no formato BCrypt (prefixo {@code $2a$}, {@code $2b$}
     * ou {@code $2y$}) sao ignoradas. A operacao e transacional para garantir consistencia.
     * Registra log de aviso com a quantidade de usuarios convertidos.
     *
     * @param args argumentos da aplicacao (nao utilizados)
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        int atualizados = 0;

        for (var usuario : usuarioRepository.findAll()) {
            String senha = usuario.getSenha();
            if (senha == null || senha.isBlank() || isBcrypt(senha)) {
                continue;
            }

            usuario.setSenha(passwordEncoder.encode(senha));
            atualizados++;
        }

        if (atualizados > 0) {
            log.warn(
                    "Backfill de senha executado: {} usuario(s) com senha em texto puro"
                            + " convertidos.",
                    atualizados);
        }
    }

    /**
     * Verifica se um valor de senha esta no formato BCrypt.
     *
     * <p>Checa se a string inicia com os prefixos padrao de hash BCrypt: {@code $2a$}, {@code $2b$}
     * ou {@code $2y$}.
     *
     * @param value valor da senha a ser verificado
     * @return {@code true} se a senha ja esta no formato BCrypt
     */
    private boolean isBcrypt(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
