package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;

public interface TipoUsuarioGateway {

    TipoUsuario salvar(TipoUsuario tipoUsuario);

    TipoUsuario atualizar(TipoUsuario tipoUsuario);

    Optional<TipoUsuario> buscarPorId(UUID id);

    Optional<TipoUsuario> buscarAtivoPorId(UUID id);

    Optional<TipoUsuario> buscarPorTipo(TipoUsuarioEnum tipo);

    Optional<TipoUsuario> buscarPorNome(String nome);

    List<TipoUsuario> listarTodos();

    List<TipoUsuario> listarAtivos();

    boolean existePorId(UUID id);

    boolean existePorTipo(TipoUsuarioEnum tipo);

    boolean existePorNome(String nome);

    void desativar(UUID id);

}
