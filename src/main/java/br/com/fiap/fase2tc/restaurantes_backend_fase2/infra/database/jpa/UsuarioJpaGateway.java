package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.UsuarioMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.TipoUsuarioRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioJpaGateway implements UsuarioGateway {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public Usuario salvar(UUID restauranteId, Usuario usuario) {
        UsuarioEntity entity = usuarioMapper.toEntity(restauranteId, usuario);
        vincularTipoUsuarioSeExistente(entity, usuario);
        UsuarioEntity salvo = usuarioRepository.save(entity);
        return usuarioMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public Usuario atualizar(UUID restauranteId, Usuario usuario) {
        UsuarioEntity entity = usuarioMapper.toEntity(restauranteId, usuario);
        vincularTipoUsuarioSeExistente(entity, usuario);
        UsuarioEntity atualizado = usuarioRepository.save(entity);
        return usuarioMapper.toDomain(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarAtivoPorId(UUID id) {
        return usuarioRepository.findByIdAndAtivoTrue(id).map(usuarioMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorIdERestaurante(UUID restauranteId, UUID usuarioId) {
        return usuarioRepository.findByIdAndRestauranteIdAndAtivoTrue(usuarioId, restauranteId)
                .or(() -> usuarioRepository.findByIdAndRestauranteId(usuarioId, restauranteId))
                .map(usuarioMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarDonoPorRestaurante(UUID restauranteId) {
        return usuarioRepository.findFirstByRestauranteIdAndTipoUsuario_TipoAndAtivoTrue(
                restauranteId, TipoUsuarioEnum.DONO_RESTAURANTE)
                .map(usuarioMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCaseAndAtivoTrue(email)
                .or(() -> usuarioRepository.findByEmailIgnoreCase(email))
                .map(usuarioMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmailERestaurante(UUID restauranteId, String email) {
        return usuarioRepository.findByEmailIgnoreCaseAndRestauranteIdAndAtivoTrue(email, restauranteId)
                .map(usuarioMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorRestaurante(UUID restauranteId) {
        return usuarioMapper.toDomainList(usuarioRepository.findByRestauranteId(restauranteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarAtivosPorRestaurante(UUID restauranteId) {
        return usuarioMapper.toDomainList(usuarioRepository.findByRestauranteIdAndAtivoTrue(restauranteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarClientesPorRestaurante(UUID restauranteId) {
        return usuarioMapper.toDomainList(
                usuarioRepository.findByRestauranteIdAndTipoUsuario_Tipo(restauranteId, TipoUsuarioEnum.CLIENTE));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarClientesAtivosPorRestaurante(UUID restauranteId) {
        return usuarioMapper.toDomainList(
                usuarioRepository.findByRestauranteIdAndTipoUsuario_TipoAndAtivoTrue(restauranteId, TipoUsuarioEnum.CLIENTE));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(UUID id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorIdERestaurante(UUID restauranteId, UUID usuarioId) {
        return usuarioRepository.existsByIdAndRestauranteId(usuarioId, restauranteId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional
    public void desativar(UUID restauranteId, UUID usuarioId) {
        usuarioRepository.findByIdAndRestauranteId(usuarioId, restauranteId).ifPresent(entity -> {
            entity.setAtivo(false);
            usuarioRepository.save(entity);
        });
    }

    private void vincularTipoUsuarioSeExistente(UsuarioEntity entity, Usuario domain) {
        if (domain != null && domain.getTipoUsuario() != null) {
            if (domain.getTipoUsuario().getId() != null) {
                tipoUsuarioRepository.findById(domain.getTipoUsuario().getId())
                        .ifPresent(entity::setTipoUsuario);
            } else if (domain.getTipoUsuario().getTipo() != null) {
                tipoUsuarioRepository.findByTipo(domain.getTipoUsuario().getTipo())
                        .ifPresent(entity::setTipoUsuario);
            }
        }
    }
}
