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
 * @author Danilo Fernando
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
