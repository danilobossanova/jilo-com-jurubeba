package com.grupo3.postech.jilocomjurubeba.interfaces.rest.cardapio;

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

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.AtualizarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.BuscarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.CriarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.DeletarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.ListarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio.AtualizarCardapioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio.CardapioResponse;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.cardapio.CriarCardapioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.cardapio.CardapioRestMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para operacoes CRUD de Cardapio.
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
@RequestMapping("/v1/cardapios")
@Tag(name = "Cardapios", description = "Gerenciamento de itens de cardapio do sistema")
public class CardapioController {

    private static final Logger log = LoggerFactory.getLogger(CardapioController.class);

    private final CriarCardapioUseCase criarCardapioUseCase;
    private final BuscarCardapioUseCase buscarCardapioUseCase;
    private final ListarCardapioUseCase listarCardapioUseCase;
    private final AtualizarCardapioUseCase atualizarCardapioUseCase;
    private final DeletarCardapioUseCase deletarCardapioUseCase;
    private final CardapioRestMapper mapper;

    /**
     * Construtor com injecao de dependencias.
     *
     * @param criarCardapioUseCase caso de uso para criacao de item de cardapio
     * @param buscarCardapioUseCase caso de uso para busca por ID
     * @param listarCardapioUseCase caso de uso para listagem de itens de cardapio
     * @param atualizarCardapioUseCase caso de uso para atualizacao de item de cardapio
     * @param deletarCardapioUseCase caso de uso para desativacao (soft delete)
     * @param mapper mapper para conversao entre DTOs REST e Application
     */
    public CardapioController(
            CriarCardapioUseCase criarCardapioUseCase,
            BuscarCardapioUseCase buscarCardapioUseCase,
            ListarCardapioUseCase listarCardapioUseCase,
            AtualizarCardapioUseCase atualizarCardapioUseCase,
            DeletarCardapioUseCase deletarCardapioUseCase,
            CardapioRestMapper mapper) {
        this.criarCardapioUseCase = criarCardapioUseCase;
        this.buscarCardapioUseCase = buscarCardapioUseCase;
        this.listarCardapioUseCase = listarCardapioUseCase;
        this.atualizarCardapioUseCase = atualizarCardapioUseCase;
        this.deletarCardapioUseCase = deletarCardapioUseCase;
        this.mapper = mapper;
    }

    /**
     * Cria um novo item de cardapio no sistema.
     *
     * <p>Endpoint: {@code POST /v1/cardapios}
     *
     * <p>Fluxo: Request -> CardapioRestMapper.toInput() -> CriarCardapioUseCase.executar() ->
     * CardapioRestMapper.toResponse() -> Response
     *
     * @param request dados do item de cardapio a ser criado (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 201 (Created), header Location e corpo com {@link
     *     CardapioResponse}
     */
    @PostMapping
    @Operation(
            summary = "Criar item de cardapio",
            description = "Cria um novo item de cardapio no sistema")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Item de cardapio criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "422", description = "Violacao de regra de negocio")
            })
    public ResponseEntity<CardapioResponse> criar(
            @Valid @RequestBody CriarCardapioRequest request) {
        log.debug("Requisicao para criar item de cardapio: {}", request.nome());

        CriarCardapioInput input = mapper.toInput(request);
        CardapioOutput output = criarCardapioUseCase.executar(input);
        CardapioResponse response = mapper.toResponse(output);

        URI location = URI.create("/v1/cardapios/" + output.id());
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Lista todos os itens de cardapio cadastrados.
     *
     * <p>Endpoint: {@code GET /v1/cardapios}
     *
     * <p>Fluxo: ListarCardapioUseCase.executar() -> CardapioRestMapper.toResponseList() -> Response
     *
     * @return {@link ResponseEntity} com status 200 (OK) e lista de {@link CardapioResponse}
     */
    @GetMapping
    @Operation(
            summary = "Listar itens de cardapio",
            description = "Retorna todos os itens de cardapio cadastrados")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            })
    public ResponseEntity<List<CardapioResponse>> listarTodos() {
        log.debug("Requisicao para listar itens de cardapio");

        List<CardapioOutput> outputs = listarCardapioUseCase.executar();
        List<CardapioResponse> responses = mapper.toResponseList(outputs);

        return ResponseEntity.ok(responses);
    }

    /**
     * Busca um item de cardapio pelo seu identificador.
     *
     * <p>Endpoint: {@code GET /v1/cardapios/{id}}
     *
     * <p>Fluxo: BuscarCardapioUseCase.executar(id) -> CardapioRestMapper.toResponse() -> Response
     *
     * @param id identificador unico do item de cardapio
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link CardapioResponse}
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar item de cardapio por ID",
            description = "Retorna um item de cardapio pelo seu identificador")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Item encontrado"),
                @ApiResponse(responseCode = "404", description = "Item nao encontrado")
            })
    public ResponseEntity<CardapioResponse> buscarPorId(@PathVariable Long id) {
        log.debug("Requisicao para buscar item de cardapio por id: {}", id);

        CardapioOutput output = buscarCardapioUseCase.executar(id);
        CardapioResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um item de cardapio existente.
     *
     * <p>Endpoint: {@code PUT /v1/cardapios/{id}?donoId={donoId}}
     *
     * <p>Apenas o dono do restaurante ao qual o item pertence pode realizar a atualizacao. O {@code
     * donoId} e validado pelo caso de uso.
     *
     * <p>Fluxo: Request -> CardapioRestMapper.toInput(id, donoId, request) ->
     * AtualizarCardapioUseCase.executar() -> CardapioRestMapper.toResponse() -> Response
     *
     * @param id identificador unico do item de cardapio a ser atualizado
     * @param donoId identificador do usuario dono do restaurante (query param)
     * @param request dados de atualizacao (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link CardapioResponse}
     *     atualizado
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar item de cardapio",
            description =
                    "Atualiza um item de cardapio existente."
                            + " Apenas o dono do restaurante pode atualizar.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Item nao encontrado"),
                @ApiResponse(
                        responseCode = "422",
                        description = "Violacao de regra de negocio (ex: nao e o dono)")
            })
    public ResponseEntity<CardapioResponse> atualizar(
            @PathVariable Long id,
            @RequestParam Long donoId,
            @Valid @RequestBody AtualizarCardapioRequest request) {
        log.debug("Requisicao para atualizar item de cardapio id: {} por donoId: {}", id, donoId);

        AtualizarCardapioInput input = mapper.toInput(id, donoId, request);
        CardapioOutput output = atualizarCardapioUseCase.executar(input);
        CardapioResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Desativa (soft delete) um item de cardapio.
     *
     * <p>Endpoint: {@code DELETE /v1/cardapios/{id}}
     *
     * <p>Fluxo: DeletarCardapioUseCase.executar(id) -> Response 204 No Content
     *
     * <p>O registro nao e removido fisicamente do banco; o campo {@code ativo} e definido como
     * {@code false}.
     *
     * @param id identificador unico do item de cardapio a ser desativado
     * @return {@link ResponseEntity} com status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Desativar item de cardapio",
            description = "Desativa (soft delete) um item de cardapio")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Item desativado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Item nao encontrado")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.debug("Requisicao para desativar item de cardapio id: {}", id);

        deletarCardapioUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }
}
