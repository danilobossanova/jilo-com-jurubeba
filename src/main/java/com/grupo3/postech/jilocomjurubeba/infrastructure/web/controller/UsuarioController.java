package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.CriarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.BuscarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.CriarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.DeletarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.ListarUsuarioUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private final ListarUsuarioUseCase listarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    @PostMapping
    public ResponseEntity<UsuarioOutput> criar(@RequestBody CriarUsuarioInput input) {
        UsuarioOutput criado = criarUsuarioUseCase.executar(input);
        return ResponseEntity.created(URI.create("/usuarios/" + criado.id())).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioOutput> buscarPorId(@PathVariable Long id) {
        UsuarioOutput usuario = buscarUsuarioUseCase.executar(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioOutput>> listar() {
        return ResponseEntity.ok(listarUsuarioUseCase.executar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioOutput> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarUsuarioInput input
    ) {
        UsuarioOutput atualizado = atualizarUsuarioUseCase.executar(id, input);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}