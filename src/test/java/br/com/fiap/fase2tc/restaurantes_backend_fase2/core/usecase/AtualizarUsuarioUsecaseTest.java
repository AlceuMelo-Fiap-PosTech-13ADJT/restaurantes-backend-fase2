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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Caso de Uso: AtualizarUsuarioUsecase")
class AtualizarUsuarioUsecaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private AtualizarUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();

        AtualizarUsuarioInputDto input = AtualizarUsuarioInputDto.builder()
                .id(id)
                .nome("Carlos Silva Atualizado")
                .email("carlos.novo@email.com")
                .tipoUsuarioId(tipoId)
                .restauranteId(restauranteId)
                .build();

        TipoUsuario tipoAtual = TipoUsuario.builder().id(UUID.randomUUID()).tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build();
        TipoUsuario tipoNovo = TipoUsuario.builder().id(tipoId).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();

        Usuario usuarioExistente = Usuario.builder()
                .id(id)
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuario(tipoAtual)
                .restauranteId(restauranteId)
                .ativo(true)
                .build();

        Restaurante restaurante = Restaurante.builder().id(restauranteId).ativo(true).build();

        when(usuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.existePorEmail("carlos.novo@email.com")).thenReturn(false);
        when(tipoUsuarioGateway.buscarAtivoPorId(tipoId)).thenReturn(Optional.of(tipoNovo));
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(usuarioGateway.atualizar(eq(restauranteId), any(Usuario.class))).thenReturn(usuarioExistente);

        UsuarioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        verify(usuarioGateway).atualizar(eq(restauranteId), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando input for nulo ou id nulo")
    void deveLancarExcecaoQuandoIdNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do usuário é obrigatório.");

        AtualizarUsuarioInputDto inputSemId = AtualizarUsuarioInputDto.builder().nome("Nome").build();
        assertThatThrownBy(() -> usecase.executar(inputSemId))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for nulo ou em branco")
    void deveLancarExcecaoQuandoNomeInvalido() {
        AtualizarUsuarioInputDto input = AtualizarUsuarioInputDto.builder()
                .id(UUID.randomUUID())
                .nome("   ")
                .email("carlos@email.com")
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Nome do usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email for nulo ou em branco")
    void deveLancarExcecaoQuandoEmailInvalido() {
        AtualizarUsuarioInputDto input = AtualizarUsuarioInputDto.builder()
                .id(UUID.randomUUID())
                .nome("Carlos")
                .email("   ")
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("E-mail do usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        UUID id = UUID.randomUUID();
        AtualizarUsuarioInputDto input = AtualizarUsuarioInputDto.builder()
                .id(id)
                .nome("Carlos")
                .email("carlos@email.com")
                .build();

        when(usuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Usuário não encontrado.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando novo email já pertencer a outro usuário")
    void deveLancarExcecaoQuandoNovoEmailJaExistir() {
        UUID id = UUID.randomUUID();
        AtualizarUsuarioInputDto input = AtualizarUsuarioInputDto.builder()
                .id(id)
                .nome("Carlos")
                .email("outro@email.com")
                .build();

        Usuario usuarioExistente = Usuario.builder()
                .id(id)
                .nome("Carlos")
                .email("carlos@email.com")
                .ativo(true)
                .build();

        when(usuarioGateway.buscarAtivoPorId(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.existePorEmail("outro@email.com")).thenReturn(true);

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Já existe um usuário cadastrado com este e-mail.");
    }
}
