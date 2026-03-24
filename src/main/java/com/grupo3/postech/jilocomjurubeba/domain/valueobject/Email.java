package com.grupo3.postech.jilocomjurubeba.domain.valueobject;

import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

/**
 * Value Object imutavel que representa um endereco de e-mail.
 *
 * <p>Realiza validacao basica no construtor: nao nulo, nao vazio, deve conter o caractere {@code
 * "@"} com pelo menos um caractere antes e depois. O endereco e normalizado para lowercase e sem
 * espacos nas extremidades (trim).
 *
 * <p>A igualdade entre dois objetos {@code Email} e determinada pelo conteudo do endereco
 * normalizado, nao pela referencia do objeto.
 *
 * <p>Exemplo de uso:
 *
 * <pre>{@code
 * Email email = new Email("Usuario@Exemplo.com");
 * email.getEndereco(); // retorna "usuario@exemplo.com"
 * }</pre>
 *
 * @see com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class Email {

    private final String endereco;

    /**
     * Cria um novo Email a partir de uma string.
     *
     * @param email string contendo o endereco de e-mail
     * @throws ValidacaoException se o e-mail for nulo, vazio ou nao contiver "@" com texto antes e
     *     depois
     */
    public Email(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidacaoException("Email e obrigatorio");
        }

        String normalizado = email.trim().toLowerCase();

        int posicaoArroba = normalizado.indexOf('@');
        if (posicaoArroba <= 0 || posicaoArroba >= normalizado.length() - 1) {
            throw new ValidacaoException("Email invalido");
        }

        this.endereco = normalizado;
    }

    /**
     * Retorna o endereco de e-mail normalizado (lowercase, sem espacos).
     *
     * @return endereco de e-mail
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Compara dois emails pelo conteudo do endereco normalizado.
     *
     * <p>Como Value Object, a igualdade e baseada no valor (endereco de e-mail), nao na referencia
     * do objeto. A comparacao e case-insensitive pois os enderecos sao normalizados para lowercase
     * no construtor.
     *
     * @param o objeto a ser comparado
     * @return {@code true} se o objeto for um {@code Email} com o mesmo endereco
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email that)) return false;
        return Objects.equals(endereco, that.endereco);
    }

    /**
     * Retorna o hash code baseado no endereco de e-mail normalizado.
     *
     * @return hash code calculado a partir do endereco
     */
    @Override
    public int hashCode() {
        return Objects.hash(endereco);
    }

    /**
     * Retorna o endereco de e-mail normalizado (lowercase, sem espacos).
     *
     * @return string do endereco de e-mail (ex: {@code "usuario@exemplo.com"})
     */
    @Override
    public String toString() {
        return endereco;
    }
}
