package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteResponseJson {

    private UUID id;
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    private UUID donoId;
    private String donoNome;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public RestauranteResponseJson(UUID id) {
        this.id = id;
    }

    public static RestauranteResponseJson fromDto(CriarRestauranteOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return new RestauranteResponseJson(dto.getId());
    }

    public static RestauranteResponseJson fromDto(RestauranteOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return RestauranteResponseJson.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .endereco(dto.getEndereco())
                .tipoCozinha(dto.getTipoCozinha())
                .horarioFuncionamento(dto.getHorarioFuncionamento())
                .donoId(dto.getDonoId())
                .donoNome(dto.getDonoNome())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .dataAlteracao(dto.getDataAlteracao())
                .build();
    }
}
