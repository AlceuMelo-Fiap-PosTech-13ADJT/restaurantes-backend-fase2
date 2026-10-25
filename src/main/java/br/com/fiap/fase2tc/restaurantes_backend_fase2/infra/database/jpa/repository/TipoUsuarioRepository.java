package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, UUID> {

    Optional<TipoUsuarioEntity> findByIdAndAtivoTrue(UUID id);

    Optional<TipoUsuarioEntity> findByTipo(TipoUsuarioEnum tipo);

    Optional<TipoUsuarioEntity> findByTipoAndAtivoTrue(TipoUsuarioEnum tipo);

    Optional<TipoUsuarioEntity> findByNomeIgnoreCase(String nome);

    Optional<TipoUsuarioEntity> findByNomeIgnoreCaseAndAtivoTrue(String nome);

    List<TipoUsuarioEntity> findByAtivoTrue();

    boolean existsByTipo(TipoUsuarioEnum tipo);

    boolean existsByTipoAndAtivoTrue(TipoUsuarioEnum tipo);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndAtivoTrue(String nome);
}
