package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Entidade JPA: ItemCardapioEntity")
class ItemCardapioEntityTest {

    @Test
    @DisplayName("Deve inicializar dataCriacao e ativo automaticamente no prePersist")
    void devePreencherCamposNoPrePersist() {
        var entity = ItemCardapioEntity.builder()
                .restauranteId(UUID.randomUUID())
                .nome("Prato")
                .descricao("Desc")
                .preco(BigDecimal.TEN)
                .disponivelApenasRestaurante(false)
                .build();

        entity.prePersist();

        assertThat(entity.getDataCriacao()).isNotNull();
        assertThat(entity.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve atualizar dataAlteracao no preUpdate")
    void deveAtualizarDataAlteracaoNoPreUpdate() {
        var entity = ItemCardapioEntity.builder().nome("Item").build();

        entity.preUpdate();

        assertThat(entity.getDataAlteracao()).isNotNull();
    }
}
