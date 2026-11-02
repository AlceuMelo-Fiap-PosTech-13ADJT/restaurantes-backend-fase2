package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: ListarRestaurantesUsecase")
class ListarRestaurantesUsecaseTest {

    @Mock
    private RestauranteGateway restauranteGateway;

    @InjectMocks
    private ListarRestaurantesUsecaseImpl usecase;

    @Test
    @DisplayName("Deve listar todos os restaurantes ativos com sucesso")
    void deveListarRestaurantesAtivos() {
        var r1 = Restaurante.builder().id(UUID.randomUUID()).nome("Restaurante A").ativo(true).build();
        var r2 = Restaurante.builder().id(UUID.randomUUID()).nome("Restaurante B").ativo(true).build();

        when(restauranteGateway.listarAtivos()).thenReturn(List.of(r1, r2));

        List<RestauranteOutputDto> lista = usecase.executar();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getNome()).isEqualTo("Restaurante A");
        assertThat(lista.get(1).getNome()).isEqualTo("Restaurante B");
    }
}
