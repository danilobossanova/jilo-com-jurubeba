package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.AtualizarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CriarCardapioInput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.AtualizarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.BuscarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.CriarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.DeletarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.cardapio.ListarCardapioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.restaurante.RestauranteGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto.AtualizarCardapioRequest;
import com.grupo3.postech.jilocomjurubeba.infrastructure.web.dto.CriarCardapioRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final RestauranteGatewayDomain restauranteGateway;

    public CardapioController(
            CriarCardapioUseCase criar,
            AtualizarCardapioUseCase atualizar,
            DeletarCardapioUseCase deletar,
            BuscarCardapioUseCase buscar,
            ListarCardapioUseCase listar,
            RestauranteGatewayDomain restauranteGateway) {
        this.criar = criar;
        this.atualizar = atualizar;
        this.deletar = deletar;
        this.buscar = buscar;
        this.listar = listar;
        this.restauranteGateway = restauranteGateway;
    }

    // ✅ público
    // Funciona para:
    // GET /cardapios
    // GET /restaurantes/{restauranteId}/cardapio
    @GetMapping
    public List<CardapioOutput> listar(@PathVariable(required = false) Long restauranteId) {
        List<CardapioOutput> todos = listar.executar();

        // Se veio restauranteId na rota nova, filtra (mantém compatibilidade)
        if (restauranteId == null) return todos;

        // CardapioOutput PRECISA expor restauranteId (se não expuser, me manda o CardapioOutput que eu ajusto)
        return todos.stream()
                .filter(it -> Objects.equals(it.restauranteId(), restauranteId))
                .collect(Collectors.toList());
    }

    // ✅ público
    // Funciona para:
    // GET /cardapios/{id}
    // GET /restaurantes/{restauranteId}/cardapio/{id}
    @GetMapping("/{id}")
    public CardapioOutput buscar(@PathVariable Long id, @PathVariable(required = false) Long restauranteId) {
        CardapioOutput out = buscar.executar(id);

        // Se chamou pela rota nova, garante que o item é daquele restaurante
        if (restauranteId != null && !Objects.equals(out.restauranteId(), restauranteId)) {
            throw new EntidadeNaoEncontradaException("Cardapio", id);
        }
        return out;
    }

    // 🔒 restrito (controlado no SecurityFilterChain)
    // Funciona para:
    // POST /cardapios  (usa req.restauranteId)
    // POST /restaurantes/{restauranteId}/cardapio (usa path)
    @PostMapping
    public ResponseEntity<CardapioOutput> criar(
            @PathVariable(required = false) Long restauranteId,
            @RequestBody CriarCardapioRequest req) {

        Long restId = restauranteId != null ? restauranteId : req.restauranteId();

        Restaurante restaurante = restauranteGateway
                .findByIdRestaurante(restId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", restId));

        CriarCardapioInput input = new CriarCardapioInput(
                req.nome(),
                req.descricao(),
                req.preco(),
                req.apenasNoLocal(),
                req.caminhoFoto(),
                restaurante,
                true
        );

        CardapioOutput out = criar.executar(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    // 🔒 restrito
    // Funciona para:
    // PUT /cardapios/{id}
    // PUT /restaurantes/{restauranteId}/cardapio/{id}
    @PutMapping("/{id}")
    public CardapioOutput atualizar(
            @PathVariable Long id,
            @PathVariable(required = false) Long restauranteId,
            @RequestBody AtualizarCardapioRequest req) {

        Long restId = restauranteId != null ? restauranteId : req.restauranteId();

        Restaurante restaurante = restauranteGateway
                .findByIdRestaurante(restId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante", restId));

        AtualizarCardapioInput input = new AtualizarCardapioInput(
                id,
                req.nome(),
                req.descricao(),
                req.preco(),
                req.apenasNoLocal(),
                req.caminhoFoto(),
                restaurante
        );

        return atualizar.executar(input);
    }

    // 🔒 restrito
    // Funciona para:
    // DELETE /cardapios/{id}
    // DELETE /restaurantes/{restauranteId}/cardapio/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        deletar.executar(id);
    }
}