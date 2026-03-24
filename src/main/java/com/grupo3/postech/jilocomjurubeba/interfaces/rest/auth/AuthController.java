package com.grupo3.postech.jilocomjurubeba.interfaces.rest.auth;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo3.postech.jilocomjurubeba.application.dto.autenticacao.AutenticarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.autenticacao.AutenticarUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.autenticacao.AutenticarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.auth.LoginRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.auth.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para autenticacao de usuarios.
 *
 * <p>Thin controller que delega toda logica para o caso de uso de autenticacao. Nao possui
 * dependencias de infrastructure — apenas de application e interfaces.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacao", description = "Endpoints de autenticacao e geracao de token JWT")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    /**
     * Construtor com injecao de dependencias.
     *
     * @param autenticarUsuarioUseCase caso de uso para autenticacao de usuario
     */
    public AuthController(AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    /**
     * Autentica um usuario com email e senha, retornando um token JWT.
     *
     * <p>Endpoint: {@code POST /auth/login}
     *
     * <p>Fluxo: LoginRequest -> AutenticarUsuarioInput -> AutenticarUsuarioUseCase.executar() ->
     * AutenticarUsuarioOutput -> LoginResponse
     *
     * @param request credenciais do usuario (email e senha, validados com Bean Validation)
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link LoginResponse} contendo
     *     o token JWT
     */
    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuario",
            description = "Autentica um usuario com email e senha, retornando um token JWT")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Autenticacao bem-sucedida"),
                @ApiResponse(responseCode = "422", description = "Credenciais invalidas")
            })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.debug("Requisicao de login para o email: {}", request.email());

        AutenticarUsuarioInput input = new AutenticarUsuarioInput(request.email(), request.senha());
        AutenticarUsuarioOutput output = autenticarUsuarioUseCase.executar(input);

        log.debug("Token gerado com sucesso para o email: {}", request.email());

        return ResponseEntity.ok(new LoginResponse(output.token()));
    }
}
