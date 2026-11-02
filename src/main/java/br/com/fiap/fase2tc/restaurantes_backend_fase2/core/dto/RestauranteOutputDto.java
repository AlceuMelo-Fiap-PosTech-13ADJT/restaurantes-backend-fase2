package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteOutputDto {
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

    public static RestauranteOutputDto fromDomain(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }
        return RestauranteOutputDto.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .endereco(restaurante.getEndereco())
                .tipoCozinha(restaurante.getTipoCozinha())
                .horarioFuncionamento(restaurante.getHorarioFuncionamento())
                .donoId(restaurante.getDono() != null ? restaurante.getDono().getId() : null)
                .donoNome(restaurante.getDono() != null ? restaurante.getDono().getNome() : null)
                .ativo(restaurante.getAtivo())
                .dataCriacao(restaurante.getDataCriacao())
                .dataAlteracao(restaurante.getDataAlteracao())
                .build();
    }
}
