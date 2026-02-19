package com.grupo3.postech.jilocomjurubeba.domain.valueobject;

import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Cpf {

    private final String cpf;

    public Cpf(String cpf){
        validarCpf(cpf);

        this.cpf = cpf;
    }

    private void validarCpf(String cpf) {
        if (cpf == null || !cpf.isBlank()) {
            throw new RegraDeNegocioException("cpf é obrigatório");
        }
    }
}
