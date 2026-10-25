package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AssociarTipoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssociarTipoUsuarioUsecaseImpl implements AssociarTipoUsuarioUsecase {

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;

    @Override
    public UsuarioOutputDto executar(AssociarTipoUsuarioInputDto inputDto) {
        if (inputDto == null || inputDto.getUsuarioId() == null) {
            throw new RegraNegocioException("ID do usuário é obrigatório.");
        }
        if (inputDto.getTipoUsuarioId() == null) {
            throw new RegraNegocioException("ID do tipo de usuário é obrigatório.");
        }

        Usuario usuario = usuarioGateway.buscarAtivoPorId(inputDto.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        usuario.validarAtivo();

        TipoUsuario tipoUsuario = tipoUsuarioGateway.buscarAtivoPorId(inputDto.getTipoUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado."));

        tipoUsuario.validarAtivo();

        usuario.associarTipoUsuario(tipoUsuario);
        Usuario usuarioAtualizado = usuarioGateway.atualizar(inputDto.getRestauranteId(), usuario);

        return UsuarioOutputDto.fromDomain(usuarioAtualizado);
    }
}
