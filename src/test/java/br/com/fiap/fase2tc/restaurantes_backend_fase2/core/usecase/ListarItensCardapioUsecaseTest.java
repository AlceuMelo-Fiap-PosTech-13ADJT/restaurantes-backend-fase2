package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
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
@DisplayName("Testes de UseCase: ListarItensCardapioUsecase")
class ListarItensCardapioUsecaseTest {

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private ListarItensCardapioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve listar itens ativos do cardápio do restaurante com sucesso")
    void deveListarItensAtivosComSucesso() {
        var restauranteId = UUID.randomUUID();
        var restaurante = Restaurante.builder().id(restauranteId).ativo(true).build();
        var item1 = ItemCardapio.builder().id(UUID.randomUUID()).nome("Item 1").ativo(true).build();
        var item2 = ItemCardapio.builder().id(UUID.randomUUID()).nome("Item 2").ativo(true).build();

        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.of(restaurante));
        when(itemCardapioGateway.listarAtivosPorRestaurante(restauranteId)).thenReturn(List.of(item1, item2));

        List<ItemCardapioOutputDto> lista = usecase.executar(restauranteId);

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getNome()).isEqualTo("Item 1");
        assertThat(lista.get(1).getNome()).isEqualTo("Item 2");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restauranteId for nulo")
    void deveLancarExcecaoQuandoRestauranteIdNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        var restauranteId = UUID.randomUUID();
        when(restauranteGateway.buscarAtivoPorId(restauranteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(restauranteId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Restaurante não encontrado.");
    }
}
