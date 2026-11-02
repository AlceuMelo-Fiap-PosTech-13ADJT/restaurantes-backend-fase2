package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Caso de Uso: BuscarUsuarioPorIdUsecase")
class BuscarUsuarioPorIdUsecaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private BuscarUsuarioPorIdUsecaseImpl usecase;

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        UUID usuarioId = UUID.randomUUID();
        TipoUsuario tipoUsuario = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .build();

        Usuario usuario = Usuario.builder()
                .id(usuarioId)
                .nome("Maria Dono")
                .email("maria.dono@email.com")
                .tipoUsuario(tipoUsuario)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();

        when(usuarioGateway.buscarAtivoPorId(usuarioId)).thenReturn(Optional.of(usuario));

        UsuarioOutputDto output = usecase.executar(usuarioId);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(usuarioId);
        assertThat(output.getNome()).isEqualTo("Maria Dono");
        assertThat(output.getEmail()).isEqualTo("maria.dono@email.com");
        assertThat(output.getTipoUsuarioId()).isEqualTo(tipoUsuario.getId());
        assertThat(output.getTipoUsuarioNome()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE.getDescricao());
        verify(usuarioGateway).buscarAtivoPorId(usuarioId);
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
        UUID usuarioId = UUID.randomUUID();
        when(usuarioGateway.buscarAtivoPorId(usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(usuarioId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Usuário não encontrado.");
    }
}
