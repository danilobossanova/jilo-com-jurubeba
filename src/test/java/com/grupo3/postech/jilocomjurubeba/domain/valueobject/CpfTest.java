package com.grupo3.postech.jilocomjurubeba.domain.valueobject;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CpfTest {

    @Test
    @DisplayName("Deve criar CPF valido")
    void deveCriarCpfValido() {

        Cpf cpf = new Cpf("12345678909");

        assertThat(cpf.getNumero()).isEqualTo("12345678909");
    }

    @Test
    @DisplayName("Deve remover mascara do CPF")
    void deveRemoverMascaraCpf() {

        Cpf cpf = new Cpf("123.456.789-09");

        assertThat(cpf.getNumero()).isEqualTo("12345678909");
    }

    @Test
    @DisplayName("Deve lancar excecao quando CPF for nulo")
    void deveLancarExcecaoQuandoCpfNulo() {

        assertThatThrownBy(() -> new Cpf(null))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessageContaining("cpf é obrigatório");
    }

    @Test
    @DisplayName("Deve lancar excecao quando CPF for vazio")
    void deveLancarExcecaoQuandoCpfVazio() {

        assertThatThrownBy(() -> new Cpf(" "))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessageContaining("cpf é obrigatório");
    }

    @Test
    @DisplayName("Deve lancar excecao quando CPF nao tiver 11 digitos")
    void deveLancarExcecaoQuandoCpfInvalido() {

        assertThatThrownBy(() -> new Cpf("123"))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessageContaining("cpf deve conter 11 dígitos");
    }

    @Test
    @DisplayName("Deve retornar CPF no toString")
    void deveRetornarCpfNoToString() {

        Cpf cpf = new Cpf("12345678909");

        assertThat(cpf.toString()).isEqualTo("12345678909");
    }

    @Test
    @DisplayName("CPFs iguais devem ser iguais")
    void deveSerIgualQuandoNumeroIgual() {

        Cpf cpf1 = new Cpf("12345678909");
        Cpf cpf2 = new Cpf("12345678909");

        assertThat(cpf1).isEqualTo(cpf2);
        assertThat(cpf1.hashCode()).isEqualTo(cpf2.hashCode());
    }

    @Test
    @DisplayName("CPFs diferentes devem ser diferentes")
    void deveSerDiferenteQuandoNumeroDiferente() {

        Cpf cpf1 = new Cpf("12345678909");
        Cpf cpf2 = new Cpf("98765432100");

        assertThat(cpf1).isNotEqualTo(cpf2);
    }
}
