package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {

    Optional<UsuarioEntity> findByIdAndAtivoTrue(UUID id);

    Optional<UsuarioEntity> findByIdAndRestauranteId(UUID id, UUID restauranteId);

    Optional<UsuarioEntity> findByIdAndRestauranteIdAndAtivoTrue(UUID id, UUID restauranteId);

    Optional<UsuarioEntity> findByEmailIgnoreCase(String email);

    Optional<UsuarioEntity> findByEmailIgnoreCaseAndAtivoTrue(String email);

    Optional<UsuarioEntity> findByEmailIgnoreCaseAndRestauranteIdAndAtivoTrue(String email, UUID restauranteId);

    Optional<UsuarioEntity> findByRestauranteIdAndTipoUsuario_TipoAndAtivoTrue(UUID restauranteId, TipoUsuarioEnum tipo);

    List<UsuarioEntity> findByRestauranteId(UUID restauranteId);

    List<UsuarioEntity> findByRestauranteIdAndAtivoTrue(UUID restauranteId);

    List<UsuarioEntity> findByRestauranteIdAndTipoUsuario_Tipo(UUID restauranteId, TipoUsuarioEnum tipo);

    List<UsuarioEntity> findByRestauranteIdAndTipoUsuario_TipoAndAtivoTrue(UUID restauranteId, TipoUsuarioEnum tipo);

    boolean existsByIdAndRestauranteId(UUID id, UUID restauranteId);

    boolean existsByEmailIgnoreCase(String email);
}
