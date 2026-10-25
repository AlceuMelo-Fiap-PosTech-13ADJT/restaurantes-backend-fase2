package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.RestauranteEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.RestauranteMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.RestauranteRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários: RestauranteJpaGateway")
class RestauranteJpaGatewayTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RestauranteMapper restauranteMapper;

    @InjectMocks
    private RestauranteJpaGateway gateway;

    @Test
    @DisplayName("Deve salvar restaurante com sucesso vinculando dono existente")
    void deveSalvarComSucesso() {
        UUID donoId = UUID.randomUUID();
        Usuario donoDomain = Usuario.builder().id(donoId).build();
        Restaurante domain = Restaurante.builder().nome("Restaurante A").dono(donoDomain).build();

        UsuarioEntity donoEntity = UsuarioEntity.builder().id(donoId).build();
        RestauranteEntity entity = RestauranteEntity.builder().nome("Restaurante A").build();
        RestauranteEntity salvo = RestauranteEntity.builder().id(UUID.randomUUID()).nome("Restaurante A").build();
        Restaurante domainSalvo = Restaurante.builder().id(salvo.getId()).nome("Restaurante A").build();

        when(restauranteMapper.toEntity(domain)).thenReturn(entity);
        when(usuarioRepository.findById(donoId)).thenReturn(Optional.of(donoEntity));
        when(restauranteRepository.save(entity)).thenReturn(salvo);
        when(restauranteMapper.toDomain(salvo)).thenReturn(domainSalvo);

        Restaurante resultado = gateway.salvar(domain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(salvo.getId());
        verify(restauranteRepository).save(entity);
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void deveAtualizarComSucesso() {
        Restaurante domain = Restaurante.builder().id(UUID.randomUUID()).nome("Restaurante Atualizado").build();
        RestauranteEntity entity = RestauranteEntity.builder().id(domain.getId()).nome("Restaurante Atualizado").build();

        when(restauranteMapper.toEntity(domain)).thenReturn(entity);
        when(restauranteRepository.save(entity)).thenReturn(entity);
        when(restauranteMapper.toDomain(entity)).thenReturn(domain);

        Restaurante resultado = gateway.atualizar(domain);

        assertThat(resultado).isNotNull();
        verify(restauranteRepository).save(entity);
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void deveBuscarPorId() {
        UUID id = UUID.randomUUID();
        RestauranteEntity entity = RestauranteEntity.builder().id(id).build();
        Restaurante domain = Restaurante.builder().id(id).build();

        when(restauranteRepository.findById(id)).thenReturn(Optional.of(entity));
        when(restauranteMapper.toDomain(entity)).thenReturn(domain);

        Optional<Restaurante> resultado = gateway.buscarPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve buscar ativo por ID com sucesso")
    void deveBuscarAtivoPorId() {
        UUID id = UUID.randomUUID();
        RestauranteEntity entity = RestauranteEntity.builder().id(id).ativo(true).build();
        Restaurante domain = Restaurante.builder().id(id).ativo(true).build();

        when(restauranteRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(entity));
        when(restauranteMapper.toDomain(entity)).thenReturn(domain);

        Optional<Restaurante> resultado = gateway.buscarAtivoPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve listar todos e listar ativos com sucesso")
    void deveListarRestaurantes() {
        RestauranteEntity entity = RestauranteEntity.builder().build();
        Restaurante domain = Restaurante.builder().build();

        when(restauranteRepository.findAll()).thenReturn(List.of(entity));
        when(restauranteRepository.findByAtivoTrue()).thenReturn(List.of(entity));
        when(restauranteMapper.toDomainList(any())).thenReturn(List.of(domain));

        assertThat(gateway.listarTodos()).hasSize(1);
        assertThat(gateway.listarAtivos()).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar por nome, tipo de cozinha e donoId")
    void deveBuscarPorFiltros() {
        UUID donoId = UUID.randomUUID();
        RestauranteEntity entity = RestauranteEntity.builder().build();
        Restaurante domain = Restaurante.builder().build();

        when(restauranteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue("Pizza")).thenReturn(List.of(entity));
        when(restauranteRepository.findByTipoCozinhaIgnoreCaseAndAtivoTrue("Italiana")).thenReturn(List.of(entity));
        when(restauranteRepository.findByDono_IdAndAtivoTrue(donoId)).thenReturn(List.of(entity));
        when(restauranteMapper.toDomainList(any())).thenReturn(List.of(domain));

        assertThat(gateway.buscarPorNome("Pizza")).hasSize(1);
        assertThat(gateway.buscarPorTipoCozinha("Italiana")).hasSize(1);
        assertThat(gateway.buscarPorDonoId(donoId)).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar restaurante por clienteId com sucesso")
    void deveBuscarPorClienteId() {
        UUID clienteId = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();

        UsuarioEntity cliente = UsuarioEntity.builder().id(clienteId).restauranteId(restauranteId).ativo(true).build();
        RestauranteEntity restauranteEntity = RestauranteEntity.builder().id(restauranteId).ativo(true).build();
        Restaurante restauranteDomain = Restaurante.builder().id(restauranteId).build();

        when(usuarioRepository.findByIdAndAtivoTrue(clienteId)).thenReturn(Optional.of(cliente));
        when(restauranteRepository.findByIdAndAtivoTrue(restauranteId)).thenReturn(Optional.of(restauranteEntity));
        when(restauranteMapper.toDomain(restauranteEntity)).thenReturn(restauranteDomain);

        List<Restaurante> resultado = gateway.buscarPorClienteId(clienteId);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(restauranteId);
    }

    @Test
    @DisplayName("Deve verificar existência por ID e por donoId")
    void deveVerificarExistencia() {
        UUID id = UUID.randomUUID();
        UUID donoId = UUID.randomUUID();

        when(restauranteRepository.existsById(id)).thenReturn(true);
        when(restauranteRepository.existsByDono_IdAndAtivoTrue(donoId)).thenReturn(true);

        assertThat(gateway.existePorId(id)).isTrue();
        assertThat(gateway.existePorDonoId(donoId)).isTrue();
    }

    @Test
    @DisplayName("Deve desativar restaurante com sucesso")
    void deveDesativar() {
        UUID id = UUID.randomUUID();
        RestauranteEntity entity = RestauranteEntity.builder().id(id).ativo(true).build();

        when(restauranteRepository.findById(id)).thenReturn(Optional.of(entity));

        gateway.desativar(id);

        assertThat(entity.getAtivo()).isFalse();
        verify(restauranteRepository).save(entity);
    }
}
