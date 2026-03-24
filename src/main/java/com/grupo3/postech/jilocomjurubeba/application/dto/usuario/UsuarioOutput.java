package com.grupo3.postech.jilocomjurubeba.application.dto.usuario;

/**
 * DTO de saida para Usuario.
 *
 * <p>Representa os dados de um usuario retornados pelos casos de uso. E uma "foto" imutavel dos
 * dados da entidade de dominio, desacoplando a camada Application da entidade {@link
 * com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario}. A senha nunca e incluida neste
 * DTO por questoes de seguranca.
 *
 * @param id identificador unico do usuario
 * @param nome nome completo do usuario
 * @param cpf CPF do usuario (numero formatado)
 * @param email endereco de email do usuario
 * @param telefone numero de telefone do usuario
 * @param tipoUsuario nome do tipo de usuario associado (ex: "MASTER", "DONO_RESTAURANTE")
 * @param ativo indica se o usuario esta ativo no sistema
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public record UsuarioOutput(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        String tipoUsuario,
        boolean ativo) {}
