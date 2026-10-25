package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
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
public class UsuarioResponseJson {

    private UUID id;
    private String nome;
    private String email;
    private UUID tipoUsuarioId;
    private String tipoUsuarioNome;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public UsuarioResponseJson(UUID id) {
        this.id = id;
    }

    public static UsuarioResponseJson fromDto(UsuarioOutputDto dto) {
        if (dto == null) {
            return null;
        }
        return UsuarioResponseJson.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .tipoUsuarioId(dto.getTipoUsuarioId())
                .tipoUsuarioNome(dto.getTipoUsuarioNome())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .dataAlteracao(dto.getDataAlteracao())
                .build();
    }
}
