package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: AtualizarRestauranteUsecase")
class AtualizarRestauranteUsecaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @InjectMocks
    private AtualizarRestauranteUsecaseImpl usecase;

    @Test
    @DisplayName("Deve atualizar dados do restaurante com sucesso")
    void deveAtualizarRestauranteComSucesso() {
        var restauranteId = UUID.randomUUID();
        var donoId = UUID.randomUUID();

        var input = AtualizarRestauranteInputDto.builder()
                .restauranteId(restauranteId)
                .usuarioOperadorId(donoId)
                .nome("Novo Nome")
                .endereco("Novo Endereço")
                .tipoCozinha("Nova Cozinha")
                .horarioFuncionamento("10h às 22h")
                .build();

        var dono = Usuario.builder().id(donoId).ativo(true).build();
        var restaurante = Restaurante.builder()
                .id(restauranteId)
                .nome("Antigo Nome")
                .endereco("Antigo Endereço")
                .tipoCozinha("Antiga Cozinha")
                .horarioFuncionamento("Antigo Horario")
                .dono(dono)
                .ativo(true)
                .build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(restauranteGateway.atualizar(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getNome()).isEqualTo("Novo Nome");
        verify(validarPermissaoUsuarioUsecase).executar(any(ValidarPermissaoUsuarioInputDto.class));
        verify(restauranteGateway).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando input ou restauranteId for nulo")
    void deveLancarExcecaoQuandoInputNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");

        var inputSemId = AtualizarRestauranteInputDto.builder().build();
        assertThatThrownBy(() -> usecase.executar(inputSemId))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        var restauranteId = UUID.randomUUID();
        var input = AtualizarRestauranteInputDto.builder()
                .restauranteId(restauranteId)
                .usuarioOperadorId(UUID.randomUUID())
                .nome("Nome")
                .build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado.");
    }
}
