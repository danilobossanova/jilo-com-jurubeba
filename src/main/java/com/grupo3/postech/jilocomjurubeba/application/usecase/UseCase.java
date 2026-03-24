package com.grupo3.postech.jilocomjurubeba.application.usecase;

/**
 * Interface base para todos os casos de uso.
 *
 * <p>Define o contrato padrão para execução de casos de uso, seguindo o padrão Command com entrada
 * e saída tipadas.
 *
 * @param <I> Tipo do objeto de entrada (Input)
 * @param <O> Tipo do objeto de saída (Output)
 *     <p>Exemplo de implementação:
 *     <pre>
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
public interface UseCase<I, O> {

    /**
     * Executa o caso de uso.
     *
     * @param input dados de entrada para o caso de uso
     * @return resultado da execução
     */
    O executar(I input);
}
