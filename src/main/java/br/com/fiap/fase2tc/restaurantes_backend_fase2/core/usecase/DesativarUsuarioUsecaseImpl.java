package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DesativarUsuarioUsecaseImpl implements DesativarUsuarioUsecase {

    private final UsuarioGateway usuarioGateway;

    @Override
    public void executar(UUID id) {
        if (id == null) {
            throw new RegraNegocioException("ID do usuário é obrigatório.");
        }

        Usuario usuario = usuarioGateway.buscarAtivoPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        usuario.desativar();
        usuarioGateway.desativar(id);
    }
}
