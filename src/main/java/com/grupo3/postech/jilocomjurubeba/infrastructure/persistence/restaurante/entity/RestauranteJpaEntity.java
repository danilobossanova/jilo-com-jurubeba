package com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.restaurante.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.grupo3.postech.jilocomjurubeba.domain.valueobject.TipoCozinha;
import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.entity.UsuarioJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representacao JPA da entidade Restaurante.
 *
 * <p>Esta classe e separada da entidade de dominio ({@code Restaurante}) para manter o dominio
 * livre de anotacoes de framework. A conversao entre as duas e feita pelo {@code
 * RestaurantePersistenceMapper}.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cozinha", nullable = false, length = 40)
    private TipoCozinha tipoCozinha;

    @Column(name = "hora_abertura", nullable = false)
    private LocalTime horaAbertura;

    @Column(name = "hora_fechamento", nullable = false)
    private LocalTime horaFechamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dono_id", nullable = false)
    private UsuarioJpaEntity dono;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}
