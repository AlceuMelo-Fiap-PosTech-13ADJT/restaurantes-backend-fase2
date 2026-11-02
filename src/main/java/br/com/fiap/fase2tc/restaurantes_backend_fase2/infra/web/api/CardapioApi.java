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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ErroResponseJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ItemCardapioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ItemCardapioResponseJson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/cardapio")
@RequiredArgsConstructor
@Tag(name = "Cardápio", description = "Endpoints para gerenciamento do cardápio e pratos vinculados ao restaurante")
public class CardapioApi {

    private final CardapioController cardapioController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar item ao cardápio", description = "Cadastra um novo prato/item no cardápio do restaurante. Operação restrita ao dono do estabelecimento.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item adicionado ao cardápio com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCardapioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Operador logado não é o dono vinculado a este restaurante",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public ItemCardapioResponseJson criar(
            @Parameter(description = "UUID do restaurante proprietário do cardápio", required = true)
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
    @Operation(summary = "Atualizar item do cardápio", description = "Atualiza os dados de um item do cardápio. Operação restrita ao dono do restaurante.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCardapioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Operador logado não é o dono vinculado a este restaurante",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Item do cardápio ou restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public ItemCardapioResponseJson atualizar(
            @Parameter(description = "UUID do restaurante proprietário do cardápio", required = true)
            @PathVariable UUID restauranteId,
            @Parameter(description = "UUID do item do cardápio a ser atualizado", required = true)
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
    @Operation(summary = "Buscar item do cardápio por ID", description = "Retorna os detalhes de um item específico do cardápio.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCardapioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Item do cardápio ou restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public ItemCardapioResponseJson buscarPorId(
            @Parameter(description = "UUID do restaurante proprietário do cardápio", required = true)
            @PathVariable UUID restauranteId,
            @Parameter(description = "UUID do item do cardápio", required = true)
            @PathVariable UUID itemId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        ItemCardapioOutputDto output = cardapioController.buscarPorId(restauranteId, itemId);
        return ItemCardapioResponseJson.fromDto(output);
    }

    @GetMapping
    @Operation(summary = "Listar itens do cardápio", description = "Retorna todos os itens ativos do cardápio de um restaurante específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Itens do cardápio listados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public List<ItemCardapioResponseJson> listar(
            @Parameter(description = "UUID do restaurante", required = true)
            @PathVariable UUID restauranteId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        return cardapioController.listarItens(restauranteId, usuarioLogadoId).stream()
                .map(ItemCardapioResponseJson::fromDto)
                .toList();
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativar item do cardápio", description = "Executa a exclusão lógica (soft delete) de um item do cardápio. Operação restrita ao dono do restaurante.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Item desativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Operador logado não é o dono vinculado a este restaurante",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Item do cardápio ou restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public void desativar(
            @Parameter(description = "UUID do restaurante", required = true)
            @PathVariable UUID restauranteId,
            @Parameter(description = "UUID do item do cardápio a ser desativado", required = true)
            @PathVariable UUID itemId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        cardapioController.desativar(restauranteId, itemId, usuarioLogadoId);
    }
}
