package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.CardapioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ItemCardapioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ItemCardapioResponseJson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/cardapio")
@RequiredArgsConstructor
public class CardapioApi {

    private final CardapioController cardapioController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemCardapioResponseJson criar(
            @PathVariable UUID restauranteId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody ItemCardapioRequestJson request) {
        CriarItemCardapioInputDto input = CriarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .usuarioOperadorId(usuarioLogadoId)
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .preco(request.getPreco())
                .disponivelApenasRestaurante(request.getDisponivelApenasRestaurante())
                .fotoPath(request.getFotoPath())
                .build();
        ItemCardapioOutputDto output = cardapioController.criar(input);
        return ItemCardapioResponseJson.fromDto(output);
    }

    @PutMapping("/{itemId}")
    public ItemCardapioResponseJson atualizar(
            @PathVariable UUID restauranteId,
            @PathVariable UUID itemId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody ItemCardapioRequestJson request) {
        AtualizarItemCardapioInputDto input = AtualizarItemCardapioInputDto.builder()
                .restauranteId(restauranteId)
                .itemId(itemId)
                .usuarioOperadorId(usuarioLogadoId)
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .preco(request.getPreco())
                .disponivelApenasRestaurante(request.getDisponivelApenasRestaurante())
                .fotoPath(request.getFotoPath())
                .build();
        ItemCardapioOutputDto output = cardapioController.atualizar(input);
        return ItemCardapioResponseJson.fromDto(output);
    }

    @GetMapping("/{itemId}")
    public ItemCardapioResponseJson buscarPorId(
            @PathVariable UUID restauranteId,
            @PathVariable UUID itemId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        ItemCardapioOutputDto output = cardapioController.buscarPorId(restauranteId, itemId);
        return ItemCardapioResponseJson.fromDto(output);
    }

    @GetMapping
    public List<ItemCardapioResponseJson> listar(
            @PathVariable UUID restauranteId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        return cardapioController.listarItens(restauranteId, usuarioLogadoId).stream()
                .map(ItemCardapioResponseJson::fromDto)
                .toList();
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(
            @PathVariable UUID restauranteId,
            @PathVariable UUID itemId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        cardapioController.desativar(restauranteId, itemId, usuarioLogadoId);
    }
}
