package com.grupo3.postech.jilocomjurubeba.domain.valueobject;

import java.util.Objects;

import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;

/**
 * Value Object imutavel que representa um CPF (Cadastro de Pessoa Fisica).
 *
 * <p>Realiza validacao basica no construtor: nao nulo, 11 digitos numericos, rejeita CPFs com todos
 * os digitos iguais (ex: {@code "00000000000"}, {@code "11111111111"}, etc). Caracteres nao
 * numericos (pontos, tracos) sao removidos automaticamente antes da validacao.
 *
 * <p>Armazena internamente apenas os 11 digitos numericos, sem formatacao. A igualdade entre dois
 * objetos {@code Cpf} e determinada pelo conteudo do numero, nao pela referencia.
 *
 * <p>Exemplo de uso:
 *
 * <pre>{@code
 * Cpf cpf = new Cpf("123.456.789-00");   // formato com pontuacao
 * Cpf cpf2 = new Cpf("12345678900");      // formato somente digitos
 * cpf.getNumero(); // retorna "12345678900"
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
public class Cpf {

    private final String numero;

    /**
     * Cria um novo CPF a partir de uma string.
     *
     * <p>Aceita formatos com ou sem pontuacao (ex: "123.456.789-00" ou "12345678900"). Caracteres
     * nao numericos sao removidos antes da validacao.
     *
     * @param cpf string contendo o CPF
     * @throws ValidacaoException se o CPF for nulo, vazio, nao tiver 11 digitos ou tiver todos os
     *     digitos iguais
     */
    public Cpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new ValidacaoException("CPF e obrigatorio");
        }

        String apenasNumeros = cpf.replaceAll("\\D", "");

        if (apenasNumeros.length() != 11) {
            throw new ValidacaoException("CPF deve conter 11 digitos");
        }

        if (todosDigitosIguais(apenasNumeros)) {
            throw new ValidacaoException("CPF com todos os digitos iguais e invalido");
        }

        this.numero = apenasNumeros;
    }

    /**
     * Retorna o numero do CPF (apenas digitos).
     *
     * @return numero do CPF com 11 digitos
     */
    public String getNumero() {
        return numero;
    }

    private boolean todosDigitosIguais(String cpf) {
        char primeiro = cpf.charAt(0);
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != primeiro) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compara dois CPFs pelo conteudo do numero.
     *
     * <p>Como Value Object, a igualdade e baseada no valor (numero do CPF), nao na referencia do
     * objeto.
     *
     * @param o objeto a ser comparado
     * @return {@code true} se o objeto for um {@code Cpf} com o mesmo numero
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf cpf)) return false;
        return Objects.equals(numero, cpf.numero);
    }

    /**
     * Retorna o hash code baseado no numero do CPF.
     *
     * @return hash code calculado a partir dos 11 digitos do CPF
     */
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    /**
     * Retorna o numero do CPF como string (apenas digitos, sem formatacao).
     *
     * @return string com os 11 digitos do CPF (ex: {@code "12345678900"})
     */
    @Override
    public String toString() {
        return numero;
    }
}
