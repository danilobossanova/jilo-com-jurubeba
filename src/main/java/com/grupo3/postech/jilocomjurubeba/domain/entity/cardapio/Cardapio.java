package com.grupo3.postech.jilocomjurubeba.domain.entity.cardapio;

import java.math.BigDecimal;

import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;

import lombok.*;

@Getter
@Setter
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

    @Builder
    public Cardapio(
            Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Restaurante restaurante,
            boolean ativo) {

        validar();
        atualizarPreco(preco);
        alterarDisponibilidade(apenasNoLocal);

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = caminhoFoto;
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
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = caminhoFoto;
        this.restaurante = restaurante;
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    private void validar() {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome é obrigatório");
        }

        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Preço deve ser maior que zero");
        }

        if (restaurante == null) {
            throw new RegraDeNegocioException("Item deve pertencer a um restaurante");
        }
    }

    private void atualizarPreco(BigDecimal novoPreco) {
        if (novoPreco == null || novoPreco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Preço inválido");
        }
        this.preco = novoPreco;
    }

    private void alterarDisponibilidade(boolean apenasNoLocal) {
        this.apenasNoLocal = apenasNoLocal;
    }

    public void atualizarDados(
            String nome,
            String descricao,
            BigDecimal preco,
            boolean apenasNoLocal,
            String caminhoFoto,
            Restaurante restaurante) {
        this.nome = nome.trim().toUpperCase();
        this.descricao = descricao.trim();
        this.preco = preco;
        this.apenasNoLocal = apenasNoLocal;
        this.caminhoFoto = caminhoFoto;
        this.restaurante = restaurante;
    }
}
