package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller;

import java.util.List;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.DesativarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.AtualizarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.BuscarItemCardapioPorIdUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.CriarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.DesativarItemCardapioUsecase;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase.ListarItensCardapioUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class CardapioController {

    private final CriarItemCardapioUsecase criarItemCardapioUsecase;
    private final AtualizarItemCardapioUsecase atualizarItemCardapioUsecase;
    private final BuscarItemCardapioPorIdUsecase buscarItemCardapioPorIdUsecase;
    private final ListarItensCardapioUsecase listarItensCardapioUsecase;
    private final DesativarItemCardapioUsecase desativarItemCardapioUsecase;

    public ItemCardapioOutputDto criar(CriarItemCardapioInputDto inputDto) {
        return criarItemCardapioUsecase.executar(inputDto);
    }

    public ItemCardapioOutputDto adicionarItem(CriarItemCardapioInputDto inputDto) {
        return criar(inputDto);
    }

    public ItemCardapioOutputDto atualizar(AtualizarItemCardapioInputDto inputDto) {
        return atualizarItemCardapioUsecase.executar(inputDto);
    }

    public ItemCardapioOutputDto atualizarItem(AtualizarItemCardapioInputDto inputDto) {
        return atualizar(inputDto);
    }

    public ItemCardapioOutputDto buscarPorId(UUID restauranteId, UUID itemId) {
        return buscarItemCardapioPorIdUsecase.executar(restauranteId, itemId);
    }

    public List<ItemCardapioOutputDto> listar(UUID restauranteId) {
        return listarItensCardapioUsecase.executar(restauranteId);
    }

    public List<ItemCardapioOutputDto> listarItens(UUID restauranteId) {
        return listar(restauranteId);
    }

    public List<ItemCardapioOutputDto> listarItens(UUID restauranteId, UUID usuarioOperadorId) {
        return listar(restauranteId);
    }

    public void desativar(DesativarItemCardapioInputDto inputDto) {
        desativarItemCardapioUsecase.executar(inputDto);
    }

    public void desativar(UUID restauranteId, UUID itemId, UUID usuarioOperadorId) {
        desativar(DesativarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .itemId(itemId)
                .usuarioOperadorId(usuarioOperadorId)
                .build());
    }
}
