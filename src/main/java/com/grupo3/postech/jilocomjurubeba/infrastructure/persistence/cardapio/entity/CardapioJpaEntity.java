package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.cardapio.entity;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity.RestauranteJpaEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cardapio")
public class CardapioJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(columnDefinition = "TEXT")
  private String descricao;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal preco;

  @Column(nullable = false)
  private boolean apenasNoLocal;

  private String caminhoFoto;

  @Column(nullable = false)
  private boolean ativo = true;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurante_id", nullable = false)
  private RestauranteJpaEntity restaurante;

  public CardapioJpaEntity() {}

  // getters/setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getPreco() {
    return preco;
  }

  public void setPreco(BigDecimal preco) {
    this.preco = preco;
  }

  public boolean isApenasNoLocal() {
    return apenasNoLocal;
  }

  public void setApenasNoLocal(boolean apenasNoLocal) {
    this.apenasNoLocal = apenasNoLocal;
  }

  public String getCaminhoFoto() {
    return caminhoFoto;
  }

  public void setCaminhoFoto(String caminhoFoto) {
    this.caminhoFoto = caminhoFoto;
  }

  public boolean isAtivo() {
    return ativo;
  }

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }

  public RestauranteJpaEntity getRestaurante() {
    return restaurante;
  }

  public void setRestaurante(RestauranteJpaEntity restaurante) {
    this.restaurante = restaurante;
  }
}
