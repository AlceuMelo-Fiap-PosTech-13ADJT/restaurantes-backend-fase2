package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarUsuarioUsecaseImpl implements AtualizarUsuarioUsecase {

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final RestauranteGateway restauranteGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public UsuarioOutputDto executar(AtualizarUsuarioInputDto inputDto) {
        if (inputDto == null || inputDto.getId() == null) {
            throw new RegraNegocioException("ID do usuário é obrigatório.");
        }
        if (inputDto.getNome() == null || inputDto.getNome().trim().isBlank()) {
            throw new RegraNegocioException("Nome do usuário é obrigatório.");
        }
        if (inputDto.getEmail() == null || inputDto.getEmail().trim().isBlank()) {
            throw new RegraNegocioException("E-mail do usuário é obrigatório.");
        }

        Usuario usuarioExistente = usuarioGateway.buscarAtivoPorId(inputDto.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));
        usuarioExistente.validarAtivo();

        if (inputDto.getUsuarioOperadorId() != null && !inputDto.getUsuarioOperadorId().equals(inputDto.getId())) {
            UUID targetRestauranteId = inputDto.getRestauranteId() != null
                    ? inputDto.getRestauranteId()
                    : usuarioExistente.getRestauranteId();
            validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                    .usuarioOperadorId(inputDto.getUsuarioOperadorId())
                    .restauranteId(targetRestauranteId)
                    .requerDono(true)
                    .build());
        }

        String novoEmail = inputDto.getEmail().trim().toLowerCase();
        if (!usuarioExistente.getEmail().equalsIgnoreCase(novoEmail) && usuarioGateway.existePorEmail(novoEmail)) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail.");
        }

        TipoUsuario novoTipo = usuarioExistente.getTipoUsuario();
        if (inputDto.getTipoUsuarioId() != null) {
            novoTipo = tipoUsuarioGateway.buscarAtivoPorId(inputDto.getTipoUsuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado."));
            novoTipo.validarAtivo();
        }

        UUID targetRestauranteId = inputDto.getRestauranteId() != null
                ? inputDto.getRestauranteId()
                : usuarioExistente.getRestauranteId();

        if (inputDto.getRestauranteId() != null) {
            restauranteGateway.buscarAtivoPorId(inputDto.getRestauranteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));
        }

        usuarioExistente.atualizar(inputDto.getNome().trim(), novoEmail, novoTipo);
        Usuario atualizado = usuarioGateway.atualizar(targetRestauranteId, usuarioExistente);

        return UsuarioOutputDto.fromDomain(atualizado);
    }
}
