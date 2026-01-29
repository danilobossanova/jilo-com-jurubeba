package com.grupo3.postech.jilocomjurubeba.application.usecase.saude;

import com.grupo3.postech.jilocomjurubeba.application.dto.saude.SaudeOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;

/**
 * Caso de uso para verificar a saúde da aplicação.
 *
 * Este é um exemplo simples de implementação end-to-end
 * para validar a arquitetura Clean Architecture.
 *
 * Fluxo:
 * 1. Controller recebe requisição GET /v1/health
 * 2. Controller chama este UseCase
 * 3. UseCase retorna SaudeOutput
 * 4. Controller converte para SaudeResponse e retorna HTTP 200
 *
 * @author Danilo Fernando
 */
public class VerificarSaudeUseCase implements UseCaseSemEntrada<SaudeOutput> {

    private final String versaoAplicacao;

    /**
     * Construtor com injeção da versão da aplicação.
     *
     * @param versaoAplicacao versão atual da aplicação
     */
    public VerificarSaudeUseCase(String versaoAplicacao) {
        this.versaoAplicacao = versaoAplicacao;
    }

    /**
     * Executa a verificação de saúde.
     *
     * Em uma implementação real, poderia verificar:
     * - Conexão com banco de dados
     * - Serviços externos
     * - Recursos de memória/disco
     *
     * @return SaudeOutput com status da aplicação
     */
    @Override
    public SaudeOutput executar() {
        return SaudeOutput.up(versaoAplicacao);
    }
}
