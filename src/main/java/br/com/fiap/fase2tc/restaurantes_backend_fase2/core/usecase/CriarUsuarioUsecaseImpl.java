package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import java.time.LocalDateTime;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.Usuario;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.CriarUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.UsuarioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ValidarPermissaoUsuarioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.RestauranteGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.TipoUsuarioGateway;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CriarUsuarioUsecaseImpl implements CriarUsuarioUsecase {

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final RestauranteGateway restauranteGateway;
    private final ValidarPermissaoUsuarioUsecase validarPermissaoUsuarioUsecase;

    @Override
    public UsuarioOutputDto executar(CriarUsuarioInputDto inputDto) {
        if (inputDto == null) {
            throw new RegraNegocioException("Dados do usuário são obrigatórios.");
        }

        if (inputDto.getUsuarioOperadorId() != null) {
            validarPermissaoUsuarioUsecase.executar(ValidarPermissaoUsuarioInputDto.builder()
                    .usuarioOperadorId(inputDto.getUsuarioOperadorId())
                    .restauranteId(inputDto.getRestauranteId())
                    .requerDono(true)
                    .build());
        }
        if (inputDto.getNome() == null || inputDto.getNome().trim().isBlank()) {
            throw new RegraNegocioException("Nome do usuário é obrigatório.");
        }
        if (inputDto.getEmail() == null || inputDto.getEmail().trim().isBlank()) {
            throw new RegraNegocioException("E-mail do usuário é obrigatório.");
        }
        if (inputDto.getTipoUsuarioId() == null) {
            throw new RegraNegocioException("Tipo de usuário é obrigatório.");
        }

        String emailNormalizado = inputDto.getEmail().trim().toLowerCase();
        if (usuarioGateway.existePorEmail(emailNormalizado)) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail.");
        }

        TipoUsuario tipoUsuario = tipoUsuarioGateway.buscarAtivoPorId(inputDto.getTipoUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado."));
        tipoUsuario.validarAtivo();

        if (inputDto.getRestauranteId() != null) {
            restauranteGateway.buscarAtivoPorId(inputDto.getRestauranteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado."));
        }

        Usuario usuario = Usuario.builder()
                .nome(inputDto.getNome().trim())
                .email(emailNormalizado)
                .restauranteId(inputDto.getRestauranteId())
                .tipoUsuario(tipoUsuario)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAlteracao(LocalDateTime.now())
                .build();

        usuario.validarParaCriacao();

        Usuario salvo = usuarioGateway.salvar(inputDto.getRestauranteId(), usuario);
        return UsuarioOutputDto.fromDomain(salvo);
    }
}
