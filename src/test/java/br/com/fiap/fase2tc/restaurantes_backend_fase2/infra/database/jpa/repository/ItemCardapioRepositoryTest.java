package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.ItemCardapioEntity;

@SpringBootTest
@Transactional
@DisplayName("Testes de Repositório JPA: ItemCardapioRepository")
class ItemCardapioRepositoryTest {

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Test
    @DisplayName("Deve salvar item do cardápio e buscar por restaurante e filtros")
    void deveSalvarEBuscarItemCardapio() {
        var restauranteId = UUID.randomUUID();

        var item = itemCardapioRepository.save(ItemCardapioEntity.builder()
                .restauranteId(restauranteId)
                .nome("Gnocchi ao Pesto")
                .descricao("Gnocchi caseiro com molho pesto tradicional")
                .preco(new BigDecimal("42.00"))
                .disponivelApenasRestaurante(false)
                .fotoPath("/img/gnocchi.jpg")
                .ativo(true)
                .build());

        assertThat(item.getId()).isNotNull();

        Optional<ItemCardapioEntity> porId = itemCardapioRepository.findByIdAndRestauranteIdAndAtivoTrue(item.getId(), restauranteId);
        assertThat(porId).isPresent();
        assertThat(porId.get().getNome()).isEqualTo("Gnocchi ao Pesto");

        List<ItemCardapioEntity> porRestaurante = itemCardapioRepository.findByRestauranteIdAndAtivoTrue(restauranteId);
        assertThat(porRestaurante).hasSize(1);

        List<ItemCardapioEntity> porNome = itemCardapioRepository.findByRestauranteIdAndNomeContainingIgnoreCaseAndAtivoTrue(restauranteId, "gnocchi");
        assertThat(porNome).hasSize(1);

        boolean existe = itemCardapioRepository.existsByIdAndRestauranteIdAndAtivoTrue(item.getId(), restauranteId);
        assertThat(existe).isTrue();
    }
}
