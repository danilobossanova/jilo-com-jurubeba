package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AuthRequest(@JsonAlias({"username", "user", "login"}) String email, String senha) {}
