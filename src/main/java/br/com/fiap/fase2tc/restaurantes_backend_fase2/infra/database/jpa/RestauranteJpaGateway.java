package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.RestauranteEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.RestauranteMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.RestauranteRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestauranteJpaGateway implements RestauranteGateway {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestauranteMapper restauranteMapper;

    @Override
    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        RestauranteEntity entity = restauranteMapper.toEntity(restaurante);
        vincularDonoSeExistente(entity, restaurante);
        RestauranteEntity salvo = restauranteRepository.save(entity);
        return restauranteMapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public Restaurante atualizar(Restaurante restaurante) {
        RestauranteEntity entity = restauranteMapper.toEntity(restaurante);
        vincularDonoSeExistente(entity, restaurante);
        RestauranteEntity atualizado = restauranteRepository.save(entity);
        return restauranteMapper.toDomain(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarPorId(UUID id) {
        return restauranteRepository.findById(id).map(restauranteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Restaurante> buscarAtivoPorId(UUID id) {
        return restauranteRepository.findByIdAndAtivoTrue(id).map(restauranteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> listarTodos() {
        return restauranteMapper.toDomainList(restauranteRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteMapper.toDomainList(restauranteRepository.findByAtivoTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorNome(String nome) {
        return restauranteMapper.toDomainList(
                restauranteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorTipoCozinha(String tipoCozinha) {
        return restauranteMapper.toDomainList(
                restauranteRepository.findByTipoCozinhaIgnoreCaseAndAtivoTrue(tipoCozinha));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorDonoId(UUID donoId) {
        return restauranteMapper.toDomainList(
                restauranteRepository.findByDono_IdAndAtivoTrue(donoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorClienteId(UUID clienteId) {
        Optional<UsuarioEntity> clienteOpt = usuarioRepository.findByIdAndAtivoTrue(clienteId);
        if (clienteOpt.isPresent() && clienteOpt.get().getRestauranteId() != null) {
            return restauranteRepository.findByIdAndAtivoTrue(clienteOpt.get().getRestauranteId())
                    .map(restauranteMapper::toDomain)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(UUID id) {
        return restauranteRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDonoId(UUID donoId) {
        return restauranteRepository.existsByDono_IdAndAtivoTrue(donoId);
    }

    @Override
    @Transactional
    public void desativar(UUID id) {
        restauranteRepository.findById(id).ifPresent(entity -> {
            entity.setAtivo(false);
            restauranteRepository.save(entity);
        });
    }

    private void vincularDonoSeExistente(RestauranteEntity entity, Restaurante domain) {
        if (domain != null && domain.getDono() != null && domain.getDono().getId() != null) {
            usuarioRepository.findById(domain.getDono().getId())
                    .ifPresent(entity::setDono);
        }
    }
}
