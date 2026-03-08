package com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.enumroles.TypeCozinha;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import java.time.LocalTime;
import java.util.Objects;

public class Restaurante {

  private Long id;
  private String nome;
  private String endereco;
  private TypeCozinha typeCozinha;
  private LocalTime horaAbertura;
  private LocalTime horaFechamento;
  private Usuario dono;
  private boolean ativo;

  public Restaurante() {}

  public Restaurante(
      Long id,
      String nome,
      String endereco,
      TypeCozinha typeCozinha,
      LocalTime horaAbertura,
      LocalTime horaFechamento,
      Usuario dono,
      boolean ativo) {
    validarNome(nome);
    validarEndereco(endereco);
    validarTipoCozinha(typeCozinha);
    validarHorario(horaAbertura, horaFechamento);
    validarDono(dono);

    this.id = id;
    this.nome = normalizarNome(nome);
    this.endereco = normalizarEndereco(endereco);
    this.typeCozinha = typeCozinha;
    this.horaAbertura = horaAbertura;
    this.horaFechamento = horaFechamento;
    this.dono = dono;
    this.ativo = ativo;
  }

  public Restaurante(
      String nome,
      String endereco,
      TypeCozinha typeCozinha,
      LocalTime horaAbertura,
      LocalTime horaFechamento,
      Usuario dono) {
    validarNome(nome);
    validarEndereco(endereco);
    validarTipoCozinha(typeCozinha);
    validarHorario(horaAbertura, horaFechamento);
    validarDono(dono);

    this.nome = normalizarNome(nome);
    this.endereco = normalizarEndereco(endereco);
    this.typeCozinha = typeCozinha;
    this.horaAbertura = horaAbertura;
    this.horaFechamento = horaFechamento;
    this.dono = dono;
    this.ativo = true;
  }

  public void atualizarCadastro(
      String nome,
      String endereco,
      TypeCozinha typeCozinha,
      LocalTime horaAbertura,
      LocalTime horaFechamento,
      Usuario dono) {
    validarNome(nome);
    validarEndereco(endereco);
    validarTipoCozinha(typeCozinha);
    validarHorario(horaAbertura, horaFechamento);
    validarDono(dono);

    this.nome = normalizarNome(nome);
    this.endereco = normalizarEndereco(endereco);
    this.typeCozinha = typeCozinha;
    this.horaAbertura = horaAbertura;
    this.horaFechamento = horaFechamento;
    this.dono = dono;
  }

  public void ativar() {
    this.ativo = true;
  }

  public void desativar() {
    this.ativo = false;
  }

  public void alterarEndereco(String novoEndereco) {
    validarEndereco(novoEndereco);
    this.endereco = normalizarEndereco(novoEndereco);
  }

  public void alterarHorario(LocalTime novaAbertura, LocalTime novoFechamento) {
    validarHorario(novaAbertura, novoFechamento);
    this.horaAbertura = novaAbertura;
    this.horaFechamento = novoFechamento;
  }

  public boolean estaAtivo() {
    return ativo;
  }

  public boolean estaAberto(LocalTime horarioAtual) {
    if (horarioAtual == null) {
      throw new RegraDeNegocioException("Horário atual é obrigatório");
    }

    if (horaAbertura.isBefore(horaFechamento)) {
      return !horarioAtual.isBefore(horaAbertura) && !horarioAtual.isAfter(horaFechamento);
    }

    return !horarioAtual.isBefore(horaAbertura) || !horarioAtual.isAfter(horaFechamento);
  }

  public boolean possuiMesmoIdQue(Long outroId) {
    return id != null && id.equals(outroId);
  }

  public boolean pertenceAoDono(Long donoId) {
    return dono != null
        && dono.snapshot() != null
        && dono.snapshot().id() != null
        && dono.snapshot().id().equals(donoId);
  }

  public RestauranteSnapshot snapshot() {
    Usuario.UsuarioSnapshot donoSnapshot = dono != null ? dono.snapshot() : null;

    return new RestauranteSnapshot(
        id,
        nome,
        endereco,
        typeCozinha,
        horaAbertura,
        horaFechamento,
        donoSnapshot != null ? donoSnapshot.id() : null,
        ativo);
  }

  public record RestauranteSnapshot(
      Long id,
      String nome,
      String endereco,
      TypeCozinha typeCozinha,
      LocalTime horaAbertura,
      LocalTime horaFechamento,
      Long donoId,
      boolean ativo) {}

  private void validarNome(String nome) {
    if (nome == null || nome.isBlank()) {
      throw new RegraDeNegocioException("Nome é obrigatório");
    }
  }

  private void validarEndereco(String endereco) {
    if (endereco == null || endereco.isBlank()) {
      throw new RegraDeNegocioException("Endereço é obrigatório");
    }
  }

  private void validarTipoCozinha(TypeCozinha typeCozinha) {
    if (typeCozinha == null) {
      throw new RegraDeNegocioException("Tipo de cozinha é obrigatório");
    }
  }

  private void validarHorario(LocalTime abertura, LocalTime fechamento) {
    if (abertura == null || fechamento == null) {
      throw new RegraDeNegocioException("Horários são obrigatórios");
    }

    if (abertura.equals(fechamento)) {
      throw new RegraDeNegocioException("Abertura e fechamento não podem ser iguais");
    }
  }

  private void validarDono(Usuario dono) {
    if (Objects.isNull(dono)) {
      throw new RegraDeNegocioException("Restaurante deve possuir um dono");
    }

    Usuario.UsuarioSnapshot donoSnapshot = dono.snapshot();
    if (donoSnapshot == null || donoSnapshot.id() == null) {
      throw new RegraDeNegocioException("Dono do restaurante deve possuir id válido");
    }
  }

  private String normalizarNome(String nome) {
    return nome.trim().toUpperCase();
  }

  private String normalizarEndereco(String endereco) {
    return endereco.trim();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Restaurante that)) return false;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getEndereco() {
    return endereco;
  }

  public TypeCozinha getTypeCozinha() {
    return typeCozinha;
  }

  public LocalTime getHoraAbertura() {
    return horaAbertura;
  }

  public LocalTime getHoraFechamento() {
    return horaFechamento;
  }

  public Usuario getDono() {
    return dono;
  }

  public boolean isAtivo() {
    return ativo;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public void setTypeCozinha(TypeCozinha typeCozinha) {
    this.typeCozinha = typeCozinha;
  }

  public void setHoraAbertura(LocalTime horaAbertura) {
    this.horaAbertura = horaAbertura;
  }

  public void setHoraFechamento(LocalTime horaFechamento) {
    this.horaFechamento = horaFechamento;
  }

  public void setDono(Usuario dono) {
    this.dono = dono;
  }

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }
}
