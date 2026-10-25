package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarItemCardapioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarItensCardapioUsecase;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Controller Core: CardapioController")
class CardapioControllerTest {

    @Mock
    private CriarItemCardapioUsecase criarItemCardapioUsecase;

    @Mock
    private AtualizarItemCardapioUsecase atualizarItemCardapioUsecase;

    @Mock
    private BuscarItemCardapioPorIdUsecase buscarItemCardapioPorIdUsecase;

    @Mock
    private ListarItensCardapioUsecase listarItensCardapioUsecase;

    @Mock
    private DesativarItemCardapioUsecase desativarItemCardapioUsecase;

    @InjectMocks
    private CardapioController controller;

    @Test
    @DisplayName("Deve delegar criação para CriarItemCardapioUsecase")
    void deveDelegarCriacao() {
        var input = CriarItemCardapioInputDto.builder().nome("Spaghetti").preco(BigDecimal.valueOf(35.0)).build();
        var expected = ItemCardapioOutputDto.builder().id(UUID.randomUUID()).nome("Spaghetti").build();

        when(criarItemCardapioUsecase.executar(input)).thenReturn(expected);

        ItemCardapioOutputDto result = controller.criar(input);

        assertThat(result).isSameAs(expected);
        verify(criarItemCardapioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar adicionarItem para CriarItemCardapioUsecase")
    void deveDelegarAdicionarItem() {
        var input = CriarItemCardapioInputDto.builder().nome("Ravioli").build();
        var expected = ItemCardapioOutputDto.builder().id(UUID.randomUUID()).nome("Ravioli").build();

        when(criarItemCardapioUsecase.executar(input)).thenReturn(expected);

        ItemCardapioOutputDto result = controller.adicionarItem(input);

        assertThat(result).isSameAs(expected);
        verify(criarItemCardapioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar atualização para AtualizarItemCardapioUsecase")
    void deveDelegarAtualizacao() {
        var input = AtualizarItemCardapioInputDto.builder().nome("Novo Nome").build();
        var expected = ItemCardapioOutputDto.builder().id(UUID.randomUUID()).nome("Novo Nome").build();

        when(atualizarItemCardapioUsecase.executar(input)).thenReturn(expected);

        ItemCardapioOutputDto result = controller.atualizar(input);

        assertThat(result).isSameAs(expected);
        verify(atualizarItemCardapioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar atualizarItem para AtualizarItemCardapioUsecase")
    void deveDelegarAtualizarItem() {
        var input = AtualizarItemCardapioInputDto.builder().nome("Outro Nome").build();
        var expected = ItemCardapioOutputDto.builder().id(UUID.randomUUID()).nome("Outro Nome").build();

        when(atualizarItemCardapioUsecase.executar(input)).thenReturn(expected);

        ItemCardapioOutputDto result = controller.atualizarItem(input);

        assertThat(result).isSameAs(expected);
        verify(atualizarItemCardapioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar busca por ID para BuscarItemCardapioPorIdUsecase")
    void deveDelegarBuscaPorId() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var expected = ItemCardapioOutputDto.builder().id(itemId).nome("Pizza").build();

        when(buscarItemCardapioPorIdUsecase.executar(restauranteId, itemId)).thenReturn(expected);

        ItemCardapioOutputDto result = controller.buscarPorId(restauranteId, itemId);

        assertThat(result).isSameAs(expected);
        verify(buscarItemCardapioPorIdUsecase).executar(restauranteId, itemId);
    }

    @Test
    @DisplayName("Deve delegar listagem para ListarItensCardapioUsecase")
    void deveDelegarListagem() {
        var restauranteId = UUID.randomUUID();
        var expected = List.of(ItemCardapioOutputDto.builder().id(UUID.randomUUID()).build());

        when(listarItensCardapioUsecase.executar(restauranteId)).thenReturn(expected);

        List<ItemCardapioOutputDto> result1 = controller.listar(restauranteId);
        List<ItemCardapioOutputDto> result2 = controller.listarItens(restauranteId);
        List<ItemCardapioOutputDto> result3 = controller.listarItens(restauranteId, UUID.randomUUID());

        assertThat(result1).isSameAs(expected);
        assertThat(result2).isSameAs(expected);
        assertThat(result3).isSameAs(expected);
        verify(listarItensCardapioUsecase, times(3)).executar(restauranteId);
    }

    @Test
    @DisplayName("Deve delegar desativação via DTO para DesativarItemCardapioUsecase")
    void deveDelegarDesativacaoDto() {
        var input = DesativarItemCardapioInputDto.builder()
                .restauranteId(UUID.randomUUID())
                .itemId(UUID.randomUUID())
                .usuarioOperadorId(UUID.randomUUID())
                .build();

        controller.desativar(input);

        verify(desativarItemCardapioUsecase).executar(input);
    }

    @Test
    @DisplayName("Deve delegar desativação via parâmetros de conveniência")
    void deveDelegarDesativacaoConveniente() {
        var restauranteId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var usuarioOperadorId = UUID.randomUUID();

        controller.desativar(restauranteId, itemId, usuarioOperadorId);

        verify(desativarItemCardapioUsecase).executar(any(DesativarItemCardapioInputDto.class));
    }
}
