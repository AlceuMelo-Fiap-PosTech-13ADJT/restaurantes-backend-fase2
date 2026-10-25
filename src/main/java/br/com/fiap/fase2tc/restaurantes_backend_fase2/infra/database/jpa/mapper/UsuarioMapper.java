package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final TipoUsuarioMapper tipoUsuarioMapper;

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        TipoUsuario tipoUsuario = null;
        if (entity.getTipoUsuario() != null) {
            tipoUsuario = tipoUsuarioMapper.toDomain(entity.getTipoUsuario());
        }

        return Usuario.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .tipoUsuario(tipoUsuario)
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataAlteracao(entity.getDataAlteracao())
                .build();
    }

    public UsuarioEntity toEntity(Usuario domain) {
        return toEntity(null, domain);
    }

    public UsuarioEntity toEntity(UUID restauranteId, Usuario domain) {
        if (domain == null) {
            return null;
        }

        TipoUsuarioEntity tipoUsuarioEntity = null;
        if (domain.getTipoUsuario() != null) {
            tipoUsuarioEntity = tipoUsuarioMapper.toEntity(domain.getTipoUsuario());
        }

        return UsuarioEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .email(domain.getEmail())
                .restauranteId(restauranteId)
                .tipoUsuario(tipoUsuarioEntity)
                .ativo(domain.getAtivo())
                .dataCriacao(domain.getDataCriacao())
                .dataAlteracao(domain.getDataAlteracao())
                .build();
    }

    public List<Usuario> toDomainList(List<UsuarioEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
