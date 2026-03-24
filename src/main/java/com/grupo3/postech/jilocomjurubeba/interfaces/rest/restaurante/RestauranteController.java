package com.grupo3.postech.jilocomjurubeba.interfaces.rest.restaurante;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.AtualizarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.BuscarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.CriarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.DeletarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.ListarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante.AtualizarRestauranteRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante.CriarRestauranteRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.restaurante.RestauranteResponse;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.restaurante.RestauranteRestMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para operacoes CRUD de Restaurante.
 *
 * <p>Thin controller que delega toda logica para os casos de uso. O fluxo de cada endpoint e:
 *
 * <pre>
 * Request -> Mapper -> Input -> UseCase.executar() -> Output -> Mapper -> Response
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
@RequestMapping("/v1/restaurantes")
@Tag(name = "Restaurantes", description = "Gerenciamento de restaurantes do sistema")
public class RestauranteController {

    private static final Logger log = LoggerFactory.getLogger(RestauranteController.class);

    private final CriarRestauranteUseCase criarRestauranteUseCase;
    private final BuscarRestauranteUseCase buscarRestauranteUseCase;
    private final ListarRestauranteUseCase listarRestauranteUseCase;
    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;
    private final DeletarRestauranteUseCase deletarRestauranteUseCase;
    private final RestauranteRestMapper mapper;

    /**
     * Construtor com injecao de dependencias.
     *
     * @param criarRestauranteUseCase caso de uso para criacao de restaurante
     * @param buscarRestauranteUseCase caso de uso para busca por ID
     * @param listarRestauranteUseCase caso de uso para listagem de restaurantes
     * @param atualizarRestauranteUseCase caso de uso para atualizacao de restaurante
     * @param deletarRestauranteUseCase caso de uso para desativacao (soft delete)
     * @param mapper mapper para conversao entre DTOs REST e Application
     */
    public RestauranteController(
            CriarRestauranteUseCase criarRestauranteUseCase,
            BuscarRestauranteUseCase buscarRestauranteUseCase,
            ListarRestauranteUseCase listarRestauranteUseCase,
            AtualizarRestauranteUseCase atualizarRestauranteUseCase,
            DeletarRestauranteUseCase deletarRestauranteUseCase,
            RestauranteRestMapper mapper) {
        this.criarRestauranteUseCase = criarRestauranteUseCase;
        this.buscarRestauranteUseCase = buscarRestauranteUseCase;
        this.listarRestauranteUseCase = listarRestauranteUseCase;
        this.atualizarRestauranteUseCase = atualizarRestauranteUseCase;
        this.deletarRestauranteUseCase = deletarRestauranteUseCase;
        this.mapper = mapper;
    }

    /**
     * Cria um novo restaurante no sistema.
     *
     * <p>Endpoint: {@code POST /v1/restaurantes}
     *
     * <p>Fluxo: Request -> RestauranteRestMapper.toInput() -> CriarRestauranteUseCase.executar() ->
     * RestauranteRestMapper.toResponse() -> Response
     *
     * @param request dados do restaurante a ser criado (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 201 (Created), header Location e corpo com {@link
     *     RestauranteResponse}
     */
    @PostMapping
    @Operation(summary = "Criar restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "422", description = "Violacao de regra de negocio")
            })
    public ResponseEntity<RestauranteResponse> criar(
            @Valid @RequestBody CriarRestauranteRequest request) {
        log.debug("Requisicao para criar restaurante: {}", request.nome());

        CriarRestauranteInput input = mapper.toInput(request);
        RestauranteOutput output = criarRestauranteUseCase.executar(input);
        RestauranteResponse response = mapper.toResponse(output);

        URI location = URI.create("/v1/restaurantes/" + output.id());
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Lista todos os restaurantes cadastrados.
     *
     * <p>Endpoint: {@code GET /v1/restaurantes}
     *
     * <p>Fluxo: ListarRestauranteUseCase.executar() -> RestauranteRestMapper.toResponseList() ->
     * Response
     *
     * @return {@link ResponseEntity} com status 200 (OK) e lista de {@link RestauranteResponse}
     */
    @GetMapping
    @Operation(
            summary = "Listar restaurantes",
            description = "Retorna todos os restaurantes cadastrados")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            })
    public ResponseEntity<List<RestauranteResponse>> listarTodos() {
        log.debug("Requisicao para listar restaurantes");

        List<RestauranteOutput> outputs = listarRestauranteUseCase.executar();
        List<RestauranteResponse> responses = mapper.toResponseList(outputs);

        return ResponseEntity.ok(responses);
    }

    /**
     * Busca um restaurante pelo seu identificador.
     *
     * <p>Endpoint: {@code GET /v1/restaurantes/{id}}
     *
     * <p>Fluxo: BuscarRestauranteUseCase.executar(id) -> RestauranteRestMapper.toResponse() ->
     * Response
     *
     * @param id identificador unico do restaurante
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link RestauranteResponse}
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar restaurante por ID",
            description = "Retorna um restaurante pelo seu identificador")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
                @ApiResponse(responseCode = "404", description = "Restaurante nao encontrado")
            })
    public ResponseEntity<RestauranteResponse> buscarPorId(@PathVariable Long id) {
        log.debug("Requisicao para buscar restaurante por id: {}", id);

        RestauranteOutput output = buscarRestauranteUseCase.executar(id);
        RestauranteResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um restaurante existente.
     *
     * <p>Endpoint: {@code PUT /v1/restaurantes/{id}?donoId={donoId}}
     *
     * <p>Apenas o dono do restaurante pode realizar a atualizacao. O {@code donoId} e validado pelo
     * caso de uso.
     *
     * <p>Fluxo: Request -> RestauranteRestMapper.toInput(id, donoId, request) ->
     * AtualizarRestauranteUseCase.executar() -> RestauranteRestMapper.toResponse() -> Response
     *
     * @param id identificador unico do restaurante a ser atualizado
     * @param donoId identificador do usuario dono (query param para validacao de propriedade)
     * @param request dados de atualizacao (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link RestauranteResponse}
     *     atualizado
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar restaurante",
            description = "Atualiza um restaurante existente. Apenas o dono pode atualizar.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Restaurante atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Restaurante nao encontrado"),
                @ApiResponse(
                        responseCode = "422",
                        description = "Violacao de regra de negocio (ex: nao e o dono)")
            })
    public ResponseEntity<RestauranteResponse> atualizar(
            @PathVariable Long id,
            @RequestParam Long donoId,
            @Valid @RequestBody AtualizarRestauranteRequest request) {
        log.debug("Requisicao para atualizar restaurante id: {} por donoId: {}", id, donoId);

        AtualizarRestauranteInput input = mapper.toInput(id, donoId, request);
        RestauranteOutput output = atualizarRestauranteUseCase.executar(input);
        RestauranteResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Desativa (soft delete) um restaurante.
     *
     * <p>Endpoint: {@code DELETE /v1/restaurantes/{id}}
     *
     * <p>Fluxo: DeletarRestauranteUseCase.executar(id) -> Response 204 No Content
     *
     * <p>O registro nao e removido fisicamente do banco; o campo {@code ativo} e definido como
     * {@code false}.
     *
     * @param id identificador unico do restaurante a ser desativado
     * @return {@link ResponseEntity} com status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Desativar restaurante",
            description = "Desativa (soft delete) um restaurante")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Restaurante desativado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Restaurante nao encontrado")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.debug("Requisicao para desativar restaurante id: {}", id);

        deletarRestauranteUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }
}
