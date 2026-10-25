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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: BuscarItemCardapioPorIdUsecase")
class BuscarItemCardapioPorIdUsecaseTest {

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private BuscarItemCardapioPorIdUsecaseImpl usecase;

    @Test
    @DisplayName("Deve buscar item do cardápio por ID com sucesso")
    void deveBuscarItemComSucesso() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();

        var restaurante = Restaurante.builder().id(restauranteId).ativo(true).build();
        var item = ItemCardapio.builder().id(itemId).nome("Risoto de Cogumelos").ativo(true).build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(itemCardapioGateway.buscarAtivoPorId(restauranteId, itemId)).thenReturn(Optional.of(item));

        ItemCardapioOutputDto output = usecase.executar(restauranteId, itemId);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(itemId);
        assertThat(output.getNome()).isEqualTo("Risoto de Cogumelos");
    }

    @Test
    @DisplayName("Deve lançar exceção quando IDs forem nulos")
    void deveLancarExcecaoQuandoIdsNulos() {
        assertThatThrownBy(() -> usecase.executar(null, UUID.randomUUID()))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");

        assertThatThrownBy(() -> usecase.executar(UUID.randomUUID(), null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do item do cardápio é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(restauranteId, itemId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando item não for encontrado")
    void deveLancarExcecaoQuandoItemNaoEncontrado() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();

        var restaurante = Restaurante.builder().id(restauranteId).ativo(true).build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(itemCardapioGateway.buscarAtivoPorId(restauranteId, itemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(restauranteId, itemId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Item do cardápio não encontrado.");
    }
}
