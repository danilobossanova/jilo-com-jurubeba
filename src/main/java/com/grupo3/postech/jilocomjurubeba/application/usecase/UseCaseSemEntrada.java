package com.grupo3.postech.jilocomjurubeba.application.usecase;

/**
 * Interface para casos de uso que não requerem entrada.
 *
 * <p>Útil para operações de consulta simples ou ações que não precisam de parâmetros de entrada.
 *
 * @param <O> Tipo do objeto de saída (Output)
 *     <p>Exemplo de uso:
 *     <pre>
 * public class VerificarSaudeUseCase implements UseCaseSemEntrada<SaudeOutput> {
 *
 *     {@literal @}Override
 *     public SaudeOutput executar() {
 *         return new SaudeOutput("OK", LocalDateTime.now());
 *     }
 * }
 * </pre>
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
@FunctionalInterface
public interface UseCaseSemEntrada<O> {

    /**
     * Executa o caso de uso sem parâmetros de entrada.
     *
     * @return resultado da execução
     */
    O executar();
}
