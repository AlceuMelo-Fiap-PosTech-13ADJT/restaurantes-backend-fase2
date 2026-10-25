package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: CriarItemCardapioUsecase")
class CriarItemCardapioUsecaseTest {

    @Mock
    private ItemCardapioGateway itemCardapioGateway;

    @Mock
    private ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @InjectMocks
    private CriarItemCardapioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve criar item do cardápio com sucesso")
    void deveCriarItemCardapioComSucesso() {
        var restauranteId = UUID.randomUUID();
        var donoId = UUID.randomUUID();
        var itemId = UUID.randomUUID();

        var input = CriarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .usuarioOperadorId(donoId)
                .nome("Lasanha Bolonhesa")
                .descricao("Massa fresca com molho bolonhesa artesanal")
                .preco(new BigDecimal("48.90"))
                .disponivelApenasRestaurante(false)
                .fotoPath("/fotos/lasanha.jpg")
                .build();

        var salvo = ItemCardapio.builder()
                .id(itemId)
                .nome("Lasanha Bolonhesa")
                .descricao("Massa fresca com molho bolonhesa artesanal")
                .preco(new BigDecimal("48.90"))
                .disponivelApenasRestaurante(false)
                .fotoPath("/fotos/lasanha.jpg")
                .ativo(true)
                .build();

        when(itemCardapioGateway.salvar(eq(restauranteId), any(ItemCardapio.class))).thenReturn(salvo);

        ItemCardapioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(itemId);
        assertThat(output.getNome()).isEqualTo("Lasanha Bolonhesa");
        verify(validarPermissaoUsuarioUsecase).executar(any(ValidarPermissaoUsuarioInputDto.class));
        verify(itemCardapioGateway).salvar(eq(restauranteId), any(ItemCardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando input ou restauranteId for nulo")
    void deveLancarExcecaoQuandoInputNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Dados do item do cardápio são obrigatórios.");

        var inputSemRestaurante = CriarItemCardapioInputDto.builder().nome("Item").build();
        assertThatThrownBy(() -> usecase.executar(inputSemRestaurante))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("ID do restaurante é obrigatório.");
    }
}
