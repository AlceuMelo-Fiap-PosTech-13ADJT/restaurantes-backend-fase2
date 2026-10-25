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
    private UUID restauranteId;
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

    public boolean isAtivo() {
        return this.ativo != null && Boolean.TRUE.equals(this.ativo);
    }

    public void validarAtivo() {
        if (this.ativo == null || Boolean.FALSE.equals(this.ativo)) {
            throw new AcessoNaoAutorizadoException("Usuário inativo ou não autorizado.");
        }
    }

    public void validarParaCriacao() {
        if (this.nome == null || this.nome.trim().isBlank()) {
            throw new RegraNegocioException("Nome do usuário é obrigatório.");
        }
        if (this.email == null || this.email.trim().isBlank()) {
            throw new RegraNegocioException("E-mail do usuário é obrigatório.");
        }
        if (this.tipoUsuario == null) {
            throw new RegraNegocioException("Tipo de usuário é obrigatório.");
        }
    }

    public void atualizar(String novoNome, String novoEmail, TipoUsuario novoTipoUsuario) {
        if (novoNome != null && !novoNome.trim().isBlank()) {
            this.nome = novoNome.trim();
        }
        if (novoEmail != null && !novoEmail.trim().isBlank()) {
            this.email = novoEmail.trim();
        }
        if (novoTipoUsuario != null) {
            this.tipoUsuario = novoTipoUsuario;
        }
        this.dataAlteracao = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
        this.dataAlteracao = LocalDateTime.now();
    }

    public void associarTipoUsuario(TipoUsuario novoTipoUsuario) {
        if (novoTipoUsuario == null || novoTipoUsuario.getTipo() == null ||
                novoTipoUsuario.getAtivo() == null || !novoTipoUsuario.getAtivo()) {
            throw new RegraNegocioException("Tipo de usuário válido é obrigatório.");
        }
        if (this.tipoUsuario == null ||
                !Objects.equals(this.tipoUsuario.getId(), novoTipoUsuario.getId())) {
            this.tipoUsuario = novoTipoUsuario;
            this.dataAlteracao = LocalDateTime.now();
        } else {
            throw new RegraNegocioException("Tipo de usuário é o mesmo já cadastrado.");
        }
    }
}
