package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity.TipoUsuarioJpaEntity;

import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "usuario")
public class UsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(length = 20)
    private String telefone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    private TipoUsuarioJpaEntity tipoUsuario;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    public UsuarioJpaEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public TipoUsuarioJpaEntity getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuarioJpaEntity tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
}
