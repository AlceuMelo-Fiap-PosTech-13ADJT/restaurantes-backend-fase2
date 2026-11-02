package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;

class TipoUsuarioMapperTest {

    private TipoUsuarioMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TipoUsuarioMapper();
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Domínio (toDomain)")
    class ToDomainTests {

        @Test
        @DisplayName("Deve mapear TipoUsuarioEntity completa para TipoUsuario domínio")
        void deveMapearEntityCompletaParaDomain() {
            UUID id = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            TipoUsuarioEntity entity = TipoUsuarioEntity.builder()
                    .id(id)
                    .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                    .nome("Dono de restaurante")
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            TipoUsuario domain = mapper.toDomain(entity);

            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isEqualTo(id);
            assertThat(domain.getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
            assertThat(domain.getNome()).isEqualTo("Dono de restaurante");
            assertThat(domain.getAtivo()).isTrue();
            assertThat(domain.getDataCriacao()).isEqualTo(agora);
            assertThat(domain.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve converter nome para TipoUsuarioEnum caso tipo seja nulo na Entity")
        void deveInferirTipoApartirDoNomeQuandoTipoNulo() {
            UUID id = UUID.randomUUID();

            TipoUsuarioEntity entity = TipoUsuarioEntity.builder()
                    .id(id)
                    .tipo(null)
                    .nome("Cliente")
                    .ativo(true)
                    .build();

            TipoUsuario domain = mapper.toDomain(entity);

            assertThat(domain).isNotNull();
            assertThat(domain.getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
        }

        @Test
        @DisplayName("Deve retornar null quando entity for nula")
        void deveRetornarNullQuandoEntityNula() {
            TipoUsuario domain = mapper.toDomain(null);
            assertThat(domain).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Entity (toEntity)")
    class ToEntityTests {

        @Test
        @DisplayName("Deve mapear TipoUsuario domínio para TipoUsuarioEntity")
        void deveMapearDomainParaEntity() {
            UUID id = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            TipoUsuario domain = TipoUsuario.builder()
                    .id(id)
                    .tipo(TipoUsuarioEnum.CLIENTE)
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            TipoUsuarioEntity entity = mapper.toEntity(domain);

            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
            assertThat(entity.getNome()).isEqualTo("Cliente");
            assertThat(entity.getAtivo()).isTrue();
            assertThat(entity.getDataCriacao()).isEqualTo(agora);
            assertThat(entity.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve retornar null quando domain for nulo")
        void deveRetornarNullQuandoDomainNulo() {
            TipoUsuarioEntity entity = mapper.toEntity(null);
            assertThat(entity).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de mapeamento de lista (toDomainList)")
    class ToDomainListTests {

        @Test
        @DisplayName("Deve mapear lista de entities para lista de domínios")
        void deveMapearListaDeEntities() {
            TipoUsuarioEntity entity1 = TipoUsuarioEntity.builder()
                    .id(UUID.randomUUID())
                    .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                    .nome("Dono de restaurante")
                    .ativo(true)
                    .build();

            TipoUsuarioEntity entity2 = TipoUsuarioEntity.builder()
                    .id(UUID.randomUUID())
                    .tipo(TipoUsuarioEnum.CLIENTE)
                    .nome("Cliente")
                    .ativo(true)
                    .build();

            List<TipoUsuario> lista = mapper.toDomainList(List.of(entity1, entity2));

            assertThat(lista).hasSize(2);
            assertThat(lista.get(0).getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
            assertThat(lista.get(1).getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando lista de entities for nula")
        void deveRetornarListaVaziaQuandoListaNula() {
            List<TipoUsuario> lista = mapper.toDomainList(null);
            assertThat(lista).isEmpty();
        }
    }
}
