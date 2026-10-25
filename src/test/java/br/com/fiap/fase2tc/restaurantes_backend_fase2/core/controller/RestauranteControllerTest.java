package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarRestaurantePorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarRestauranteUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarRestaurantesUsecase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Controller Core: RestauranteController")
class RestauranteControllerTest {

    @Mock
    private CriarRestauranteUsecase criarRestauranteUsecase;

    @Mock
    private AtualizarRestauranteUsecase atualizarRestauranteUsecase;

    @Mock
    private BuscarRestaurantePorIdUsecase buscarRestaurantePorIdUsecase;

    @Mock
    private ListarRestaurantesUsecase listarRestaurantesUsecase;

    @Mock
    private DesativarRestauranteUsecase desativarRestauranteUsecase;

    @InjectMocks
    private RestauranteController controller;

    @Test
    @DisplayName("Deve delegar criação para CriarRestauranteUsecase")
    void deveDelegarCriacao() {
        var input = CriarRestauranteInputDto.builder().nome("Restaurante Teste").build();
        var expected = new CriarRestauranteOutputDto(UUID.randomUUID());

        when(criarRestauranteUsecase.executar(input)).thenReturn(expected);

        CriarRestauranteOutputDto result = controller.criar(input);

        assertThat(result).isSameAs(expected);
        verify(criarRestauranteUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar atualização para AtualizarRestauranteUsecase")
    void deveDelegarAtualizacao() {
        var input = AtualizarRestauranteInputDto.builder().restauranteId(UUID.randomUUID()).nome("Nome Atualizado").build();
        var expected = RestauranteOutputDto.builder().id(input.getRestauranteId()).nome("Nome Atualizado").build();

        when(atualizarRestauranteUsecase.executar(input)).thenReturn(expected);

        RestauranteOutputDto result = controller.atualizar(input);

        assertThat(result).isSameAs(expected);
        verify(atualizarRestauranteUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar busca por ID para BuscarRestaurantePorIdUsecase")
    void deveDelegarBuscaPorId() {
        var id = UUID.randomUUID();
        var expected = RestauranteOutputDto.builder().id(id).nome("Restaurante").build();

        when(buscarRestaurantePorIdUsecase.executar(id)).thenReturn(expected);

        RestauranteOutputDto result = controller.buscarPorId(id);

        assertThat(result).isSameAs(expected);
        verify(buscarRestaurantePorIdUsecase).executar(id);
    }

    @Test
    @DisplayName("Deve delegar listagem para ListarRestaurantesUsecase")
    void deveDelegarListagem() {
        var r1 = RestauranteOutputDto.builder().id(UUID.randomUUID()).nome("Restaurante 1").build();
        var r2 = RestauranteOutputDto.builder().id(UUID.randomUUID()).nome("Restaurante 2").build();
        var expected = List.of(r1, r2);

        when(listarRestaurantesUsecase.executar()).thenReturn(expected);

        List<RestauranteOutputDto> result = controller.listar();

        assertThat(result).isSameAs(expected);
        verify(listarRestaurantesUsecase).executar();
    }

    @Test
    @DisplayName("Deve delegar desativação via DTO para DesativarRestauranteUsecase")
    void deveDelegarDesativacaoDto() {
        var input = DesativarRestauranteInputDto.builder()
                .restauranteId(UUID.randomUUID())
                .usuarioOperadorId(UUID.randomUUID())
                .build();

        controller.desativar(input);

        verify(desativarRestauranteUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar desativação via parâmetros de conveniência")
    void deveDelegarDesativacaoConveniente() {
        var restauranteId = UUID.randomUUID();
        var operadorId = UUID.randomUUID();

        controller.desativar(restauranteId, operadorId);

        verify(desativarRestauranteUsecase).executar(any(DesativarRestauranteInputDto.class));
    }

    @Test
    @DisplayName("Deve instanciar via construtor com apenas CriarRestauranteUsecase")
    void deveInstanciarComUmParametro() {
        var simpleController = new RestauranteController(criarRestauranteUsecase);
        assertThat(simpleController).isNotNull();
    }
}
