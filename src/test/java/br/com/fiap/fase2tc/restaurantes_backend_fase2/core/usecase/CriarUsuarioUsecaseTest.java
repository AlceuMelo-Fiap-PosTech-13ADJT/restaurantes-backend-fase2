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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Caso de Uso: CriarUsuarioUsecase")
class CriarUsuarioUsecaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private TipoUsuarioGateway tipoUsuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private CriarUsuarioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve criar usuário com sucesso com restaurante informado")
    void deveCriarUsuarioComSucesso() {
        UUID tipoId = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();

        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuarioId(tipoId)
                .restauranteId(restauranteId)
                .build();

        TipoUsuario tipoUsuario = TipoUsuario.builder()
                .id(tipoId)
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        Restaurante restaurante = Restaurante.builder()
                .id(restauranteId)
                .nome("Restaurante Teste")
                .ativo(true)
                .build();

        Usuario usuarioSalvo = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuario(tipoUsuario)
                .restauranteId(restauranteId)
                .ativo(true)
                .build();

        when(usuarioGateway.existePorEmail("carlos@email.com")).thenReturn(false);
        when(tipoUsuarioGateway.buscarAtivoPorId(tipoId)).thenReturn(Optional.of(tipoUsuario));
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(usuarioGateway.salvar(eq(restauranteId), any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(usuarioSalvo.getId());
        assertThat(output.getNome()).isEqualTo("Carlos Silva");
        assertThat(output.getEmail()).isEqualTo("carlos@email.com");
        verify(usuarioGateway).salvar(eq(restauranteId), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando input for nulo")
    void deveLancarExcecaoQuandoInputNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Dados do usuário são obrigatórios.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for nulo ou em branco")
    void deveLancarExcecaoQuandoNomeInvalido() {
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome("   ")
                .email("carlos@email.com")
                .tipoUsuarioId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Nome do usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email for nulo ou em branco")
    void deveLancarExcecaoQuandoEmailInvalido() {
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome("Carlos Silva")
                .email(" ")
                .tipoUsuarioId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("E-mail do usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipoUsuarioId for nulo")
    void deveLancarExcecaoQuandoTipoUsuarioIdNulo() {
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuarioId(null)
                .build();

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Tipo de usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existir")
    void deveLancarExcecaoQuandoEmailJaExistir() {
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuarioId(UUID.randomUUID())
                .build();

        when(usuarioGateway.existePorEmail("carlos@email.com")).thenReturn(true);

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Já existe um usuário cadastrado com este e-mail.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não for encontrado")
    void deveLancarExcecaoQuandoTipoNaoEncontrado() {
        UUID tipoId = UUID.randomUUID();
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuarioId(tipoId)
                .build();

        when(usuarioGateway.existePorEmail("carlos@email.com")).thenReturn(false);
        when(tipoUsuarioGateway.buscarAtivoPorId(tipoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Tipo de usuário não encontrado.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante informado não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        UUID tipoId = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome("Carlos Silva")
                .email("carlos@email.com")
                .tipoUsuarioId(tipoId)
                .restauranteId(restauranteId)
                .build();

        TipoUsuario tipoUsuario = TipoUsuario.builder()
                .id(tipoId)
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        when(usuarioGateway.existePorEmail("carlos@email.com")).thenReturn(false);
        when(tipoUsuarioGateway.buscarAtivoPorId(tipoId)).thenReturn(Optional.of(tipoUsuario));
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado.");
    }
}
