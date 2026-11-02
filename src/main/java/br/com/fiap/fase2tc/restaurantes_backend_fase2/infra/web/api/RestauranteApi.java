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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.RestauranteController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AtualizarRestauranteRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.CriarRestauranteRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ErroResponseJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.RestauranteResponseJson;
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
@RequestMapping("/v1/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de estabelecimentos, dados cadastrais e controle de proprietário")
public class RestauranteApi {

    private final RestauranteController restauranteController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo restaurante", description = "Cria um novo restaurante vinculado a um usuário com perfil de 'Dono de restaurante'.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Usuário informado para dono não existe ou não tem o perfil 'Dono de restaurante'",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public RestauranteResponseJson criar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody CriarRestauranteRequestJson request) {
        CriarRestauranteInputDto input = CriarRestauranteInputDto.builder()
                .usuarioOperadorId(usuarioLogadoId)
                .nome(request.getNome())
                .endereco(request.getEndereco())
                .tipoCozinha(request.getTipoCozinha())
                .horarioFuncionamento(request.getHorarioFuncionamento())
                .donoId(request.getDonoId())
                .build();
        CriarRestauranteOutputDto output = restauranteController.criar(input);
        return RestauranteResponseJson.fromDto(output);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente. Operação restrita exclusivamente ao dono do estabelecimento.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Operador logado não é o dono vinculado a este restaurante",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public RestauranteResponseJson atualizar(
            @Parameter(description = "UUID do restaurante a ser atualizado", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody AtualizarRestauranteRequestJson request) {
        AtualizarRestauranteInputDto input = AtualizarRestauranteInputDto.builder()
                .restauranteId(id)
                .usuarioOperadorId(usuarioLogadoId)
                .nome(request.getNome())
                .endereco(request.getEndereco())
                .tipoCozinha(request.getTipoCozinha())
                .horarioFuncionamento(request.getHorarioFuncionamento())
                .build();
        RestauranteOutputDto output = restauranteController.atualizar(input);
        return RestauranteResponseJson.fromDto(output);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os detalhes de um restaurante específico pelo seu identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public RestauranteResponseJson buscarPorId(
            @Parameter(description = "UUID do restaurante", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        RestauranteOutputDto output = restauranteController.buscarPorId(id);
        return RestauranteResponseJson.fromDto(output);
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Retorna a listagem de todos os restaurantes ativos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public List<RestauranteResponseJson> listar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        return restauranteController.listar().stream()
                .map(RestauranteResponseJson::fromDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativar restaurante", description = "Executa a exclusão lógica (soft delete) do restaurante. Operação restrita ao dono do estabelecimento.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Restaurante desativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Operador logado não é o dono do restaurante",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public void desativar(
            @Parameter(description = "UUID do restaurante a ser desativado", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        restauranteController.desativar(id, usuarioLogadoId);
    }
}
