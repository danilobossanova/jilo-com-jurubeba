package com.grupo3.postech.jilocomjurubeba.domain.valueobject;

import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("Deve criar email valido")
    void deveCriarEmailValido() {

        Email email = new Email("usuario@email.com");

        assertThat(email.getEmail()).isEqualTo("usuario@email.com");
    }

    @Test
    @DisplayName("Deve lancar excecao quando email for nulo")
    void deveLancarExcecaoQuandoEmailNulo() {

        assertThatThrownBy(() -> new Email(null))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessageContaining("Email inválido");
    }

    @Test
    @DisplayName("Deve lancar excecao quando email nao possuir arroba")
    void deveLancarExcecaoQuandoEmailSemArroba() {

        assertThatThrownBy(() -> new Email("usuarioemail.com"))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessageContaining("Email inválido");
    }

    @Test
    @DisplayName("Emails iguais devem ser iguais")
    void deveSerIgualQuandoEmailsIguais() {

        Email email1 = new Email("teste@email.com");
        Email email2 = new Email("teste@email.com");

        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    @DisplayName("Emails diferentes devem ser diferentes")
    void deveSerDiferenteQuandoEmailsDiferentes() {

        Email email1 = new Email("teste@email.com");
        Email email2 = new Email("outro@email.com");

        assertThat(email1).isNotEqualTo(email2);
    }
}
