package com.grupo3.postech.jilocomjurubeba.application.usecase;

/**
 * Interface para casos de uso que não requerem entrada.
 *
 * Útil para operações de consulta simples ou ações que não precisam
 * de parâmetros de entrada.
 *
 * @param <O> Tipo do objeto de saída (Output)
 *
 * Exemplo de uso:
 * <pre>
 * public class VerificarSaudeUseCase implements UseCaseSemEntrada<SaudeOutput> {
 *
 *     {@literal @}Override
 *     public SaudeOutput executar() {
 *         return new SaudeOutput("OK", LocalDateTime.now());
 *     }
 * }
 * </pre>
 *
 * @author Danilo Fernando
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
