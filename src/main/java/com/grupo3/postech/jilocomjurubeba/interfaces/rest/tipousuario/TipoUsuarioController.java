package com.grupo3.postech.jilocomjurubeba.interfaces.rest.tipousuario;

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
import org.springframework.web.bind.annotation.RestController;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.AtualizarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.CriarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.AtualizarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.BuscarTipoUsuarioPorIdUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.CriarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.DeletarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.ListarTiposUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario.AtualizarTipoUsuarioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario.CriarTipoUsuarioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.tipousuario.TipoUsuarioResponse;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.tipousuario.TipoUsuarioRestMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para operacoes CRUD de TipoUsuario.
 *
 * <p>Thin controller que delega toda logica para os casos de uso. O fluxo de cada endpoint e:
 *
 * <pre>
 * Request → Mapper → Input → UseCase.executar() → Output → Mapper → Response
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
@RequestMapping("/v1/tipos-usuario")
@Tag(name = "Tipos de Usuario", description = "Gerenciamento de tipos de usuario do sistema")
public class TipoUsuarioController {

    private static final Logger log = LoggerFactory.getLogger(TipoUsuarioController.class);

    private final CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;
    private final BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase;
    private final ListarTiposUsuarioUseCase listarTiposUsuarioUseCase;
    private final AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase;
    private final DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase;
    private final TipoUsuarioRestMapper mapper;

    /**
     * Construtor com injecao de dependencias.
     *
     * @param criarTipoUsuarioUseCase caso de uso para criacao de tipo de usuario
     * @param buscarTipoUsuarioPorIdUseCase caso de uso para busca por ID
     * @param listarTiposUsuarioUseCase caso de uso para listagem de tipos de usuario
     * @param atualizarTipoUsuarioUseCase caso de uso para atualizacao de tipo de usuario
     * @param deletarTipoUsuarioUseCase caso de uso para desativacao (soft delete)
     * @param mapper mapper para conversao entre DTOs REST e Application
     */
    public TipoUsuarioController(
            CriarTipoUsuarioUseCase criarTipoUsuarioUseCase,
            BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase,
            ListarTiposUsuarioUseCase listarTiposUsuarioUseCase,
            AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase,
            DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase,
            TipoUsuarioRestMapper mapper) {
        this.criarTipoUsuarioUseCase = criarTipoUsuarioUseCase;
        this.buscarTipoUsuarioPorIdUseCase = buscarTipoUsuarioPorIdUseCase;
        this.listarTiposUsuarioUseCase = listarTiposUsuarioUseCase;
        this.atualizarTipoUsuarioUseCase = atualizarTipoUsuarioUseCase;
        this.deletarTipoUsuarioUseCase = deletarTipoUsuarioUseCase;
        this.mapper = mapper;
    }

    /**
     * Cria um novo tipo de usuario no sistema.
     *
     * <p>Endpoint: {@code POST /v1/tipos-usuario}
     *
     * <p>Fluxo: Request -> TipoUsuarioRestMapper.toInput() -> CriarTipoUsuarioUseCase.executar() ->
     * TipoUsuarioRestMapper.toResponse() -> Response
     *
     * @param request dados do tipo de usuario a ser criado (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 201 (Created), header Location e corpo com {@link
     *     TipoUsuarioResponse}
     */
    @PostMapping
    @Operation(summary = "Criar tipo de usuario", description = "Cria um novo tipo de usuario")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Tipo criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "422", description = "Nome ja existe")
            })
    public ResponseEntity<TipoUsuarioResponse> criar(
            @Valid @RequestBody CriarTipoUsuarioRequest request) {
        log.debug("Requisicao para criar tipo de usuario: {}", request.nome());

        CriarTipoUsuarioInput input = mapper.toInput(request);
        TipoUsuarioOutput output = criarTipoUsuarioUseCase.executar(input);
        TipoUsuarioResponse response = mapper.toResponse(output);

        URI location = URI.create("/v1/tipos-usuario/" + output.id());
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Lista todos os tipos de usuario cadastrados.
     *
     * <p>Endpoint: {@code GET /v1/tipos-usuario}
     *
     * <p>Fluxo: ListarTiposUsuarioUseCase.executar() -> TipoUsuarioRestMapper.toResponseList() ->
     * Response
     *
     * @return {@link ResponseEntity} com status 200 (OK) e lista de {@link TipoUsuarioResponse}
     */
    @GetMapping
    @Operation(
            summary = "Listar tipos de usuario",
            description = "Retorna todos os tipos de usuario cadastrados")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            })
    public ResponseEntity<List<TipoUsuarioResponse>> listarTodos() {
        log.debug("Requisicao para listar tipos de usuario");

        List<TipoUsuarioOutput> outputs = listarTiposUsuarioUseCase.executar();
        List<TipoUsuarioResponse> responses = mapper.toResponseList(outputs);

        return ResponseEntity.ok(responses);
    }

    /**
     * Busca um tipo de usuario pelo seu identificador.
     *
     * <p>Endpoint: {@code GET /v1/tipos-usuario/{id}}
     *
     * <p>Fluxo: BuscarTipoUsuarioPorIdUseCase.executar(id) -> TipoUsuarioRestMapper.toResponse() ->
     * Response
     *
     * @param id identificador unico do tipo de usuario
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link TipoUsuarioResponse}
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar tipo de usuario por ID",
            description = "Retorna um tipo de usuario pelo seu identificador")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
                @ApiResponse(responseCode = "404", description = "Tipo nao encontrado")
            })
    public ResponseEntity<TipoUsuarioResponse> buscarPorId(@PathVariable Long id) {
        log.debug("Requisicao para buscar tipo de usuario por id: {}", id);

        TipoUsuarioOutput output = buscarTipoUsuarioPorIdUseCase.executar(id);
        TipoUsuarioResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um tipo de usuario existente.
     *
     * <p>Endpoint: {@code PUT /v1/tipos-usuario/{id}}
     *
     * <p>Fluxo: Request -> TipoUsuarioRestMapper.toInput(id, request) ->
     * AtualizarTipoUsuarioUseCase.executar() -> TipoUsuarioRestMapper.toResponse() -> Response
     *
     * @param id identificador unico do tipo de usuario a ser atualizado
     * @param request dados de atualizacao (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link TipoUsuarioResponse}
     *     atualizado
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar tipo de usuario",
            description = "Atualiza um tipo de usuario existente")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Tipo atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Tipo nao encontrado"),
                @ApiResponse(responseCode = "422", description = "Nome ja existe")
            })
    public ResponseEntity<TipoUsuarioResponse> atualizar(
            @PathVariable Long id, @Valid @RequestBody AtualizarTipoUsuarioRequest request) {
        log.debug("Requisicao para atualizar tipo de usuario id: {}", id);

        AtualizarTipoUsuarioInput input = mapper.toInput(id, request);
        TipoUsuarioOutput output = atualizarTipoUsuarioUseCase.executar(input);
        TipoUsuarioResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Desativa (soft delete) um tipo de usuario.
     *
     * <p>Endpoint: {@code DELETE /v1/tipos-usuario/{id}}
     *
     * <p>Fluxo: DeletarTipoUsuarioUseCase.executar(id) -> Response 204 No Content
     *
     * <p>O registro nao e removido fisicamente do banco; o campo {@code ativo} e definido como
     * {@code false}.
     *
     * @param id identificador unico do tipo de usuario a ser desativado
     * @return {@link ResponseEntity} com status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Desativar tipo de usuario",
            description = "Desativa (soft delete) um tipo de usuario")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Tipo desativado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Tipo nao encontrado")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.debug("Requisicao para desativar tipo de usuario id: {}", id);

        deletarTipoUsuarioUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }
}
