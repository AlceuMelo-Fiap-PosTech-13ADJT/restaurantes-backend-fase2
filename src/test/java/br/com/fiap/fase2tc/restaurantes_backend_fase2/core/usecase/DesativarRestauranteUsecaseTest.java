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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: DesativarRestauranteUsecase")
class DesativarRestauranteUsecaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @InjectMocks
    private DesativarRestauranteUsecaseImpl usecase;

    @Test
    @DisplayName("Deve desativar restaurante com sucesso")
    void deveDesativarRestauranteComSucesso() {
        var restauranteId = UUID.randomUUID();
        var donoId = UUID.randomUUID();

        var input = DesativarRestauranteInputDto.builder()
                .restauranteId(restauranteId)
                .usuarioOperadorId(donoId)
                .build();

        var restaurante = Restaurante.builder()
                .id(restauranteId)
                .ativo(true)
                .build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));

        usecase.executar(input);

        assertThat(restaurante.getAtivo()).isFalse();
        verify(validarPermissaoUsuarioUsecase).executar(any(ValidarPermissaoUsuarioInputDto.class));
        verify(restauranteGateway).desativar(restauranteId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando input ou restauranteId for nulo")
    void deveLancarExcecaoQuandoInputNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");

        var inputSemId = DesativarRestauranteInputDto.builder().build();
        assertThatThrownBy(() -> usecase.executar(inputSemId))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        var restauranteId = UUID.randomUUID();
        var input = DesativarRestauranteInputDto.builder()
                .restauranteId(restauranteId)
                .usuarioOperadorId(UUID.randomUUID())
                .build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado.");
    }
}
