package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarRestauranteOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.RestauranteOutputDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Testes de DTOs JSON: Restaurante")
class RestauranteJsonTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("CriarRestauranteRequestJson válido não deve ter violações")
    void criarRestauranteValido() {
        CriarRestauranteRequestJson request = CriarRestauranteRequestJson.builder()
                .nome("Cantina Bella")
                .endereco("Rua 1, 100")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("18:00 - 23:00")
                .donoId(UUID.randomUUID())
                .build();

        Set<ConstraintViolation<CriarRestauranteRequestJson>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("CriarRestauranteRequestJson com campos nulos deve ter violações")
    void criarRestauranteComCamposNulos() {
        CriarRestauranteRequestJson request = CriarRestauranteRequestJson.builder()
                .nome("")
                .endereco(null)
                .tipoCozinha(" ")
                .horarioFuncionamento(null)
                .donoId(null)
                .build();

        Set<ConstraintViolation<CriarRestauranteRequestJson>> violations = validator.validate(request);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(5);
    }

    @Test
    @DisplayName("AtualizarRestauranteRequestJson válido não deve ter violações")
    void atualizarRestauranteValido() {
        AtualizarRestauranteRequestJson request = AtualizarRestauranteRequestJson.builder()
                .nome("Novo Nome")
                .endereco("Novo Endereco")
                .tipoCozinha("Japonesa")
                .horarioFuncionamento("11:00 - 22:00")
                .build();

        Set<ConstraintViolation<AtualizarRestauranteRequestJson>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("RestauranteResponseJson deve mapear a partir de RestauranteOutputDto e CriarRestauranteOutputDto")
    void restauranteResponseFromDtos() {
        UUID id = UUID.randomUUID();
        UUID donoId = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        RestauranteOutputDto outputDto = RestauranteOutputDto.builder()
                .id(id)
                .nome("Churrascaria")
                .endereco("Av Brasil, 500")
                .tipoCozinha("Brasileira")
                .horarioFuncionamento("12:00 - 23:00")
                .donoId(donoId)
                .donoNome("Marcos")
                .ativo(true)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        RestauranteResponseJson response = RestauranteResponseJson.fromDto(outputDto);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getNome()).isEqualTo("Churrascaria");
        assertThat(response.getDonoId()).isEqualTo(donoId);
        assertThat(response.getDonoNome()).isEqualTo("Marcos");
        assertThat(response.getAtivo()).isTrue();

        CriarRestauranteOutputDto criarOutput = new CriarRestauranteOutputDto(id);
        RestauranteResponseJson responseCriar = RestauranteResponseJson.fromDto(criarOutput);

        assertThat(responseCriar).isNotNull();
        assertThat(responseCriar.getId()).isEqualTo(id);
    }
}
