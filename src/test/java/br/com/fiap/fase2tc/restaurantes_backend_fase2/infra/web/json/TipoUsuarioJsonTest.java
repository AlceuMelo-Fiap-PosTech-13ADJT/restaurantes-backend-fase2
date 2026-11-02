package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.TipoUsuarioOutputDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Testes de DTOs JSON: TipoUsuario")
class TipoUsuarioJsonTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("CriarTipoUsuarioRequestJson válido não deve ter violações")
    void criarTipoUsuarioValido() {
        CriarTipoUsuarioRequestJson request = CriarTipoUsuarioRequestJson.builder()
                .nome("Cliente")
                .build();

        Set<ConstraintViolation<CriarTipoUsuarioRequestJson>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("CriarTipoUsuarioRequestJson com nome em branco deve ter violações")
    void criarTipoUsuarioInvalido() {
        CriarTipoUsuarioRequestJson request = CriarTipoUsuarioRequestJson.builder()
                .nome("   ")
                .build();

        Set<ConstraintViolation<CriarTipoUsuarioRequestJson>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("AtualizarTipoUsuarioRequestJson válido não deve ter violações")
    void atualizarTipoUsuarioValido() {
        AtualizarTipoUsuarioRequestJson request = AtualizarTipoUsuarioRequestJson.builder()
                .nome("Dono de restaurante")
                .build();

        Set<ConstraintViolation<AtualizarTipoUsuarioRequestJson>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("AssociarTipoUsuarioRequestJson com ID nulo deve ter violação")
    void associarTipoUsuarioInvalido() {
        AssociarTipoUsuarioRequestJson request = AssociarTipoUsuarioRequestJson.builder()
                .tipoUsuarioId(null)
                .build();

        Set<ConstraintViolation<AssociarTipoUsuarioRequestJson>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("TipoUsuarioResponseJson deve mapear corretamente a partir do OutputDto")
    void tipoUsuarioResponseFromDto() {
        UUID id = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        TipoUsuarioOutputDto dto = TipoUsuarioOutputDto.builder()
                .id(id)
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .nome("Dono de restaurante")
                .ativo(true)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        TipoUsuarioResponseJson response = TipoUsuarioResponseJson.fromDto(dto);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
        assertThat(response.getNome()).isEqualTo("Dono de restaurante");
        assertThat(response.getAtivo()).isTrue();
        assertThat(response.getDataCriacao()).isEqualTo(agora);
    }
}
