package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.AtualizarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.CriarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.AtualizarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.BuscarTipoUsuarioPorIdUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.CriarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.DeletarTipoUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario.ListarTiposUsuarioUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tipos-usuario")
@RequiredArgsConstructor
public class TipoUsuarioController {

    private final CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;
    private final BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase;
    private final ListarTiposUsuarioUseCase listarTiposUsuarioUseCase;
    private final AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase;
    private final DeletarTipoUsuarioUseCase deletarTipoUsuarioUseCase;

    @PostMapping
    public ResponseEntity<TipoUsuarioOutput> criar(@RequestBody CriarTipoUsuarioInput input) {

        TipoUsuarioOutput criado = criarTipoUsuarioUseCase.executar(input);
        return ResponseEntity.created(URI.create("/tipos-usuario/" + criado.id())).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioOutput> buscarPorId(@PathVariable Long id) {
        TipoUsuarioOutput output = buscarTipoUsuarioPorIdUseCase.executar(id);
        return ResponseEntity.ok(output);
    }

    @GetMapping
    public ResponseEntity<List<TipoUsuarioOutput>> listar() {
        return ResponseEntity.ok(listarTiposUsuarioUseCase.executar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioOutput> atualizar(
        @PathVariable Long id, @RequestBody AtualizarTipoUsuarioInput input) {

        AtualizarTipoUsuarioInput inputComId =
            new AtualizarTipoUsuarioInput(id, input.nome(), input.descricao());

        TipoUsuarioOutput atualizado = atualizarTipoUsuarioUseCase.executar(inputComId);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarTipoUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
