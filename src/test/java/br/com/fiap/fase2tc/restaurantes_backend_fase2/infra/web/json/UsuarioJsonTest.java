package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;

@DisplayName("Testes de DTOs JSON: Usuario")
class UsuarioJsonTest {

    @Test
    @DisplayName("UsuarioResponseJson deve mapear corretamente a partir de UsuarioOutputDto")
    void usuarioResponseFromDto() {
        UUID id = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        UsuarioOutputDto dto = UsuarioOutputDto.builder()
                .id(id)
                .nome("Fernanda Lima")
                .email("fernanda@email.com")
                .tipoUsuarioId(tipoId)
                .tipoUsuarioNome("Dono de restaurante")
                .ativo(true)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        UsuarioResponseJson response = UsuarioResponseJson.fromDto(dto);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getNome()).isEqualTo("Fernanda Lima");
        assertThat(response.getEmail()).isEqualTo("fernanda@email.com");
        assertThat(response.getTipoUsuarioId()).isEqualTo(tipoId);
        assertThat(response.getTipoUsuarioNome()).isEqualTo("Dono de restaurante");
        assertThat(response.getAtivo()).isTrue();
    }
}
