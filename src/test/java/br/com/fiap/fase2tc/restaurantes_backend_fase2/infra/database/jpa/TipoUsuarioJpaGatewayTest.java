package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.TipoUsuarioMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.TipoUsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários: TipoUsuarioJpaGateway")
class TipoUsuarioJpaGatewayTest {

    @Mock
    private TipoUsuarioRepository repository;

    @Mock
    private TipoUsuarioMapper mapper;

    @InjectMocks
    private TipoUsuarioJpaGateway gateway;

    @Test
    @DisplayName("Deve salvar tipo de usuário com sucesso")
    void deveSalvarComSucesso() {
        TipoUsuario domain = TipoUsuario.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();
        TipoUsuarioEntity salvo = TipoUsuarioEntity.builder().id(UUID.randomUUID()).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();
        TipoUsuario domainSalvo = TipoUsuario.builder().id(salvo.getId()).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(salvo);
        when(mapper.toDomain(salvo)).thenReturn(domainSalvo);

        TipoUsuario resultado = gateway.salvar(domain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(salvo.getId());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve atualizar tipo de usuário com sucesso")
    void deveAtualizarComSucesso() {
        UUID id = UUID.randomUUID();
        TipoUsuario domain = TipoUsuario.builder().id(id).tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build();
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().id(id).tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        TipoUsuario resultado = gateway.atualizar(domain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void deveBuscarPorId() {
        UUID id = UUID.randomUUID();
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().id(id).tipo(TipoUsuarioEnum.CLIENTE).build();
        TipoUsuario domain = TipoUsuario.builder().id(id).tipo(TipoUsuarioEnum.CLIENTE).build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<TipoUsuario> resultado = gateway.buscarPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve buscar ativo por ID com sucesso")
    void deveBuscarAtivoPorId() {
        UUID id = UUID.randomUUID();
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().id(id).tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build();
        TipoUsuario domain = TipoUsuario.builder().id(id).tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build();

        when(repository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<TipoUsuario> resultado = gateway.buscarAtivoPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve buscar por enum tipo com sucesso")
    void deveBuscarPorTipo() {
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();
        TipoUsuario domain = TipoUsuario.builder().tipo(TipoUsuarioEnum.DONO_RESTAURANTE).ativo(true).build();

        when(repository.findByTipoAndAtivoTrue(TipoUsuarioEnum.DONO_RESTAURANTE)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<TipoUsuario> resultado = gateway.buscarPorTipo(TipoUsuarioEnum.DONO_RESTAURANTE);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
    }

    @Test
    @DisplayName("Deve buscar por nome com sucesso")
    void deveBuscarPorNome() {
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().nome("Cliente").ativo(true).build();
        TipoUsuario domain = TipoUsuario.builder().tipo(TipoUsuarioEnum.CLIENTE).ativo(true).build();

        when(repository.findByNomeIgnoreCaseAndAtivoTrue("Cliente")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<TipoUsuario> resultado = gateway.buscarPorNome("Cliente");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
    }

    @Test
    @DisplayName("Deve listar todos com sucesso")
    void deveListarTodos() {
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().build();
        TipoUsuario domain = TipoUsuario.builder().build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomainList(any())).thenReturn(List.of(domain));

        List<TipoUsuario> resultado = gateway.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("Deve listar ativos com sucesso")
    void deveListarAtivos() {
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().ativo(true).build();
        TipoUsuario domain = TipoUsuario.builder().ativo(true).build();

        when(repository.findByAtivoTrue()).thenReturn(List.of(entity));
        when(mapper.toDomainList(any())).thenReturn(List.of(domain));

        List<TipoUsuario> resultado = gateway.listarAtivos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("Deve verificar existência por ID, tipo e nome")
    void deveVerificarExistencia() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        when(repository.existsByTipo(TipoUsuarioEnum.CLIENTE)).thenReturn(true);
        when(repository.existsByNomeIgnoreCase("Cliente")).thenReturn(true);

        assertThat(gateway.existePorId(id)).isTrue();
        assertThat(gateway.existePorTipo(TipoUsuarioEnum.CLIENTE)).isTrue();
        assertThat(gateway.existePorNome("Cliente")).isTrue();
    }

    @Test
    @DisplayName("Deve desativar tipo de usuário com sucesso")
    void deveDesativar() {
        UUID id = UUID.randomUUID();
        TipoUsuarioEntity entity = TipoUsuarioEntity.builder().id(id).ativo(true).build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        gateway.desativar(id);

        assertThat(entity.getAtivo()).isFalse();
        verify(repository).save(entity);
    }
}
