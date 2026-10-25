package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes de Entidade JPA: RestauranteEntity")
class RestauranteEntityTest {

    @Test
    @DisplayName("Deve inicializar dataCriacao e ativo automaticamente no prePersist")
    void devePreencherCamposNoPrePersist() {
        var donoId = UUID.randomUUID();
        var dono = UsuarioEntity.builder().id(donoId).nome("Carlos").build();
        var entity = RestauranteEntity.builder()
                .nome("Cantina")
                .endereco("Rua 1")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("12h às 22h")
                .dono(dono)
                .build();

        entity.prePersist();

        assertThat(entity.getDataCriacao()).isNotNull();
        assertThat(entity.getAtivo()).isTrue();
        assertThat(entity.getDonoId()).isEqualTo(donoId);
    }

    @Test
    @DisplayName("Deve atualizar dataAlteracao no preUpdate")
    void deveAtualizarDataAlteracaoNoPreUpdate() {
        var entity = RestauranteEntity.builder().nome("Restaurante").build();

        entity.preUpdate();

        assertThat(entity.getDataAlteracao()).isNotNull();
    }
}
