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

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.UsuarioMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.TipoUsuarioRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários: UsuarioJpaGateway")
class UsuarioJpaGatewayTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioJpaGateway gateway;

    @Test
    @DisplayName("Deve salvar usuário com sucesso vinculando TipoUsuario existente")
    void deveSalvarComSucesso() {
        UUID restauranteId = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();

        TipoUsuario tipoDomain = TipoUsuario.builder().id(tipoId).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).build();
        Usuario domain = Usuario.builder().nome("Carlos").email("carlos@email.com").tipoUsuario(tipoDomain).build();

        TipoUsuarioEntity tipoEntity = TipoUsuarioEntity.builder().id(tipoId).tipo(TipoUsuarioEnum.DONO_RESTAURANTE).build();
        UsuarioEntity entity = UsuarioEntity.builder().nome("Carlos").email("carlos@email.com").build();
        UsuarioEntity salvo = UsuarioEntity.builder().id(UUID.randomUUID()).nome("Carlos").email("carlos@email.com").build();
        Usuario domainSalvo = Usuario.builder().id(salvo.getId()).nome("Carlos").build();

        when(usuarioMapper.toEntity(restauranteId, domain)).thenReturn(entity);
        when(tipoUsuarioRepository.findById(tipoId)).thenReturn(Optional.of(tipoEntity));
        when(usuarioRepository.save(entity)).thenReturn(salvo);
        when(usuarioMapper.toDomain(salvo)).thenReturn(domainSalvo);

        Usuario resultado = gateway.salvar(restauranteId, domain);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(salvo.getId());
        verify(usuarioRepository).save(entity);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarComSucesso() {
        UUID restauranteId = UUID.randomUUID();
        Usuario domain = Usuario.builder().id(UUID.randomUUID()).nome("Ana").build();
        UsuarioEntity entity = UsuarioEntity.builder().id(domain.getId()).nome("Ana").build();

        when(usuarioMapper.toEntity(restauranteId, domain)).thenReturn(entity);
        when(usuarioRepository.save(entity)).thenReturn(entity);
        when(usuarioMapper.toDomain(entity)).thenReturn(domain);

        Usuario resultado = gateway.atualizar(restauranteId, domain);

        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(entity);
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void deveBuscarPorId() {
        UUID id = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().id(id).build();
        Usuario domain = Usuario.builder().id(id).build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(entity));
        when(usuarioMapper.toDomain(entity)).thenReturn(domain);

        Optional<Usuario> resultado = gateway.buscarPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve buscar ativo por ID com sucesso")
    void deveBuscarAtivoPorId() {
        UUID id = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().id(id).ativo(true).build();
        Usuario domain = Usuario.builder().id(id).ativo(true).build();

        when(usuarioRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(entity));
        when(usuarioMapper.toDomain(entity)).thenReturn(domain);

        Optional<Usuario> resultado = gateway.buscarAtivoPorId(id);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve buscar por ID e restaurante")
    void deveBuscarPorIdERestaurante() {
        UUID id = UUID.randomUUID();
        UUID restId = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().id(id).restauranteId(restId).build();
        Usuario domain = Usuario.builder().id(id).build();

        when(usuarioRepository.findByIdAndRestauranteIdAndAtivoTrue(id, restId)).thenReturn(Optional.of(entity));
        when(usuarioMapper.toDomain(entity)).thenReturn(domain);

        Optional<Usuario> resultado = gateway.buscarPorIdERestaurante(restId, id);

        assertThat(resultado).isPresent();
    }

    @Test
    @DisplayName("Deve buscar dono por restaurante")
    void deveBuscarDonoPorRestaurante() {
        UUID restId = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().restauranteId(restId).build();
        Usuario domain = Usuario.builder().build();

        when(usuarioRepository.findFirstByRestauranteIdAndTipoUsuario_TipoAndAtivoTrue(restId, TipoUsuarioEnum.DONO_RESTAURANTE))
                .thenReturn(Optional.of(entity));
        when(usuarioMapper.toDomain(entity)).thenReturn(domain);

        Optional<Usuario> resultado = gateway.buscarDonoPorRestaurante(restId);

        assertThat(resultado).isPresent();
    }

    @Test
    @DisplayName("Deve buscar por email e por email e restaurante")
    void deveBuscarPorEmail() {
        UUID restId = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().email("ana@email.com").build();
        Usuario domain = Usuario.builder().email("ana@email.com").build();

        when(usuarioRepository.findByEmailIgnoreCaseAndAtivoTrue("ana@email.com")).thenReturn(Optional.of(entity));
        when(usuarioRepository.findByEmailIgnoreCaseAndRestauranteIdAndAtivoTrue("ana@email.com", restId)).thenReturn(Optional.of(entity));
        when(usuarioMapper.toDomain(entity)).thenReturn(domain);

        assertThat(gateway.buscarPorEmail("ana@email.com")).isPresent();
        assertThat(gateway.buscarPorEmailERestaurante(restId, "ana@email.com")).isPresent();
    }

    @Test
    @DisplayName("Deve listar por restaurante e listar clientes ativos")
    void deveListarUsuarios() {
        UUID restId = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().build();
        Usuario domain = Usuario.builder().build();

        when(usuarioRepository.findByRestauranteId(restId)).thenReturn(List.of(entity));
        when(usuarioRepository.findByRestauranteIdAndAtivoTrue(restId)).thenReturn(List.of(entity));
        when(usuarioRepository.findByRestauranteIdAndTipoUsuario_Tipo(restId, TipoUsuarioEnum.CLIENTE)).thenReturn(List.of(entity));
        when(usuarioRepository.findByRestauranteIdAndTipoUsuario_TipoAndAtivoTrue(restId, TipoUsuarioEnum.CLIENTE)).thenReturn(List.of(entity));
        when(usuarioMapper.toDomainList(any())).thenReturn(List.of(domain));

        assertThat(gateway.listarPorRestaurante(restId)).hasSize(1);
        assertThat(gateway.listarAtivosPorRestaurante(restId)).hasSize(1);
        assertThat(gateway.listarClientesPorRestaurante(restId)).hasSize(1);
        assertThat(gateway.listarClientesAtivosPorRestaurante(restId)).hasSize(1);
    }

    @Test
    @DisplayName("Deve verificar existência por ID, restaurante e email")
    void deveVerificarExistencia() {
        UUID id = UUID.randomUUID();
        UUID restId = UUID.randomUUID();

        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.existsByIdAndRestauranteId(id, restId)).thenReturn(true);
        when(usuarioRepository.existsByEmailIgnoreCase("teste@email.com")).thenReturn(true);

        assertThat(gateway.existePorId(id)).isTrue();
        assertThat(gateway.existePorIdERestaurante(restId, id)).isTrue();
        assertThat(gateway.existePorEmail("teste@email.com")).isTrue();
    }

    @Test
    @DisplayName("Deve desativar usuário com sucesso")
    void deveDesativar() {
        UUID id = UUID.randomUUID();
        UUID restId = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().id(id).restauranteId(restId).ativo(true).build();

        when(usuarioRepository.findByIdAndRestauranteId(id, restId)).thenReturn(Optional.of(entity));

        gateway.desativar(restId, id);

        assertThat(entity.getAtivo()).isFalse();
        verify(usuarioRepository).save(entity);
    }

    @Test
    @DisplayName("Deve desativar usuário diretamente por usuarioId")
    void deveDesativarPorUsuarioId() {
        UUID id = UUID.randomUUID();
        UsuarioEntity entity = UsuarioEntity.builder().id(id).ativo(true).build();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(entity));

        gateway.desativar(id);

        assertThat(entity.getAtivo()).isFalse();
        verify(usuarioRepository).save(entity);
    }
}
