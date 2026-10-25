package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.ItemCardapioEntity;

@Repository
public interface ItemCardapioRepository extends JpaRepository<ItemCardapioEntity, UUID> {

    Optional<ItemCardapioEntity> findByIdAndRestauranteId(UUID id, UUID restauranteId);

    Optional<ItemCardapioEntity> findByIdAndRestauranteIdAndAtivoTrue(UUID id, UUID restauranteId);

    List<ItemCardapioEntity> findByRestauranteId(UUID restauranteId);

    List<ItemCardapioEntity> findByRestauranteIdAndAtivoTrue(UUID restauranteId);

    List<ItemCardapioEntity> findByRestauranteIdAndNomeContainingIgnoreCaseAndAtivoTrue(UUID restauranteId, String nome);

    boolean existsByIdAndRestauranteId(UUID id, UUID restauranteId);

    boolean existsByIdAndRestauranteIdAndAtivoTrue(UUID id, UUID restauranteId);
}
