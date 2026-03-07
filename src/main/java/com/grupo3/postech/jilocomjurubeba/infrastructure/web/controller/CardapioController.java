package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.AtualizarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.BuscarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.CriarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.DeletarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.ListarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto.AtualizarCardapioRequest;
import com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto.CriarCardapioRequest;

@RestController
@RequestMapping({
        "/cardapios",
        "/restaurantes/{restauranteId}/cardapio"
})
public class CardapioController {

    private final CriarCardapioUseCase criar;
    private final AtualizarCardapioUseCase atualizar;
    private final DeletarCardapioUseCase deletar;
    private final BuscarCardapioUseCase buscar;
    private final ListarCardapioUseCase listar;

    public CardapioController(
            CriarCardapioUseCase criar,
            AtualizarCardapioUseCase atualizar,
            DeletarCardapioUseCase deletar,
            BuscarCardapioUseCase buscar,
            ListarCardapioUseCase listar
    ) {
        this.criar = criar;
        this.atualizar = atualizar;
        this.deletar = deletar;
        this.buscar = buscar;
        this.listar = listar;
    }

    @GetMapping
    public List<CardapioOutput> listar(@PathVariable(required = false) Long restauranteId) {
        List<CardapioOutput> todos = listar.executar();

        if (restauranteId == null) {
            return todos;
        }

        return todos.stream()
                .filter(it -> Objects.equals(it.restauranteId(), restauranteId))
                .toList();
    }

    @GetMapping("/{id}")
    public CardapioOutput buscar(
            @PathVariable Long id,
            @PathVariable(required = false) Long restauranteId
    ) {
        CardapioOutput out = buscar.executar(id);

        if (restauranteId != null && !Objects.equals(out.restauranteId(), restauranteId)) {
            throw new EntidadeNaoEncontradaException("Cardapio", id);
        }

        return out;
    }

    @PostMapping
    public ResponseEntity<CardapioOutput> criar(
            @PathVariable(required = false) Long restauranteId,
            @RequestBody CriarCardapioRequest req
    ) {
        Long restId = restauranteId != null ? restauranteId : req.restauranteId();

        CriarCardapioInput input = new CriarCardapioInput(
                req.nome(),
                req.descricao(),
                req.preco(),
                req.apenasNoLocal(),
                req.caminhoFoto(),
                restId
        );

        CardapioOutput out = criar.executar(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @PutMapping("/{id}")
    public CardapioOutput atualizar(
            @PathVariable Long id,
            @PathVariable(required = false) Long restauranteId,
            @RequestBody AtualizarCardapioRequest req
    ) {
        Long restId = restauranteId != null ? restauranteId : req.restauranteId();

        AtualizarCardapioInput input = new AtualizarCardapioInput(
                id,
                req.nome(),
                req.descricao(),
                req.preco(),
                req.apenasNoLocal(),
                req.caminhoFoto(),
                restId
        );

        return atualizar.executar(input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        deletar.executar(id);
    }
}