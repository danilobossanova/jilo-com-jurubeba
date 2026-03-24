package com.grupo3.postech.jilocomjurubeba.interfaces.rest.usuario;

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

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.CriarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.BuscarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.CriarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.DeletarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.ListarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.usuario.AtualizarUsuarioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.usuario.CriarUsuarioRequest;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.dto.usuario.UsuarioResponse;
import com.grupo3.postech.jilocomjurubeba.interfaces.rest.mapper.usuario.UsuarioRestMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para operacoes CRUD de Usuario.
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
@RequestMapping("/v1/usuarios")
@Tag(name = "Usuarios", description = "Gerenciamento de usuarios do sistema")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private final ListarUsuarioUseCase listarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final UsuarioRestMapper mapper;

    /**
     * Construtor com injecao de dependencias.
     *
     * @param criarUsuarioUseCase caso de uso para criacao de usuario
     * @param buscarUsuarioUseCase caso de uso para busca por ID
     * @param listarUsuarioUseCase caso de uso para listagem de usuarios
     * @param atualizarUsuarioUseCase caso de uso para atualizacao de usuario
     * @param deletarUsuarioUseCase caso de uso para desativacao (soft delete)
     * @param mapper mapper para conversao entre DTOs REST e Application
     */
    public UsuarioController(
            CriarUsuarioUseCase criarUsuarioUseCase,
            BuscarUsuarioUseCase buscarUsuarioUseCase,
            ListarUsuarioUseCase listarUsuarioUseCase,
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            DeletarUsuarioUseCase deletarUsuarioUseCase,
            UsuarioRestMapper mapper) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
        this.listarUsuarioUseCase = listarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
        this.mapper = mapper;
    }

    /**
     * Cria um novo usuario no sistema.
     *
     * <p>Endpoint: {@code POST /v1/usuarios}
     *
     * <p>Fluxo: Request -> UsuarioRestMapper.toInput() -> CriarUsuarioUseCase.executar() ->
     * UsuarioRestMapper.toResponse() -> Response
     *
     * @param request dados do usuario a ser criado (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 201 (Created), header Location e corpo com {@link
     *     UsuarioResponse}
     */
    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cria um novo usuario no sistema")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "422", description = "CPF ou email ja existe")
            })
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody CriarUsuarioRequest request) {
        log.debug("Requisicao para criar usuario: {}", request.nome());

        CriarUsuarioInput input = mapper.toInput(request);
        UsuarioOutput output = criarUsuarioUseCase.executar(input);
        UsuarioResponse response = mapper.toResponse(output);

        URI location = URI.create("/v1/usuarios/" + output.id());
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Lista todos os usuarios cadastrados.
     *
     * <p>Endpoint: {@code GET /v1/usuarios}
     *
     * <p>Fluxo: ListarUsuarioUseCase.executar() -> UsuarioRestMapper.toResponseList() -> Response
     *
     * @return {@link ResponseEntity} com status 200 (OK) e lista de {@link UsuarioResponse}
     */
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Retorna todos os usuarios cadastrados")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            })
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        log.debug("Requisicao para listar usuarios");

        List<UsuarioOutput> outputs = listarUsuarioUseCase.executar();
        List<UsuarioResponse> responses = mapper.toResponseList(outputs);

        return ResponseEntity.ok(responses);
    }

    /**
     * Busca um usuario pelo seu identificador.
     *
     * <p>Endpoint: {@code GET /v1/usuarios/{id}}
     *
     * <p>Fluxo: BuscarUsuarioUseCase.executar(id) -> UsuarioRestMapper.toResponse() -> Response
     *
     * @param id identificador unico do usuario
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link UsuarioResponse}
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuario por ID",
            description = "Retorna um usuario pelo seu identificador")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
            })
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        log.debug("Requisicao para buscar usuario por id: {}", id);

        UsuarioOutput output = buscarUsuarioUseCase.executar(id);
        UsuarioResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um usuario existente.
     *
     * <p>Endpoint: {@code PUT /v1/usuarios/{id}}
     *
     * <p>Fluxo: Request -> UsuarioRestMapper.toInput(id, request) ->
     * AtualizarUsuarioUseCase.executar() -> UsuarioRestMapper.toResponse() -> Response
     *
     * @param id identificador unico do usuario a ser atualizado
     * @param request dados de atualizacao (validados com Bean Validation)
     * @return {@link ResponseEntity} com status 200 (OK) e corpo com {@link UsuarioResponse}
     *     atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario", description = "Atualiza um usuario existente")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Usuario atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados invalidos"),
                @ApiResponse(responseCode = "404", description = "Usuario nao encontrado"),
                @ApiResponse(responseCode = "422", description = "Email ja existe")
            })
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id, @Valid @RequestBody AtualizarUsuarioRequest request) {
        log.debug("Requisicao para atualizar usuario id: {}", id);

        AtualizarUsuarioInput input = mapper.toInput(id, request);
        UsuarioOutput output = atualizarUsuarioUseCase.executar(input);
        UsuarioResponse response = mapper.toResponse(output);

        return ResponseEntity.ok(response);
    }

    /**
     * Desativa (soft delete) um usuario.
     *
     * <p>Endpoint: {@code DELETE /v1/usuarios/{id}}
     *
     * <p>Fluxo: DeletarUsuarioUseCase.executar(id) -> Response 204 No Content
     *
     * <p>O registro nao e removido fisicamente do banco; o campo {@code ativo} e definido como
     * {@code false}.
     *
     * @param id identificador unico do usuario a ser desativado
     * @return {@link ResponseEntity} com status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar usuario", description = "Desativa (soft delete) um usuario")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Usuario desativado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.debug("Requisicao para desativar usuario id: {}", id);

        deletarUsuarioUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }
}
