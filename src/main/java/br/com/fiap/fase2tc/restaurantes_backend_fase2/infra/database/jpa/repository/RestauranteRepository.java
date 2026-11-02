package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.RestauranteEntity;

@Repository
public interface RestauranteRepository extends JpaRepository<RestauranteEntity, UUID> {

    Optional<RestauranteEntity> findByIdAndAtivoTrue(UUID id);

    List<RestauranteEntity> findByAtivoTrue();

    List<RestauranteEntity> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<RestauranteEntity> findByTipoCozinhaIgnoreCaseAndAtivoTrue(String tipoCozinha);

    List<RestauranteEntity> findByDono_Id(UUID donoId);

    List<RestauranteEntity> findByDono_IdAndAtivoTrue(UUID donoId);

    boolean existsByDono_Id(UUID donoId);

    boolean existsByDono_IdAndAtivoTrue(UUID donoId);
}
