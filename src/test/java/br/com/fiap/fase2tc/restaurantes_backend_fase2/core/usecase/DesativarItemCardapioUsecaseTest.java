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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: DesativarItemCardapioUsecase")
class DesativarItemCardapioUsecaseTest {

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    @Mock
    private ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @InjectMocks
    private DesativarItemCardapioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve desativar item do cardápio com sucesso")
    void deveDesativarItemComSucesso() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var donoId = UUID.randomUUID();

        var input = DesativarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .itemId(itemId)
                .usuarioOperadorId(donoId)
                .build();

        var item = ItemCardapio.builder()
                .id(itemId)
                .nome("Prato para Desativar")
                .ativo(true)
                .build();

        when(itemCardapioGateway.buscarAtivoPorId(restauranteId, itemId)).thenReturn(Optional.of(item));

        usecase.executar(input);

        assertThat(item.getAtivo()).isFalse();
        verify(validarPermissaoUsuarioUsecase).executar(any(ValidarPermissaoUsuarioInputDto.class));
        verify(itemCardapioGateway).desativar(restauranteId, itemId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando input ou IDs forem nulos")
    void deveLancarExcecaoQuandoIdsNulos() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");

        var inputSemItem = DesativarItemCardapioInputDto.builder().restauranteId(UUID.randomUUID()).build();
        assertThatThrownBy(() -> usecase.executar(inputSemItem))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do item do cardápio é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando item não for encontrado")
    void deveLancarExcecaoQuandoItemNaoEncontrado() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();

        var input = DesativarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .itemId(itemId)
                .usuarioOperadorId(UUID.randomUUID())
                .build();

        when(itemCardapioGateway.buscarAtivoPorId(restauranteId, itemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Item do cardápio não encontrado.");
    }
}
