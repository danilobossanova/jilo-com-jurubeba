package com.grupo3.postech.jilocomjurubeba.interfaces.rest;

import com.grupo3.postech.jilocomjurubeba.application.dto.saude.SaudeOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.saude.VerificarSaudeUseCase;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.saude.SaudeResponse;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.SaudeRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para verificação de saúde da aplicação.
 *
 * Este controller demonstra o fluxo completo da Clean Architecture:
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
 * @author Danilo Fernando
 */
@RestController
@RequestMapping("/v1/health")
@Tag(name = "Saúde", description = "Endpoints para verificação de saúde da API")
public class SaudeController {

    private static final Logger log = LoggerFactory.getLogger(SaudeController.class);

    private final VerificarSaudeUseCase verificarSaudeUseCase;
    private final SaudeRestMapper mapper;

    /**
     * Construtor com injeção de dependências.
     *
     * @param verificarSaudeUseCase caso de uso de verificação de saúde
     * @param mapper                mapper para conversão de DTOs
     */
    public SaudeController(
            VerificarSaudeUseCase verificarSaudeUseCase,
            SaudeRestMapper mapper
    ) {
        this.verificarSaudeUseCase = verificarSaudeUseCase;
        this.mapper = mapper;
    }

    /**
     * Verifica a saúde da aplicação.
     *
     * Este endpoint é usado para health checks por load balancers,
     * Kubernetes, Docker, etc.
     *
     * @return status de saúde da aplicação
     */
    @GetMapping
    @Operation(
            summary = "Verificar saúde da API",
            description = "Retorna o status atual da aplicação, versão e timestamp"
    )
    @ApiResponses(value = {
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
