package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.CriarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
class CriarTipoUsuarioUseCaseTest {

    @Mock private TipoUsuarioGateway gateway;

    private CriarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CriarTipoUsuarioUseCase(gateway);
    }

    @Test
    @DisplayName("Deve criar tipo de usuario com sucesso")
    void deveCriarComSucesso() {
        CriarTipoUsuarioInput input = new CriarTipoUsuarioInput("MASTER", "Administrador");
        TipoUsuario salvo = new TipoUsuario(1L, "MASTER", "Administrador", true);

        when(gateway.existePorNome("MASTER")).thenReturn(false);
        when(gateway.salvar(any(TipoUsuario.class))).thenReturn(salvo);

        TipoUsuarioOutput output = useCase.executar(input);

        assertThat(output.id()).isEqualTo(1L);
        assertThat(output.nome()).isEqualTo("MASTER");
        assertThat(output.descricao()).isEqualTo("Administrador");
        assertThat(output.ativo()).isTrue();
        verify(gateway).salvar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando nome ja existe")
    void deveLancarExcecaoQuandoNomeJaExiste() {
        CriarTipoUsuarioInput input = new CriarTipoUsuarioInput("MASTER", "Administrador");

        when(gateway.existePorNome("MASTER")).thenReturn(true);

        assertThatThrownBy(() -> useCase.executar(input))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("MASTER");

        verify(gateway, never()).salvar(any());
    }
}
