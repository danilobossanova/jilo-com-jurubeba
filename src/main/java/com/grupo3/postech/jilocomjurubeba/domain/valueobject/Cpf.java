package com.grupo3.postech.jilocomjurubeba.domain.valueobject;

import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Cpf {

    private final String numero;

    public Cpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new RegraDeNegocioException("cpf é obrigatório");
        }

        // Remove caracteres não numéricos antes de salvar
        String apenasNumeros = cpf.replaceAll("\\D", "");

        if (apenasNumeros.length() != 11) {
            throw new RegraDeNegocioException("cpf deve conter 11 dígitos");
        }

        this.numero = apenasNumeros;
    }

    @Override
    public String toString() {
        return numero;
    }
}
