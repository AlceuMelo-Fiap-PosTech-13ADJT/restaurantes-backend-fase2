package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.ItemCardapioEntity;

@Component
public class ItemCardapioMapper {

    public ItemCardapio toDomain(ItemCardapioEntity entity) {
        if (entity == null) {
            return null;
        }

        return ItemCardapio.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .preco(entity.getPreco())
                .disponivelApenasRestaurante(entity.getDisponivelApenasRestaurante())
                .fotoPath(entity.getFotoPath())
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataAlteracao(entity.getDataAlteracao())
                .build();
    }

    public ItemCardapioEntity toEntity(ItemCardapio domain) {
        return toEntity(null, domain);
    }

    public ItemCardapioEntity toEntity(UUID restauranteId, ItemCardapio domain) {
        if (domain == null) {
            return null;
        }

        return ItemCardapioEntity.builder()
                .id(domain.getId())
                .restauranteId(restauranteId)
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .preco(domain.getPreco())
                .disponivelApenasRestaurante(domain.getDisponivelApenasRestaurante())
                .fotoPath(domain.getFotoPath())
                .ativo(domain.getAtivo())
                .dataCriacao(domain.getDataCriacao())
                .dataAlteracao(domain.getDataAlteracao())
                .build();
    }

    public List<ItemCardapio> toDomainList(List<ItemCardapioEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
