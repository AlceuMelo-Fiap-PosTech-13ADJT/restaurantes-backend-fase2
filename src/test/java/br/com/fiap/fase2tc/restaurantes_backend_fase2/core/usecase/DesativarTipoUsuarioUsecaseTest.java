package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: DesativarTipoUsuarioUsecase")
class DesativarTipoUsuarioUsecaseTest {

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @InjectMocks
    private DesativarTipoUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve desativar tipo de usuário com sucesso")
    void deveDesativarTipoComSucesso() {
        var id = UUID.randomUUID();
        var tipoUsuario = TipoUsuario.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        when(tipoUsuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.of(tipoUsuario));

        usecase.executar(id);

        assertThat(tipoUsuario.getAtivo()).isFalse();
        verify(tipoUsuarioGateway).desativar(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID for nulo")
    void deveLancarExcecaoQuandoIdNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do tipo de usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não for encontrado")
    void deveLancarExcecaoQuandoNaoEncontrado() {
        var id = UUID.randomUUID();
        when(tipoUsuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(id))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Tipo de usuário não encontrado.");
    }
}
