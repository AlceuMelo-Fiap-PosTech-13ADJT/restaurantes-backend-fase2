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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.RestauranteResponseJson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/restaurantes")
@RequiredArgsConstructor
public class RestauranteApi {

    private final RestauranteController restauranteController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteResponseJson criar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId,
            @Valid @RequestBody CriarRestauranteRequestJson request) {
        CriarRestauranteInputDto input = CriarRestauranteInputDto.builder()
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
    public RestauranteResponseJson atualizar(
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
    public RestauranteResponseJson buscarPorId(
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        RestauranteOutputDto output = restauranteController.buscarPorId(id);
        return RestauranteResponseJson.fromDto(output);
    }

    @GetMapping
    public List<RestauranteResponseJson> listar(
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        return restauranteController.listar().stream()
                .map(RestauranteResponseJson::fromDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(
            @PathVariable UUID id,
            @RequestHeader("usuario-logado-id") UUID usuarioLogadoId) {
        restauranteController.desativar(id, usuarioLogadoId);
    }
}
