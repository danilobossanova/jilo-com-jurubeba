package com.grupo3.postech.jilocomjurubeba.application.usecase.saude;

import com.grupo3.postech.jilocomjurubeba.application.dto.saude.SaudeOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para VerificarSaudeUseCase.
 *
 * Estes testes NÃO usam Spring - demonstram que a camada
 * de application é independente de frameworks.
 *
 * @author Danilo Fernando
 */
class VerificarSaudeUseCaseTest {

    private VerificarSaudeUseCase useCase;

    private static final String VERSAO_TESTE = "1.0.0-TEST";

    @BeforeEach
    void setUp() {
        useCase = new VerificarSaudeUseCase(VERSAO_TESTE);
    }

    @Test
    @DisplayName("Deve retornar status UP")
    void deveRetornarStatusUp() {
        // When
        SaudeOutput resultado = useCase.executar();

        // Then
        assertThat(resultado.status()).isEqualTo("UP");
    }

    @Test
    @DisplayName("Deve retornar versão da aplicação")
    void deveRetornarVersao() {
        // When
        SaudeOutput resultado = useCase.executar();

        // Then
        assertThat(resultado.versao()).isEqualTo(VERSAO_TESTE);
    }

    @Test
    @DisplayName("Deve retornar timestamp atual")
    void deveRetornarTimestamp() {
        // When
        SaudeOutput resultado = useCase.executar();

        // Then
        assertThat(resultado.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Deve criar SaudeOutput válido")
    void deveCriarSaudeOutputValido() {
        // When
        SaudeOutput resultado = useCase.executar();

        // Then
        assertThat(resultado)
                .isNotNull()
                .satisfies(saude -> {
                    assertThat(saude.status()).isEqualTo("UP");
                    assertThat(saude.versao()).isEqualTo(VERSAO_TESTE);
                    assertThat(saude.timestamp()).isNotNull();
                });
    }
}
