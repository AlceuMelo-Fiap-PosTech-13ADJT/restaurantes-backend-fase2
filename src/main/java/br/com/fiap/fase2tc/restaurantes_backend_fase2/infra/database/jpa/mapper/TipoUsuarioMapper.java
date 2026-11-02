package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;

@Component
public class TipoUsuarioMapper {

    public TipoUsuario toDomain(TipoUsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        TipoUsuarioEnum tipo = entity.getTipo();
        if (tipo == null && entity.getNome() != null && !entity.getNome().isBlank()) {
            tipo = TipoUsuarioEnum.fromDescricao(entity.getNome());
        }

        return TipoUsuario.builder()
                .id(entity.getId())
                .tipo(tipo)
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataAlteracao(entity.getDataAlteracao())
                .build();
    }

    public TipoUsuarioEntity toEntity(TipoUsuario domain) {
        if (domain == null) {
            return null;
        }

        String nome = domain.getNome();
        if (nome == null && domain.getTipo() != null) {
            nome = domain.getTipo().getDescricao();
        }

        return TipoUsuarioEntity.builder()
                .id(domain.getId())
                .tipo(domain.getTipo())
                .nome(nome)
                .ativo(domain.getAtivo())
                .dataCriacao(domain.getDataCriacao())
                .dataAlteracao(domain.getDataAlteracao())
                .build();
    }

    public List<TipoUsuario> toDomainList(List<TipoUsuarioEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
