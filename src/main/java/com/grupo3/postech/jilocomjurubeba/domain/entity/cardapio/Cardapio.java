package com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio;

import java.math.BigDecimal;

import com.grupo3.postech.jilocomjurubeba.application.dto.cardapio.CardapioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Cardapio {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean apenasNoLocal;
    private String caminhoFoto;
    private Restaurante restaurante;
    private boolean ativo;

    public Cardapio(
            Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Restaurante restaurante,
            boolean ativo) {

        validarCamposObrigatorios(nome, preco, restaurante);

        this.id = id;
        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.preco = validarPreco(preco);
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = normalizarCaminhoFoto(caminhoFoto);
        this.restaurante = restaurante;
        this.ativo = ativo;
    }

    public Cardapio(
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Restaurante restaurante) {

        validarCamposObrigatorios(nome, preco, restaurante);

        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.preco = validarPreco(preco);
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = normalizarCaminhoFoto(caminhoFoto);
        this.restaurante = restaurante;
        this.ativo = true;
    }

    public void atualizarCadastro(
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Restaurante restaurante) {

        validarCamposObrigatorios(nome, preco, restaurante);

        this.nome = normalizarNome(nome);
        this.descricao = normalizarDescricao(descricao);
        this.preco = validarPreco(preco);
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = normalizarCaminhoFoto(caminhoFoto);
        this.restaurante = restaurante;
    }

    public void alterarPreco(BigDecimal novoPreco) {
        this.preco = validarPreco(novoPreco);
    }

    public void alterarDisponibilidadeSomenteLocal(boolean apenasNoLocal) {
        this.apenasNoLocal = apenasNoLocal;
    }

    public void alterarFoto(String novoCaminhoFoto) {
        this.caminhoFoto = normalizarCaminhoFoto(novoCaminhoFoto);
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public boolean estaAtivo() {
        return ativo;
    }

    public boolean pertenceAoRestaurante(Long restauranteId) {
        Restaurante.RestauranteSnapshot restauranteSnapshot = restaurante != null ? restaurante.snapshot() : null;
        return restauranteSnapshot != null
                && restauranteSnapshot.id() != null
                && restauranteSnapshot.id().equals(restauranteId);
    }

    public CardapioSnapshot snapshot() {
        Restaurante.RestauranteSnapshot restauranteSnapshot = restaurante != null ? restaurante.snapshot() : null;

        return new CardapioSnapshot(
                id,
                nome,
                descricao,
                preco,
                apenasNoLocal,
                caminhoFoto,
                restauranteSnapshot != null ? restauranteSnapshot.id() : null,
                ativo
        );
    }

    public CardapioOutput paraOutput() {
        CardapioSnapshot dados = snapshot();
        return new CardapioOutput(
                dados.id(),
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.apenasNoLocal(),
                dados.caminhoFoto(),
                dados.restauranteId(),
                dados.ativo()
        );
    }

    public record CardapioSnapshot(
            Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Long restauranteId,
            boolean ativo) {}

    private void validarCamposObrigatorios(String nome, BigDecimal preco, Restaurante restaurante) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome é obrigatório");
        }

        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Preço deve ser maior que zero");
        }

        if (restaurante == null) {
            throw new RegraDeNegocioException("Item deve pertencer a um restaurante");
        }

        Restaurante.RestauranteSnapshot restauranteSnapshot = restaurante.snapshot();
        if (restauranteSnapshot == null || restauranteSnapshot.id() == null) {
            throw new RegraDeNegocioException("Item deve pertencer a um restaurante válido");
        }
    }

    private BigDecimal validarPreco(BigDecimal novoPreco) {
        if (novoPreco == null || novoPreco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Preço inválido");
        }
        return novoPreco;
    }

    private String normalizarNome(String nome) {
        return nome.trim().toUpperCase();
    }

    private String normalizarDescricao(String descricao) {
        return descricao == null || descricao.isBlank() ? null : descricao.trim();
    }

    private String normalizarCaminhoFoto(String caminhoFoto) {
        return caminhoFoto == null || caminhoFoto.isBlank() ? null : caminhoFoto.trim();
    }
}