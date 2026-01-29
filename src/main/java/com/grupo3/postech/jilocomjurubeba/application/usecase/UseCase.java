package com.grupo3.postech.jilocomjurubeba.application.usecase;

/**
 * Interface base para todos os casos de uso.
 *
 * Define o contrato padrão para execução de casos de uso,
 * seguindo o padrão Command com entrada e saída tipadas.
 *
 * @param <I> Tipo do objeto de entrada (Input)
 * @param <O> Tipo do objeto de saída (Output)
 *
 * Exemplo de implementação:
 * <pre>
 * public class CriarUsuarioUseCase implements UseCase<CriarUsuarioInput, UsuarioOutput> {
 *
 *     private final UsuarioGateway usuarioGateway;
 *
 *     public CriarUsuarioUseCase(UsuarioGateway usuarioGateway) {
 *         this.usuarioGateway = usuarioGateway;
 *     }
 *
 *     {@literal @}Override
 *     public UsuarioOutput executar(CriarUsuarioInput input) {
 *         // Implementação do caso de uso
 *     }
 * }
 * </pre>
 *
 * @author Danilo Fernando
 */
@FunctionalInterface
public interface UseCase<I, O> {

    /**
     * Executa o caso de uso.
     *
     * @param input dados de entrada para o caso de uso
     * @return resultado da execução
     */
    O executar(I input);
}
