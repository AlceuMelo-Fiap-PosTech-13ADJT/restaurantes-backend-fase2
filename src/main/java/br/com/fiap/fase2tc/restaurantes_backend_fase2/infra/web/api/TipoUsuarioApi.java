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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.controller.TipoUsuarioController;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AssociarTipoUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.AtualizarTipoUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.CriarTipoUsuarioRequestJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ErroResponseJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.TipoUsuarioResponseJson;
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
@RequestMapping("/v1/tipos-usuario")
@RequiredArgsConstructor
@Tag(name = "Tipos de Usuário", description = "Gerenciamento dos perfis/tipos de usuário (Dono de restaurante, Cliente) e associação a usuários")
public class TipoUsuarioApi {

    private final TipoUsuarioController tipoUsuarioController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo tipo de usuário", description = "Cria um novo tipo de usuário no sistema (subtipos aceitos: 'Dono de restaurante' ou 'Cliente').")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tipo de usuário criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada (tipo inválido ou já existente)",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public TipoUsuarioResponseJson criar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody CriarTipoUsuarioRequestJson request) {
        CriarTipoUsuarioInputDto input = CriarTipoUsuarioInputDto.builder()
                .nome(request.getNome())
                .usuarioOperadorId(usuarioLogadoId)
                .build();
        TipoUsuarioOutputDto output = tipoUsuarioController.criar(input);
        return TipoUsuarioResponseJson.fromDto(output);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza os dados de um tipo de usuário existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public TipoUsuarioResponseJson atualizar(
            @Parameter(description = "UUID do tipo de usuário a ser atualizado", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody AtualizarTipoUsuarioRequestJson request) {
        AtualizarTipoUsuarioInputDto input = AtualizarTipoUsuarioInputDto.builder()
                .id(id)
                .nome(request.getNome())
                .usuarioOperadorId(usuarioLogadoId)
                .build();
        TipoUsuarioOutputDto output = tipoUsuarioController.atualizar(input);
        return TipoUsuarioResponseJson.fromDto(output);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de usuário por ID", description = "Retorna os detalhes de um tipo de usuário específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public TipoUsuarioResponseJson buscarPorId(
            @Parameter(description = "UUID do tipo de usuário", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        TipoUsuarioOutputDto output = tipoUsuarioController.buscarPorId(id);
        return TipoUsuarioResponseJson.fromDto(output);
    }

    @GetMapping
    @Operation(summary = "Listar tipos de usuário", description = "Retorna a lista de todos os tipos de usuário ativos cadastrados.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public List<TipoUsuarioResponseJson> listar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        return tipoUsuarioController.listar().stream()
                .map(TipoUsuarioResponseJson::fromDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativar tipo de usuário", description = "Executa a exclusão lógica (soft delete) do tipo de usuário.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tipo de usuário desativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Formato de ID inválido ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Operador não autorizado ou sem permissão de dono",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public void desativar(
            @Parameter(description = "UUID do tipo de usuário a ser desativado", required = true)
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        tipoUsuarioController.desativar(id, usuarioLogadoId);
    }

    @PostMapping("/usuarios/{usuarioId}/associar")
    @Operation(summary = "Associar tipo ao usuário", description = "Vincula um tipo de usuário específico a um usuário existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo associado ao usuário com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseJson.class))),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou cabeçalho obrigatório ausente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "403", description = "Operador não autorizado ou sem permissão de dono",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "404", description = "Usuário ou tipo de usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResponseJson.class)))
    })
    public UsuarioResponseJson associar(
            @Parameter(description = "UUID do usuário a ser vinculado", required = true)
            @PathVariable UUID usuarioId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody AssociarTipoUsuarioRequestJson request) {
        AssociarTipoUsuarioInputDto input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(usuarioId)
                .tipoUsuarioId(request.getTipoUsuarioId())
                .usuarioOperadorId(usuarioLogadoId)
                .build();
        UsuarioOutputDto output = tipoUsuarioController.associar(input);
        return UsuarioResponseJson.fromDto(output);
    }
}
