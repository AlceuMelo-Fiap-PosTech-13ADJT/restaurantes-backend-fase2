package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;

public interface UsuarioGateway {

    Usuario salvar(UUID restauranteId, Usuario usuario);

    Usuario atualizar(UUID restauranteId, Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarAtivoPorId(UUID id);

    Optional<Usuario> buscarPorIdERestaurante(UUID restauranteId, UUID usuarioId);

    Optional<Usuario> buscarDonoPorRestaurante(UUID restauranteId);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorEmailERestaurante(UUID restauranteId, String email);

    List<Usuario> listarPorRestaurante(UUID restauranteId);

    List<Usuario> listarAtivosPorRestaurante(UUID restauranteId);

    List<Usuario> listarClientesPorRestaurante(UUID restauranteId);

    List<Usuario> listarClientesAtivosPorRestaurante(UUID restauranteId);

    boolean existePorId(UUID id);

    boolean existePorIdERestaurante(UUID restauranteId, UUID usuarioId);

    boolean existePorEmail(String email);

    void desativar(UUID restauranteId, UUID usuarioId);

}
