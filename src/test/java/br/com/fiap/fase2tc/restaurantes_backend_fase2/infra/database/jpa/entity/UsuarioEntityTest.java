package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;

@DisplayName("Testes de Entidade JPA: UsuarioEntity")
class UsuarioEntityTest {

    @Test
    @DisplayName("Deve inicializar dataCriacao e ativo automaticamente no prePersist")
    void devePreencherCamposNoPrePersist() {
        var tipoUsuario = TipoUsuarioEntity.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).build();
        var entity = UsuarioEntity.builder()
                .id(UUID.randomUUID())
                .nome("João Dono")
                .email("joao@restaurante.com")
                .tipoUsuario(tipoUsuario)
                .build();

        entity.prePersist();

        assertThat(entity.getDataCriacao()).isNotNull();
        assertThat(entity.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve atualizar dataAlteracao no preUpdate")
    void deveAtualizarDataAlteracaoNoPreUpdate() {
        var entity = UsuarioEntity.builder()
                .nome("Maria")
                .build();

        entity.preUpdate();

        assertThat(entity.getDataAlteracao()).isNotNull();
    }
}
