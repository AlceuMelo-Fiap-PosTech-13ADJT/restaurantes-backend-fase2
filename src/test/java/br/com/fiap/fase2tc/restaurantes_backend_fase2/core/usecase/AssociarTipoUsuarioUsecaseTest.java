package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: AssociarTipoUsuarioUsecase")
class AssociarTipoUsuarioUsecaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @InjectMocks
    private AssociarTipoUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve associar tipo de usuário a um usuário com sucesso")
    void deveAssociarTipoComSucesso() {
        var usuarioId = UUID.randomUUID();
        var tipoUsuarioId = UUID.randomUUID();
        var restauranteId = UUID.randomUUID();

        var input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(usuarioId)
                .tipoUsuarioId(tipoUsuarioId)
                .restauranteId(restauranteId)
                .build();

        var usuario = Usuario.builder()
                .id(usuarioId)
                .nome("Maria Silva")
                .ativo(true)
                .build();

        var tipoUsuario = TipoUsuario.builder()
                .id(tipoUsuarioId)
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .ativo(true)
                .build();

        when(usuarioGateway.buscarAtivoPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(tipoUsuarioGateway.buscarAtivoPorId(tipoUsuarioId)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioGateway.atualizar(eq(restauranteId), any(Usuario.class))).thenReturn(usuario);

        UsuarioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(usuarioId);
        verify(usuarioGateway).atualizar(eq(restauranteId), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        var usuarioId = UUID.randomUUID();
        var input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(usuarioId)
                .tipoUsuarioId(UUID.randomUUID())
                .build();

        when(usuarioGateway.buscarAtivoPorId(usuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Usuário não encontrado.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não for encontrado")
    void deveLancarExcecaoQuandoTipoNaoEncontrado() {
        var usuarioId = UUID.randomUUID();
        var tipoUsuarioId = UUID.randomUUID();
        var input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(usuarioId)
                .tipoUsuarioId(tipoUsuarioId)
                .build();

        var usuario = Usuario.builder()
                .id(usuarioId)
                .ativo(true)
                .build();

        when(usuarioGateway.buscarAtivoPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(tipoUsuarioGateway.buscarAtivoPorId(tipoUsuarioId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Tipo de usuário não encontrado.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando IDs forem nulos")
    void deveLancarExcecaoQuandoIdsNulos() {
        var input = AssociarTipoUsuarioInputDto.builder().build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class);
    }
}
