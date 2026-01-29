/**
 * Data Transfer Objects (DTOs) da camada Application.
 *
 * <p>Objetos simples para transferência de dados entre camadas. Separados em Input (entrada) e
 * Output (saída) por caso de uso.
 *
 * <p>Convenções: - Usar Java Records para imutabilidade - [Entidade]Input.java para entrada de
 * dados - [Entidade]Output.java para saída de dados - Validações de formato aqui, regras de negócio
 * no domínio
 *
 * <p>Organização por entidade: - dto/ - usuario/ - CriarUsuarioInput.java - UsuarioOutput.java -
 * restaurante/ - CriarRestauranteInput.java - RestauranteOutput.java
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.application.dto;
