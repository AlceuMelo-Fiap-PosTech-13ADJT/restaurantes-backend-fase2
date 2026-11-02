package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.TipoUsuarioMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TipoUsuarioJpaGateway implements TipoUsuarioGateway {

    private final TipoUsuarioRepository repository;
    private final TipoUsuarioMapper mapper;

    @Override
    @Transactional
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = mapper.toEntity(tipoUsuario);
        TipoUsuarioEntity salvo = repository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public TipoUsuario atualizar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = mapper.toEntity(tipoUsuario);
        TipoUsuarioEntity atualizado = repository.save(entity);
        return mapper.toDomain(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoUsuario> buscarPorId(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoUsuario> buscarAtivoPorId(UUID id) {
        return repository.findByIdAndAtivoTrue(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoUsuario> buscarPorTipo(TipoUsuarioEnum tipo) {
        return repository.findByTipoAndAtivoTrue(tipo)
                .or(() -> repository.findByTipo(tipo))
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoUsuario> buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCaseAndAtivoTrue(nome)
                .or(() -> repository.findByNomeIgnoreCase(nome))
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoUsuario> listarTodos() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoUsuario> listarAtivos() {
        return mapper.toDomainList(repository.findByAtivoTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(UUID id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorTipo(TipoUsuarioEnum tipo) {
        return repository.existsByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }

    @Override
    @Transactional
    public void desativar(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setAtivo(false);
            repository.save(entity);
        });
    }
}
