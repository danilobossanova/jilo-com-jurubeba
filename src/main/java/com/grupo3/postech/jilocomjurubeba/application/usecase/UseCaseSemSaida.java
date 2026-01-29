package com.grupo3.postech.jilocomjurubeba.application.usecase;

/**
 * Interface para casos de uso que não retornam valor.
 *
 * <p>Útil para operações de comando que apenas executam ações sem necessidade de retorno (ex:
 * deletar, notificar).
 *
 * @param <I> Tipo do objeto de entrada (Input)
 *     <p>Exemplo de uso:
 *     <pre>
 * public class DeletarUsuarioUseCase implements UseCaseSemSaida<DeletarUsuarioInput> {
 *
 *     private final UsuarioGateway usuarioGateway;
 *
 *     {@literal @}Override
 *     public void executar(DeletarUsuarioInput input) {
 *         usuarioGateway.deletar(input.getId());
 *     }
 * }
 * </pre>
 *
 * @author Danilo Fernando
 */
@FunctionalInterface
public interface UseCaseSemSaida<I> {

    /**
     * Executa o caso de uso sem retorno.
     *
     * @param input dados de entrada para o caso de uso
     */
    void executar(I input);
}
