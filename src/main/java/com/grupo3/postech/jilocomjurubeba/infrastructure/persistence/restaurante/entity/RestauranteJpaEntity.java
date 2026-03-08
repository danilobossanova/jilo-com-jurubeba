package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity;

import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "restaurante")
public class RestauranteJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(nullable = false, length = 255)
  private String endereco;

  @Enumerated(EnumType.STRING)
  @Column(name = "type_cozinha", nullable = false, length = 40)
  private TypeCozinha typeCozinha;

  @Column(name = "hora_abertura", nullable = false)
  private LocalTime horaAbertura;

  @Column(name = "hora_fechamento", nullable = false)
  private LocalTime horaFechamento;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "dono_id", nullable = false)
  private UsuarioJpaEntity dono;

  @Column(nullable = false)
  private boolean ativo = true;

  public RestauranteJpaEntity() {}

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

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public TypeCozinha getTypeCozinha() {
    return typeCozinha;
  }

  public void setTypeCozinha(TypeCozinha typeCozinha) {
    this.typeCozinha = typeCozinha;
  }

  public LocalTime getHoraAbertura() {
    return horaAbertura;
  }

  public void setHoraAbertura(LocalTime horaAbertura) {
    this.horaAbertura = horaAbertura;
  }

  public LocalTime getHoraFechamento() {
    return horaFechamento;
  }

  public void setHoraFechamento(LocalTime horaFechamento) {
    this.horaFechamento = horaFechamento;
  }

  public UsuarioJpaEntity getDono() {
    return dono;
  }

  public void setDono(UsuarioJpaEntity dono) {
    this.dono = dono;
  }

  public boolean isAtivo() {
    return ativo;
  }

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }
}
