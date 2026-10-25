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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.UsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AssociarTipoUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AtualizarUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.CriarUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ErroResponseJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.UsuarioResponseJson;
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
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento completo, consulta e associação de perfis de usuários")
public class UsuarioApi {

    private final UsuarioController usuarioController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário vinculado a um tipo de usuário e opcionalmente a um restaurante.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuário ou restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public UsuarioResponseJson criar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody CriarUsuarioRequestJson request) {
        CriarUsuarioInputDto input = CriarUsuarioInputDto.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .tipoUsuarioId(request.getTipoUsuarioId())
                .restauranteId(request.getRestauranteId())
                .build();
        UsuarioOutputDto output = usuarioController.criar(input);
        return UsuarioResponseJson.fromDto(output);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Usuário, tipo de usuário ou restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public UsuarioResponseJson atualizar(
            @Parameter(description = "UUID do usuário a ser atualizado", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody AtualizarUsuarioRequestJson request) {
        AtualizarUsuarioInputDto input = AtualizarUsuarioInputDto.builder()
                .id(id)
                .nome(request.getNome())
                .email(request.getEmail())
                .tipoUsuarioId(request.getTipoUsuarioId())
                .restauranteId(request.getRestauranteId())
                .build();
        UsuarioOutputDto output = usuarioController.atualizar(input);
        return UsuarioResponseJson.fromDto(output);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico pelo seu identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public UsuarioResponseJson buscarPorId(
            @Parameter(description = "UUID do usuário", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        UsuarioOutputDto output = usuarioController.buscarPorId(id);
        return UsuarioResponseJson.fromDto(output);
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar usuários por restaurante", description = "Retorna a listagem de todos os usuários ativos associados ao restaurante informado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public List<UsuarioResponseJson> listarPorRestaurante(
            @Parameter(description = "UUID do restaurante", required = true)
            @PathVariable UUID restauranteId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        return usuarioController.listarPorRestaurante(restauranteId).stream()
                .map(UsuarioResponseJson::fromDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativar usuário", description = "Executa a exclusão lógica (soft delete) do usuário informado.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public void desativar(
            @Parameter(description = "UUID do usuário a ser desativado", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        usuarioController.desativar(id);
    }

    @PostMapping("/{usuarioId}/tipo-usuario")
    @Operation(summary = "Associar tipo de usuário a um usuário", description = "Vincula um tipo de usuário (perfil) a um usuário específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de usuário associado com sucesso ao usuário",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Usuário ou tipo de usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public UsuarioResponseJson associarTipoUsuario(
            @Parameter(description = "UUID do usuário a ter o tipo associado", required = true)
            @PathVariable UUID usuarioId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody AssociarTipoUsuarioRequestJson request) {
        AssociarTipoUsuarioInputDto input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(usuarioId)
                .tipoUsuarioId(request.getTipoUsuarioId())
                .build();
        UsuarioOutputDto output = usuarioController.associarTipoUsuario(input);
        return UsuarioResponseJson.fromDto(output);
    }
}
