package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Caso de Uso: ListarUsuariosPorRestauranteUsecase")
class ListarUsuariosPorRestauranteUsecaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private ListarUsuariosPorRestauranteUsecaseImpl usecase;

    @Test
    @DisplayName("Deve listar usuários por restaurante com sucesso")
    void deveListarUsuariosPorRestauranteComSucesso() {
        UUID restauranteId = UUID.randomUUID();
        Restaurante restaurante = Restaurante.builder()
                .id(restauranteId)
                .nome("Restaurante Teste")
                .ativo(true)
                .build();

        TipoUsuario tipoCliente = TipoUsuario.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.CLIENTE)
                .ativo(true)
                .build();

        Usuario usuario1 = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Cliente 1")
                .email("cliente1@email.com")
                .tipoUsuario(tipoCliente)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Cliente 2")
                .email("cliente2@email.com")
                .tipoUsuario(tipoCliente)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(usuarioGateway.listarAtivosPorRestaurante(restauranteId)).thenReturn(List.of(usuario1, usuario2));

        List<UsuarioOutputDto> resultado = usecase.executar(restauranteId);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Cliente 1");
        assertThat(resultado.get(1).getNome()).isEqualTo("Cliente 2");
        verify(restauranteGateway).buscarAtivoPorId(restauranteId);
        verify(usuarioGateway).listarAtivosPorRestaurante(restauranteId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do restaurante for nulo")
    void deveLancarExcecaoQuandoRestauranteIdNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        UUID restauranteId = UUID.randomUUID();
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(restauranteId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado.");
    }
}
