package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuarioOutputDto {
    private UUID id;
    private TipoUsuarioEnum tipo;
    private String nome;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public static TipoUsuarioOutputDto fromDomain(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        return TipoUsuarioOutputDto.builder()
                .id(tipoUsuario.getId())
                .tipo(tipoUsuario.getTipo())
                .nome(tipoUsuario.getNome())
                .ativo(tipoUsuario.getAtivo())
                .dataCriacao(tipoUsuario.getDataCriacao())
                .dataAlteracao(tipoUsuario.getDataAlteracao())
                .build();
    }
}
