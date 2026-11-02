package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.ItemCardapio;

public interface ItemCardapioGateway {

    ItemCardapio salvar(UUID restauranteId, ItemCardapio itemCardapio);

    ItemCardapio atualizar(UUID restauranteId, ItemCardapio itemCardapio);

    Optional<ItemCardapio> buscarPorId(UUID restauranteId, UUID id);

    Optional<ItemCardapio> buscarAtivoPorId(UUID restauranteId, UUID id);

    List<ItemCardapio> listarPorRestaurante(UUID restauranteId);

    List<ItemCardapio> listarAtivosPorRestaurante(UUID restauranteId);

    List<ItemCardapio> buscarPorNomeERestaurante(UUID restauranteId, String nome);

    boolean existePorIdERestaurante(UUID restauranteId, UUID id);

    void desativar(UUID restauranteId, UUID id);

}
