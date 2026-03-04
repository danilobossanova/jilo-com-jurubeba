package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.tipousuario.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity(name = "tipo_usuario") // nome da entidade/tabela, mas o "name" aqui evita colisão
@Table(name = "tipo_usuario")
@Getter
@Setter
@NoArgsConstructor
public class TipoUsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false)
    private boolean ativo = true;
}
