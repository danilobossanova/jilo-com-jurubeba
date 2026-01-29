/**
 * Casos de Uso da Aplicação.
 *
 * Cada classe representa um caso de uso específico do sistema.
 * Implementam a interface genérica UseCase<Input, Output>.
 *
 * Convenções:
 * - Uma classe por caso de uso
 * - Nome: [Ação][Entidade]UseCase (ex: CriarUsuarioUseCase)
 * - Método principal: executar(Input input)
 * - Injeção de dependências via construtor
 *
 * Exemplo de estrutura futura:
 * - usuario/
 *   - CriarUsuarioUseCase.java
 *   - BuscarUsuarioPorIdUseCase.java
 *   - AtualizarUsuarioUseCase.java
 *   - DeletarUsuarioUseCase.java
 *
 * @author Danilo Fernando
 */
package com.grupo3.postech.jilocomjurubeba.application.usecase;
