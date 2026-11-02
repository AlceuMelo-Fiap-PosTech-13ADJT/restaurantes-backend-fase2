package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Restaurante;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.AcessoNaoAutorizadoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidarPermissaoUsuarioUsecaseImpl implements ValidarPermissaoUsuarioUsecase {

    private final UsuarioGateway usuarioGateway;
    private final RestauranteGateway restauranteGateway;

    @Override
    public Usuario executar(ValidarPermissaoUsuarioInputDto inputDto) {
        if (inputDto == null || inputDto.getUsuarioOperadorId() == null) {
            throw new AcessoNaoAutorizadoException("Identificação do usuário operador é obrigatória.");
        }

        Usuario operador = usuarioGateway.buscarAtivoPorId(inputDto.getUsuarioOperadorId())
                .orElseThrow(() -> new AcessoNaoAutorizadoException("Operador não encontrado ou inativo."));

        operador.validarAtivo();

        if (inputDto.isRequerDono()) {
            if (inputDto.getRestauranteId() != null) {
                Restaurante restaurante = restauranteGateway.buscarAtivoPorId(inputDto.getRestauranteId())
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado ou inativo."));

                restaurante.validarDono(inputDto.getUsuarioOperadorId());
            } else if (!operador.isDonoRestaurante()) {
                throw new DonoRestauranteInvalidoException("O usuário operador não possui o perfil de 'Dono de restaurante'.");
            }
        } else if (inputDto.getRestauranteId() != null) {
            restauranteGateway.buscarAtivoPorId(inputDto.getRestauranteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado ou inativo."));
        }

        return operador;
    }
}
