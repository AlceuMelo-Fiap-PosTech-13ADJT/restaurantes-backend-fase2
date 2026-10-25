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
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;

class UsuarioMapperTest {

    private TipoUsuarioMapper tipoUsuarioMapper;
    private UsuarioMapper mapper;

    @BeforeEach
    void setUp() {
        tipoUsuarioMapper = new TipoUsuarioMapper();
        mapper = new UsuarioMapper(tipoUsuarioMapper);
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Domínio (toDomain)")
    class ToDomainTests {

        @Test
        @DisplayName("Deve mapear UsuarioEntity completa para Usuario domínio")
        void deveMapearEntityCompletaParaDomain() {
            UUID id = UUID.randomUUID();
            UUID tipoId = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            TipoUsuarioEntity tipoEntity = TipoUsuarioEntity.builder()
                    .id(tipoId)
                    .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                    .nome("Dono de restaurante")
                    .ativo(true)
                    .build();

            UsuarioEntity entity = UsuarioEntity.builder()
                    .id(id)
                    .nome("Carlos Silva")
                    .email("carlos@email.com")
                    .restauranteId(UUID.randomUUID())
                    .tipoUsuario(tipoEntity)
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            Usuario domain = mapper.toDomain(entity);

            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isEqualTo(id);
            assertThat(domain.getNome()).isEqualTo("Carlos Silva");
            assertThat(domain.getEmail()).isEqualTo("carlos@email.com");
            assertThat(domain.getTipoUsuario()).isNotNull();
            assertThat(domain.getTipoUsuario().getId()).isEqualTo(tipoId);
            assertThat(domain.getTipoUsuario().getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
            assertThat(domain.getAtivo()).isTrue();
            assertThat(domain.getDataCriacao()).isEqualTo(agora);
            assertThat(domain.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve mapear UsuarioEntity com tipoUsuario nulo")
        void deveMapearEntityComTipoUsuarioNulo() {
            UsuarioEntity entity = UsuarioEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("Maria")
                    .email("maria@email.com")
                    .tipoUsuario(null)
                    .ativo(true)
                    .build();

            Usuario domain = mapper.toDomain(entity);

            assertThat(domain).isNotNull();
            assertThat(domain.getTipoUsuario()).isNull();
        }

        @Test
        @DisplayName("Deve retornar null quando entity for nula")
        void deveRetornarNullQuandoEntityNula() {
            Usuario domain = mapper.toDomain(null);
            assertThat(domain).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Entity (toEntity)")
    class ToEntityTests {

        @Test
        @DisplayName("Deve mapear Usuario domínio para UsuarioEntity com restauranteId")
        void deveMapearDomainParaEntityComRestauranteId() {
            UUID id = UUID.randomUUID();
            UUID restauranteId = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            TipoUsuario tipoDomain = TipoUsuario.builder()
                    .id(UUID.randomUUID())
                    .tipo(TipoUsuarioEnum.CLIENTE)
                    .ativo(true)
                    .build();

            Usuario domain = Usuario.builder()
                    .id(id)
                    .nome("João Santos")
                    .email("joao@email.com")
                    .tipoUsuario(tipoDomain)
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            UsuarioEntity entity = mapper.toEntity(restauranteId, domain);

            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getNome()).isEqualTo("João Santos");
            assertThat(entity.getEmail()).isEqualTo("joao@email.com");
            assertThat(entity.getRestauranteId()).isEqualTo(restauranteId);
            assertThat(entity.getTipoUsuario()).isNotNull();
            assertThat(entity.getTipoUsuario().getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
            assertThat(entity.getAtivo()).isTrue();
            assertThat(entity.getDataCriacao()).isEqualTo(agora);
            assertThat(entity.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve mapear Usuario domínio para UsuarioEntity sem restauranteId especificado")
        void deveMapearDomainParaEntitySemRestauranteId() {
            Usuario domain = Usuario.builder()
                    .id(UUID.randomUUID())
                    .nome("Ana")
                    .email("ana@email.com")
                    .ativo(true)
                    .build();

            UsuarioEntity entity = mapper.toEntity(domain);

            assertThat(entity).isNotNull();
            assertThat(entity.getRestauranteId()).isNull();
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
            UsuarioEntity entity1 = UsuarioEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("User 1")
                    .email("user1@email.com")
                    .ativo(true)
                    .build();

            UsuarioEntity entity2 = UsuarioEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("User 2")
                    .email("user2@email.com")
                    .ativo(true)
                    .build();

            List<Usuario> lista = mapper.toDomainList(List.of(entity1, entity2));

            assertThat(lista).hasSize(2);
            assertThat(lista.get(0).getNome()).isEqualTo("User 1");
            assertThat(lista.get(1).getNome()).isEqualTo("User 2");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando lista de entities for nula")
        void deveRetornarListaVaziaQuandoListaNula() {
            List<Usuario> lista = mapper.toDomainList(null);
            assertThat(lista).isEmpty();
        }
    }
}
