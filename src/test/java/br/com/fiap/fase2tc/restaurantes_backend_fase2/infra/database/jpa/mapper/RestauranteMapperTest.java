package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.RestauranteEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;

class RestauranteMapperTest {

    private TipoUsuarioMapper tipoUsuarioMapper;
    private UsuarioMapper usuarioMapper;
    private RestauranteMapper mapper;

    @BeforeEach
    void setUp() {
        tipoUsuarioMapper = new TipoUsuarioMapper();
        usuarioMapper = new UsuarioMapper(tipoUsuarioMapper);
        mapper = new RestauranteMapper(usuarioMapper);
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Domínio (toDomain)")
    class ToDomainTests {

        @Test
        @DisplayName("Deve mapear RestauranteEntity completa para Restaurante domínio")
        void deveMapearEntityCompletaParaDomain() {
            UUID id = UUID.randomUUID();
            UUID donoId = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            TipoUsuarioEntity tipoEntity = TipoUsuarioEntity.builder()
                    .id(UUID.randomUUID())
                    .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                    .nome("Dono de restaurante")
                    .ativo(true)
                    .build();

            UsuarioEntity donoEntity = UsuarioEntity.builder()
                    .id(donoId)
                    .nome("Dono Teste")
                    .email("dono@teste.com")
                    .tipoUsuario(tipoEntity)
                    .ativo(true)
                    .build();

            RestauranteEntity entity = RestauranteEntity.builder()
                    .id(id)
                    .nome("Pizzaria Bella")
                    .endereco("Rua das Flores, 123")
                    .tipoCozinha("Italiana")
                    .horarioFuncionamento("18:00 - 23:00")
                    .dono(donoEntity)
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            Restaurante domain = mapper.toDomain(entity);

            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isEqualTo(id);
            assertThat(domain.getNome()).isEqualTo("Pizzaria Bella");
            assertThat(domain.getEndereco()).isEqualTo("Rua das Flores, 123");
            assertThat(domain.getTipoCozinha()).isEqualTo("Italiana");
            assertThat(domain.getHorarioFuncionamento()).isEqualTo("18:00 - 23:00");
            assertThat(domain.getDono()).isNotNull();
            assertThat(domain.getDono().getId()).isEqualTo(donoId);
            assertThat(domain.getDono().isDonoRestaurante()).isTrue();
            assertThat(domain.getAtivo()).isTrue();
            assertThat(domain.getDataCriacao()).isEqualTo(agora);
            assertThat(domain.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve mapear RestauranteEntity com dono nulo")
        void deveMapearEntityComDonoNulo() {
            RestauranteEntity entity = RestauranteEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("Restaurante Sem Dono")
                    .endereco("Av Central, 50")
                    .tipoCozinha("Variada")
                    .horarioFuncionamento("10:00 - 20:00")
                    .dono(null)
                    .ativo(true)
                    .build();

            Restaurante domain = mapper.toDomain(entity);

            assertThat(domain).isNotNull();
            assertThat(domain.getDono()).isNull();
        }

        @Test
        @DisplayName("Deve retornar null quando entity for nula")
        void deveRetornarNullQuandoEntityNula() {
            Restaurante domain = mapper.toDomain(null);
            assertThat(domain).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de mapeamento para Entity (toEntity)")
    class ToEntityTests {

        @Test
        @DisplayName("Deve mapear Restaurante domínio para RestauranteEntity")
        void deveMapearDomainParaEntity() {
            UUID id = UUID.randomUUID();
            UUID donoId = UUID.randomUUID();
            LocalDateTime agora = LocalDateTime.now();

            TipoUsuario tipoDomain = TipoUsuario.builder()
                    .id(UUID.randomUUID())
                    .tipo(TipoUsuarioEnum.DONO_RESTAURANTE)
                    .ativo(true)
                    .build();

            Usuario donoDomain = Usuario.builder()
                    .id(donoId)
                    .nome("Dono Teste")
                    .email("dono@teste.com")
                    .tipoUsuario(tipoDomain)
                    .ativo(true)
                    .build();

            Restaurante domain = Restaurante.builder()
                    .id(id)
                    .nome("Hamburgueria Gourmet")
                    .endereco("Rua das Palmeiras, 45")
                    .tipoCozinha("Americana")
                    .horarioFuncionamento("12:00 - 22:00")
                    .dono(donoDomain)
                    .ativo(true)
                    .dataCriacao(agora)
                    .dataAlteracao(agora)
                    .build();

            RestauranteEntity entity = mapper.toEntity(domain);

            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getNome()).isEqualTo("Hamburgueria Gourmet");
            assertThat(entity.getEndereco()).isEqualTo("Rua das Palmeiras, 45");
            assertThat(entity.getTipoCozinha()).isEqualTo("Americana");
            assertThat(entity.getHorarioFuncionamento()).isEqualTo("12:00 - 22:00");
            assertThat(entity.getDono()).isNotNull();
            assertThat(entity.getDono().getId()).isEqualTo(donoId);
            assertThat(entity.getAtivo()).isTrue();
            assertThat(entity.getDataCriacao()).isEqualTo(agora);
            assertThat(entity.getDataAlteracao()).isEqualTo(agora);
        }

        @Test
        @DisplayName("Deve mapear Restaurante domínio sem dono para RestauranteEntity")
        void deveMapearDomainSemDonoParaEntity() {
            Restaurante domain = Restaurante.builder()
                    .id(UUID.randomUUID())
                    .nome("Restaurante Sem Dono")
                    .endereco("Rua Teste, 1")
                    .tipoCozinha("Mexicana")
                    .horarioFuncionamento("11:00 - 21:00")
                    .dono(null)
                    .ativo(true)
                    .build();

            RestauranteEntity entity = mapper.toEntity(domain);

            assertThat(entity).isNotNull();
            assertThat(entity.getDono()).isNull();
        }

        @Test
        @DisplayName("Deve retornar null quando domain for nulo")
        void deveRetornarNullQuandoDomainNulo() {
            RestauranteEntity entity = mapper.toEntity(null);
            assertThat(entity).isNull();
        }
    }

    @Nested
    @DisplayName("Cenários de mapeamento de lista (toDomainList)")
    class ToDomainListTests {

        @Test
        @DisplayName("Deve mapear lista de entities para lista de domínios")
        void deveMapearListaDeEntities() {
            RestauranteEntity entity1 = RestauranteEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("Restaurante 1")
                    .endereco("End 1")
                    .tipoCozinha("Italiana")
                    .horarioFuncionamento("18:00 - 23:00")
                    .ativo(true)
                    .build();

            RestauranteEntity entity2 = RestauranteEntity.builder()
                    .id(UUID.randomUUID())
                    .nome("Restaurante 2")
                    .endereco("End 2")
                    .tipoCozinha("Japonesa")
                    .horarioFuncionamento("19:00 - 00:00")
                    .ativo(true)
                    .build();

            List<Restaurante> lista = mapper.toDomainList(List.of(entity1, entity2));

            assertThat(lista).hasSize(2);
            assertThat(lista.get(0).getNome()).isEqualTo("Restaurante 1");
            assertThat(lista.get(1).getNome()).isEqualTo("Restaurante 2");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando lista de entities for nula")
        void deveRetornarListaVaziaQuandoListaNula() {
            List<Restaurante> lista = mapper.toDomainList(null);
            assertThat(lista).isEmpty();
        }
    }
}
