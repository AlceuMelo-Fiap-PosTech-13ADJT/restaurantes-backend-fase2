package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.ItemCardapioEntity;

class ItemCardapioMapperTest {

    private ItemCardapioMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ItemCardapioMapper();
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Domínio (toDomain)")
    class ToDomainTests {

        @Test
        @DisplayName("Deve mapear ItemCardapioEntity completa para ItemCardapio domínio")
        void deveMapearEntityCompletaParaDomain() {
            UUID id = UUID.randomUUID();
            UUID restauranteId = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            ItemCardapioEntity entity = ItemCardapioEntity.builder()
                    .id(id)
                    .restauranteId(restauranteId)
                    .nome("Pizza Margherita")
                    .descricao("Molho de tomate, mussarela e manjericão")
                    .preco(new BigDecimal("49.90"))
                    .disponivelApenasRestaurante(false)
                    .fotoPath("/images/margherita.jpg")
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            ItemCardapio domain = mapper.toDomain(entity);

            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isEqualTo(id);
            assertThat(domain.getNome()).isEqualTo("Pizza Margherita");
            assertThat(domain.getDescricao()).isEqualTo("Molho de tomate, mussarela e manjericão");
            assertThat(domain.getPreco()).isEqualTo(new BigDecimal("49.90"));
            assertThat(domain.getDisponivelApenasRestaurante()).isFalse();
            assertThat(domain.getFotoPath()).isEqualTo("/images/margherita.jpg");
            assertThat(domain.getAtivo()).isTrue();
            assertThat(domain.getDataCriacao()).isEqualTo(agora);
            assertThat(domain.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve retornar null quando entity for nula")
        void deveRetornarNullQuandoEntityNula() {
            ItemCardapio domain = mapper.toDomain(null);
            assertThat(domain).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Entity (toEntity)")
    class ToEntityTests {

        @Test
        @DisplayName("Deve mapear ItemCardapio domínio para ItemCardapioEntity com restauranteId")
        void deveMapearDomainParaEntityComRestauranteId() {
            UUID id = UUID.randomUUID();
            UUID restauranteId = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            ItemCardapio domain = ItemCardapio.builder()
                    .id(id)
                    .nome("Suco Natural")
                    .descricao("Suco de laranja natural")
                    .preco(new BigDecimal("12.00"))
                    .disponivelApenasRestaurante(true)
                    .fotoPath("/images/suco.jpg")
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            ItemCardapioEntity entity = mapper.toEntity(restauranteId, domain);

            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getRestauranteId()).isEqualTo(restauranteId);
            assertThat(entity.getNome()).isEqualTo("Suco Natural");
            assertThat(entity.getDescricao()).isEqualTo("Suco de laranja natural");
            assertThat(entity.getPreco()).isEqualTo(new BigDecimal("12.00"));
            assertThat(entity.getDisponivelApenasRestaurante()).isTrue();
            assertThat(entity.getFotoPath()).isEqualTo("/images/suco.jpg");
            assertThat(entity.getAtivo()).isTrue();
            assertThat(entity.getDataCriacao()).isEqualTo(agora);
            assertThat(entity.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve mapear ItemCardapio domínio para ItemCardapioEntity sem restauranteId especificado")
        void deveMapearDomainParaEntitySemRestauranteId() {
            ItemCardapio domain = ItemCardapio.builder()
                    .id(UUID.randomUUID())
                    .nome("Sobremesa")
                    .descricao("Pudim de leite")
                    .preco(new BigDecimal("15.00"))
                    .disponivelApenasRestaurante(false)
                    .ativo(true)
                    .build();

            ItemCardapioEntity entity = mapper.toEntity(domain);

            assertThat(entity).isNotNull();
            assertThat(entity.getRestauranteId()).isNull();
            assertThat(entity.getNome()).isEqualTo("Sobremesa");
        }

        @Test
        @DisplayName("Deve retornar null quando domain for nulo")
        void deveRetornarNullQuandoDomainNulo() {
            assertThat(mapper.toEntity(null)).isNull();
            assertThat(mapper.toEntity(UUID.randomUUID(), null)).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de mapeamento de lista (toDomainList)")
    class ToDomainListTests {

        @Test
        @DisplayName("Deve mapear lista de entities para lista de domínios")
        void deveMapearListaDeEntities() {
            ItemCardapioEntity entity1 = ItemCardapioEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("Item 1")
                    .descricao("Desc 1")
                    .preco(BigDecimal.TEN)
                    .disponivelApenasRestaurante(false)
                    .ativo(true)
                    .build();

            ItemCardapioEntity entity2 = ItemCardapioEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("Item 2")
                    .descricao("Desc 2")
                    .preco(BigDecimal.ONE)
                    .disponivelApenasRestaurante(true)
                    .ativo(true)
                    .build();

            List<ItemCardapio> lista = mapper.toDomainList(List.of(entity1, entity2));

            assertThat(lista).hasSize(2);
            assertThat(lista.get(0).getNome()).isEqualTo("Item 1");
            assertThat(lista.get(1).getNome()).isEqualTo("Item 2");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando lista de entities for nula")
        void deveRetornarListaVaziaQuandoListaNula() {
            List<ItemCardapio> lista = mapper.toDomainList(null);
            assertThat(lista).isEmpty();
        }
    }
}
