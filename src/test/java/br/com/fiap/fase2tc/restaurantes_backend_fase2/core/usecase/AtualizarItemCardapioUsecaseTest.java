package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: AtualizarItemCardapioUsecase")
class AtualizarItemCardapioUsecaseTest {

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    @Mock
    private ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @InjectMocks
    private AtualizarItemCardapioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve atualizar item do cardápio com sucesso")
    void deveAtualizarItemComSucesso() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var donoId = UUID.randomUUID();

        var input = AtualizarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .itemId(itemId)
                .usuarioOperadorId(donoId)
                .nome("Novo Prato")
                .descricao("Nova Descrição")
                .preco(new BigDecimal("35.00"))
                .disponivelApenasRestaurante(true)
                .fotoPath("/fotos/novo.jpg")
                .build();

        var item = ItemCardapio.builder()
                .id(itemId)
                .nome("Prato Antigo")
                .descricao("Descrição Antiga")
                .preco(new BigDecimal("30.00"))
                .disponivelApenasRestaurante(false)
                .fotoPath("/fotos/antigo.jpg")
                .ativo(true)
                .build();

        when(itemCardapioGateway.buscarAtivoPorId(restauranteId, itemId)).thenReturn(Optional.of(item));
        when(itemCardapioGateway.atualizar(eq(restauranteId), any(ItemCardapio.class))).thenReturn(item);

        ItemCardapioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getNome()).isEqualTo("Novo Prato");
        verify(validarPermissaoUsuarioUsecase).executar(any(ValidarPermissaoUsuarioInputDto.class));
        verify(itemCardapioGateway).atualizar(eq(restauranteId), any(ItemCardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando IDs forem nulos")
    void deveLancarExcecaoQuandoIdsNulos() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");

        var inputSemItem = AtualizarItemCardapioInputDto.builder().restauranteId(UUID.randomUUID()).build();
        assertThatThrownBy(() -> usecase.executar(inputSemItem))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do item do cardápio é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando item do cardápio não for encontrado")
    void deveLancarExcecaoQuandoItemNaoEncontrado() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();

        var input = AtualizarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .itemId(itemId)
                .usuarioOperadorId(UUID.randomUUID())
                .nome("Nome")
                .build();

        when(itemCardapioGateway.buscarAtivoPorId(restauranteId, itemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.executar(input))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Item do cardápio não encontrado.");
    }
}
