package com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario;

public interface PasswordHashGateway {

  String hash(String rawPassword);
}
