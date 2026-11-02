package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;

public interface RestauranteGateway {

    Restaurante salvar(Restaurante restaurante);

    Restaurante atualizar(Restaurante restaurante);

    Optional<Restaurante> buscarPorId(UUID id);

    Optional<Restaurante> buscarAtivoPorId(UUID id);

    List<Restaurante> listarTodos();

    List<Restaurante> listarAtivos();

    List<Restaurante> buscarPorNome(String nome);

    List<Restaurante> buscarPorTipoCozinha(String tipoCozinha);

    List<Restaurante> buscarPorDonoId(UUID donoId);

    List<Restaurante> buscarPorClienteId(UUID clienteId);

    boolean existePorId(UUID id);

    boolean existePorDonoId(UUID donoId);

    void desativar(UUID id);

}
