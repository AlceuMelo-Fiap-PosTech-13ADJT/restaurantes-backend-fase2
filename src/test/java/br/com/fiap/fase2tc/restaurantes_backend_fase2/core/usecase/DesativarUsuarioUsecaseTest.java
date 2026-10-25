package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Caso de Uso: DesativarUsuarioUsecase")
class DesativarUsuarioUsecaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private DesativarUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve desativar usuário com sucesso")
    void deveDesativarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        Usuario usuario = Usuario.builder().id(id).ativo(true).build();

        when(usuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.of(usuario));

        usecase.executar(id);

        verify(usuarioGateway).buscarAtivoPorId(id);
        verify(usuarioGateway).desativar(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID for nulo")
    void deveLancarExcecaoQuandoIdNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(usuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(id))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Usuário não encontrado.");
    }
}
