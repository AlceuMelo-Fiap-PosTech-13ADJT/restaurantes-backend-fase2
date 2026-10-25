package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.mapper;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.RestauranteEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestauranteMapper {

    private final UsuarioMapper usuarioMapper;

    public Restaurante toDomain(RestauranteEntity entity) {
        if (entity == null) {
            return null;
        }

        Usuario dono = null;
        if (entity.getDono() != null) {
            dono = usuarioMapper.toDomain(entity.getDono());
        }

        return Restaurante.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .endereco(entity.getEndereco())
                .tipoCozinha(entity.getTipoCozinha())
                .horarioFuncionamento(entity.getHorarioFuncionamento())
                .dono(dono)
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataAlteracao(entity.getDataAlteracao())
                .build();
    }

    public RestauranteEntity toEntity(Restaurante domain) {
        if (domain == null) {
            return null;
        }

        UsuarioEntity donoEntity = null;
        if (domain.getDono() != null) {
            donoEntity = usuarioMapper.toEntity(domain.getDono());
        }

        return RestauranteEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .endereco(domain.getEndereco())
                .tipoCozinha(domain.getTipoCozinha())
                .horarioFuncionamento(domain.getHorarioFuncionamento())
                .dono(donoEntity)
                .ativo(domain.getAtivo())
                .dataCriacao(domain.getDataCriacao())
                .dataAlteracao(domain.getDataAlteracao())
                .build();
    }

    public List<Restaurante> toDomainList(List<RestauranteEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
