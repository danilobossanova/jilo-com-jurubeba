package com.grupo3.postech.jilocomjurubeba.domain.valueobject;

import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Email {

    private final String email;

    public Email(String email) {
        validarEmail(email);
        this.email = email;
    }

    private void validarEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new RegraDeNegocioException("Email inválido");
        }
    }
}
