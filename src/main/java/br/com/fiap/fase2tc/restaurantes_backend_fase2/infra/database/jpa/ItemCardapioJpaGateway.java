package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.ItemCardapioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.ItemCardapioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper.ItemCardapioMapper;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.ItemCardapioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemCardapioJpaGateway implements ItemCardapioGateway {

    private final ItemCardapioRepository repository;
    private final ItemCardapioMapper mapper;

    @Override
    @Transactional
    public ItemCardapio salvar(UUID restauranteId, ItemCardapio itemCardapio) {
        ItemCardapioEntity entity = mapper.toEntity(restauranteId, itemCardapio);
        ItemCardapioEntity salvo = repository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    @Transactional
    public ItemCardapio atualizar(UUID restauranteId, ItemCardapio itemCardapio) {
        ItemCardapioEntity entity = mapper.toEntity(restauranteId, itemCardapio);
        ItemCardapioEntity atualizado = repository.save(entity);
        return mapper.toDomain(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemCardapio> buscarPorId(UUID restauranteId, UUID id) {
        return repository.findByIdAndRestauranteId(id, restauranteId).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemCardapio> buscarAtivoPorId(UUID restauranteId, UUID id) {
        return repository.findByIdAndRestauranteIdAndAtivoTrue(id, restauranteId).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> listarPorRestaurante(UUID restauranteId) {
        return mapper.toDomainList(repository.findByRestauranteId(restauranteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> listarAtivosPorRestaurante(UUID restauranteId) {
        return mapper.toDomainList(repository.findByRestauranteIdAndAtivoTrue(restauranteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> buscarPorNomeERestaurante(UUID restauranteId, String nome) {
        return mapper.toDomainList(
                repository.findByRestauranteIdAndNomeContainingIgnoreCaseAndAtivoTrue(restauranteId, nome));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorIdERestaurante(UUID restauranteId, UUID id) {
        return repository.existsByIdAndRestauranteIdAndAtivoTrue(id, restauranteId);
    }

    @Override
    @Transactional
    public void desativar(UUID restauranteId, UUID id) {
        repository.findByIdAndRestauranteId(id, restauranteId).ifPresent(entity -> {
            entity.setAtivo(false);
            repository.save(entity);
        });
    }
}
