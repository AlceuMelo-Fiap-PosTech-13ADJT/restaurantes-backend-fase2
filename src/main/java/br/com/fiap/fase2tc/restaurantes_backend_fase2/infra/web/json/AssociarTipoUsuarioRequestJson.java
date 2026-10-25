package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
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
public class AssociarTipoUsuarioRequestJson {

    @NotNull(message = "O ID do tipo de usuário é obrigatório")
    private UUID tipoUsuarioId;
}
