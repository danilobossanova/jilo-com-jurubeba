package com.grupo3.postech.jilocomjurubeba.interfaces.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo3.postech.jilocomjurubeba.application.dto.saude.SaudeOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.saude.VerificarSaudeUseCase;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.saude.SaudeResponse;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.SaudeRestMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para verificação de saúde da aplicação.
 *
 * <p>Este controller demonstra o fluxo completo da Clean Architecture:
 *
 * <pre>
 * HTTP Request
 *     ↓
 * Controller (interfaces/rest)
 *     ↓ chama
 * UseCase (application)
 *     ↓ retorna
 * Output DTO
 *     ↓ mapper
 * Response DTO
 *     ↓
 * HTTP Response
 * </pre>
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
@RequestMapping("/v1/health")
@Tag(name = "Saúde", description = "Endpoints para verificação de saúde da API")
public class SaudeController {

    private static final Logger log = LoggerFactory.getLogger(SaudeController.class);

    private final VerificarSaudeUseCase verificarSaudeUseCase;
    private final SaudeRestMapper mapper;

    /**
     * Construtor com injecao de dependencias.
     *
     * @param verificarSaudeUseCase caso de uso para verificacao de saude da aplicacao
     * @param mapper mapper para conversao de SaudeOutput para SaudeResponse
     */
    public SaudeController(VerificarSaudeUseCase verificarSaudeUseCase, SaudeRestMapper mapper) {
        this.verificarSaudeUseCase = verificarSaudeUseCase;
        this.mapper = mapper;
    }

    /**
     * Verifica a saude da aplicacao.
     *
     * <p>Endpoint: {@code GET /v1/health}
     *
     * <p>Endpoint publico utilizado para health checks por load balancers, Kubernetes, Docker e
     * ferramentas de monitoramento.
     *
     * <p>Fluxo: VerificarSaudeUseCase.executar() -> SaudeRestMapper.toResponse() -> Response
     *
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link SaudeResponse} contendo
     *     status, versao e timestamp
     */
    @GetMapping
    @Operation(
            summary = "Verificar saúde da API",
            description = "Retorna o status atual da aplicação, versão e timestamp")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Aplicação saudável"),
                @ApiResponse(responseCode = "503", description = "Aplicação com problemas")
            })
    public ResponseEntity<SaudeResponse> verificarSaude() {
        log.debug("Requisição de verificação de saúde recebida");

        // 1. Executa o caso de uso
        SaudeOutput output = verificarSaudeUseCase.executar();

        // 2. Converte para response
        SaudeResponse response = mapper.toResponse(output);

        // 3. Retorna resposta HTTP
        log.debug("Saúde verificada: status={}", response.status());
        return ResponseEntity.ok(response);
    }
}
