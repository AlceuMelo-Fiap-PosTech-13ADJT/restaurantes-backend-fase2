package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioOutputDto {
    private UUID id;
    private String nome;
    private String email;
    private UUID restauranteId;
    private UUID tipoUsuarioId;
    private String tipoUsuarioNome;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public static UsuarioOutputDto fromDomain(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioOutputDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .restauranteId(usuario.getRestauranteId())
                .tipoUsuarioId(usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getId() : null)
                .tipoUsuarioNome(usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getNome() : null)
                .ativo(usuario.getAtivo())
                .dataCriacao(usuario.getDataCriacao())
                .dataAlteracao(usuario.getDataAlteracao())
                .build();
    }
}
