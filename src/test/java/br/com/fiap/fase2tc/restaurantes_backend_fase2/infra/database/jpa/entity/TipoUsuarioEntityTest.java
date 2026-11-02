package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;

@DisplayName("Testes de Entidade JPA: TipoUsuarioEntity")
class TipoUsuarioEntityTest {

    @Test
    @DisplayName("Deve inicializar dataCriacao, ativo e nome automaticamente no prePersist")
    void devePreencherCamposNoPrePersist() {
        var entity = TipoUsuarioEntity.builder()
                .id(UUID.randomUUID())
                .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                .build();

        entity.prePersist();

        assertThat(entity.getDataCriacao()).isNotNull();
        assertThat(entity.getAtivo()).isTrue();
        assertThat(entity.getNome()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE.getDescricao());
    }

    @Test
    @DisplayName("Deve atualizar dataAlteracao no preUpdate")
    void deveAtualizarDataAlteracaoNoPreUpdate() {
        var entity = TipoUsuarioEntity.builder()
                .tipo(TipoUsuarioEnum.CLIENTE)
                .build();

        entity.preUpdate();

        assertThat(entity.getDataAlteracao()).isNotNull();
        assertThat(entity.getNome()).isEqualTo(TipoUsuarioEnum.CLIENTE.getDescricao());
    }
}
