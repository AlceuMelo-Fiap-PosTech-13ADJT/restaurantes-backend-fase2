package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
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
public class TipoUsuarioResponseJson {

    private UUID id;
    private TipoUsuarioEnum tipo;
    private String nome;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public TipoUsuarioResponseJson(UUID id) {
        this.id = id;
    }

    public static TipoUsuarioResponseJson fromDto(TipoUsuarioOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return TipoUsuarioResponseJson.builder()
                .id(dto.getId())
                .tipo(dto.getTipo())
                .nome(dto.getNome())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .dataAlteracao(dto.getDataAlteracao())
                .build();
    }
}
