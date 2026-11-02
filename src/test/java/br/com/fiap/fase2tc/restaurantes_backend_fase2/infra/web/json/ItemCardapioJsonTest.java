package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Testes de DTOs JSON: ItemCardapio")
class ItemCardapioJsonTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("ItemCardapioRequestJson válido não deve ter violações")
    void itemCardapioValido() {
        ItemCardapioRequestJson request = ItemCardapioRequestJson.builder()
                .nome("Hambúrguer Artesanal")
                .descricao("Blend 180g, queijo cheddar e bacon")
                .preco(new BigDecimal("35.50"))
                .disponivelApenasRestaurante(false)
                .fotoPath("/img/burger.jpg")
                .build();

        Set<ConstraintViolation<ItemCardapioRequestJson>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("ItemCardapioRequestJson com preço zero ou negativo deve ter violações")
    void itemCardapioPrecoInvalido() {
        ItemCardapioRequestJson request = ItemCardapioRequestJson.builder()
                .nome("Item Teste")
                .descricao("Descricao Teste")
                .preco(BigDecimal.ZERO)
                .disponivelApenasRestaurante(true)
                .build();

        Set<ConstraintViolation<ItemCardapioRequestJson>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("ItemCardapioResponseJson deve mapear corretamente a partir do OutputDto")
    void itemCardapioResponseFromDto() {
        UUID id = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        ItemCardapioOutputDto dto = ItemCardapioOutputDto.builder()
                .id(id)
                .restauranteId(restauranteId)
                .nome("Suco Natural")
                .descricao("Laranja")
                .preco(new BigDecimal("10.00"))
                .disponivelApenasRestaurante(true)
                .fotoPath("/suco.jpg")
                .ativo(true)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        ItemCardapioResponseJson response = ItemCardapioResponseJson.fromDto(dto);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getRestauranteId()).isEqualTo(restauranteId);
        assertThat(response.getNome()).isEqualTo("Suco Natural");
        assertThat(response.getPreco()).isEqualTo(new BigDecimal("10.00"));
        assertThat(response.getDisponivelApenasRestaurante()).isTrue();
        assertThat(response.getAtivo()).isTrue();
    }
}
