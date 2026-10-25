package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: BuscarRestaurantePorIdUsecase")
class BuscarRestaurantePorIdUsecaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private BuscarRestaurantePorIdUsecaseImpl usecase;

    @Test
    @DisplayName("Deve buscar restaurante por ID com sucesso")
    void deveBuscarRestaurantePorIdComSucesso() {
        var id = UUID.randomUUID();
        var restaurante = Restaurante.builder()
                .id(id)
                .nome("Pizzaria Bela")
                .ativo(true)
                .build();

        when(restauranteGateway.buscarAtivoPorId(id)).thenReturn(Optional.of(restaurante));

        RestauranteOutputDto output = usecase.executar(id);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(id);
        assertThat(output.getNome()).isEqualTo("Pizzaria Bela");
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID for nulo")
    void deveLancarExcecaoQuandoIdNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        var id = UUID.randomUUID();
        when(restauranteGateway.buscarAtivoPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(id))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado.");
    }
}
