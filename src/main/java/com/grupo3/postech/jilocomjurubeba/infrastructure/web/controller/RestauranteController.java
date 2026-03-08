package com.grupo3.postech.jilocomjurubeba.infrastructure.web.controller;

import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.AtualizarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.CriarRestauranteInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.restaurante.RestauranteOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.AtualizarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.BuscarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.CriarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.DeletarRestauranteUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.restaurante.ListarRestauranteUseCase;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

  private final CriarRestauranteUseCase criarUseCase;
  private final ListarRestauranteUseCase listarUseCase;
  private final BuscarRestauranteUseCase buscarUseCase;
  private final AtualizarRestauranteUseCase atualizarUseCase;
  private final DeletarRestauranteUseCase deletarUseCase;

  public RestauranteController(
      CriarRestauranteUseCase criarUseCase,
      ListarRestauranteUseCase listarUseCase,
      BuscarRestauranteUseCase buscarUseCase,
      AtualizarRestauranteUseCase atualizarUseCase,
      DeletarRestauranteUseCase deletarUseCase) {
    this.criarUseCase = criarUseCase;
    this.listarUseCase = listarUseCase;
    this.buscarUseCase = buscarUseCase;
    this.atualizarUseCase = atualizarUseCase;
    this.deletarUseCase = deletarUseCase;
  }

  @PostMapping
  public ResponseEntity<RestauranteOutput> criar(@RequestBody CriarRestauranteInput input) {
    RestauranteOutput criado = criarUseCase.executar(input);
    return ResponseEntity.created(URI.create("/restaurantes/" + criado.id())).body(criado);
  }

  @GetMapping
  public ResponseEntity<List<RestauranteOutput>> listar() {
    return ResponseEntity.ok(listarUseCase.executar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<RestauranteOutput> buscarPorId(@PathVariable Long id) {
    return ResponseEntity.ok(buscarUseCase.executar(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<RestauranteOutput> atualizar(
      @PathVariable Long id, @RequestBody AtualizarRestauranteInput input) {

    // garante o id do path como fonte da verdade
    AtualizarRestauranteInput ajustado =
        new AtualizarRestauranteInput(
            id,
            input.nome(),
            input.endereco(),
            input.typeCozinha(),
            input.horaAbertura(),
            input.horaFechamento(),
            input.donoId());

    return ResponseEntity.ok(atualizarUseCase.executar(ajustado));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable Long id) {
    deletarUseCase.executar(id);
    return ResponseEntity.noContent().build();
  }
}
