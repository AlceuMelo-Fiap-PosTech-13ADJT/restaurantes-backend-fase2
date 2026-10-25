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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.TipoUsuarioResponseJson;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.UsuarioResponseJson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/tipos-usuario")
@RequiredArgsConstructor
public class TipoUsuarioApi {

    private final TipoUsuarioController tipoUsuarioController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoUsuarioResponseJson criar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody CriarTipoUsuarioRequestJson request) {
        CriarTipoUsuarioInputDto input = CriarTipoUsuarioInputDto.builder()
                .nome(request.getNome())
                .build();
        TipoUsuarioOutputDto output = tipoUsuarioController.criar(input);
        return TipoUsuarioResponseJson.fromDto(output);
    }

    @PutMapping("/{id}")
    public TipoUsuarioResponseJson atualizar(
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody AtualizarTipoUsuarioRequestJson request) {
        AtualizarTipoUsuarioInputDto input = AtualizarTipoUsuarioInputDto.builder()
                .id(id)
                .nome(request.getNome())
                .build();
        TipoUsuarioOutputDto output = tipoUsuarioController.atualizar(input);
        return TipoUsuarioResponseJson.fromDto(output);
    }

    @GetMapping("/{id}")
    public TipoUsuarioResponseJson buscarPorId(
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        TipoUsuarioOutputDto output = tipoUsuarioController.buscarPorId(id);
        return TipoUsuarioResponseJson.fromDto(output);
    }

    @GetMapping
    public List<TipoUsuarioResponseJson> listar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        return tipoUsuarioController.listar().stream()
                .map(TipoUsuarioResponseJson::fromDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        tipoUsuarioController.desativar(id);
    }

    @PostMapping("/usuarios/{usuarioId}/associar")
    public UsuarioResponseJson associar(
            @PathVariable UUID usuarioId,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody AssociarTipoUsuarioRequestJson request) {
        AssociarTipoUsuarioInputDto input = AssociarTipoUsuarioInputDto.builder()
                .usuarioId(usuarioId)
                .tipoUsuarioId(request.getTipoUsuarioId())
                .build();
        UsuarioOutputDto output = tipoUsuarioController.associar(input);
        return UsuarioResponseJson.fromDto(output);
    }
}
