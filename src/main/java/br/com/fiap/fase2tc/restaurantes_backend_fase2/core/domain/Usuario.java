package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.AcessoNaoAutorizadoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private UUID id;
    private String nome;
    private String email;
    // TODO: associar Restaurante
    private TipoUsuario tipoUsuario;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public boolean isDonoRestaurante() {
        return this.tipoUsuario != null && this.tipoUsuario.isDonoRestaurante();
    }

    public boolean isCliente() {
        return this.tipoUsuario != null && this.tipoUsuario.isCliente();
    }

    public void validarAtivo() {
        if (this.ativo == null || !this.ativo) {
            throw new AcessoNaoAutorizadoException("Usuário inativo ou não autorizado.");
        }
    }

    public void associarTipoUsuario(TipoUsuario novoTipoUsuario) {
        if (novoTipoUsuario == null || novoTipoUsuario.getTipo() == null ||
                novoTipoUsuario.getAtivo() == null || !novoTipoUsuario.getAtivo()) {
            throw new RegraNegocioException("Tipo de usuário válido é obrigatório.");
        }
        // TODO: ao alterar o tipo de DONO_RESTAURANTE p/ CLIENTE garantir que ele não
        // seja o único dono ativo de restaurantes cadastrados
        if (this.tipoUsuario == null ||
                !Objects.equals(this.tipoUsuario.getId(), novoTipoUsuario.getId())) {
            this.tipoUsuario = novoTipoUsuario;
            this.dataAlteracao = LocalDateTime.now();
        } else {
            throw new RegraNegocioException("Tipo de usuário é o mesmo já cadastrado.");
        }
    }
}
