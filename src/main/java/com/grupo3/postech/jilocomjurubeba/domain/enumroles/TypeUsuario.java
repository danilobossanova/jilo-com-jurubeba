package com.grupo3.postech.jilocomjurubeba.domain.enumroles;

public enum TypeUsuario {
    DONO_RESTAURANTE,
    CLIENTE;

    public boolean isDono() {
        return this == DONO_RESTAURANTE;
    }

    public boolean isCliente() {
        return this == CLIENTE;
    }
}
