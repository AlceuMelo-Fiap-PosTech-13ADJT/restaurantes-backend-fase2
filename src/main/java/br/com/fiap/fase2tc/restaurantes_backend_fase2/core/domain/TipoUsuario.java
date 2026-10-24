package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuario {

    private UUID id;
    private TipoUsuarioEnum tipo;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public void validarParaCriacao() {
        if (this.tipo == null) {
            throw new RegraNegocioException("Tipo de usuário é obrigatório.");
        }
    }

    public void atualizarTipo(TipoUsuarioEnum novoTipo) {
        if (novoTipo == null) {
            throw new RegraNegocioException("Tipo de usuário é obrigatório.");
        }
        this.tipo = novoTipo;
        this.dataAlteracao = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
        this.dataAlteracao = LocalDateTime.now();
    }

    public boolean isDonoRestaurante() {
        return this.tipo != null && this.tipo.isDonoRestaurante();
    }

    public boolean isCliente() {
        return this.tipo != null && this.tipo.isCliente();
    }

    public String getNome() {
        return this.tipo != null ? this.tipo.getDescricao() : null;
    }

}
