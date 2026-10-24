package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain;

import java.math.BigDecimal;
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
public class ItemCardapio {

    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivelApenasRestaurante;
    private String fotoPath;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    public void validarParaCriacao() {
        if (this.nome == null || this.nome.isBlank()) {
            throw new RegraNegocioException("Nome do item é obrigatório.");
        }
        if (this.descricao == null || this.descricao.isBlank()) {
            throw new RegraNegocioException("Descrição do item é obrigatória.");
        }
        if (this.preco == null || this.preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Preço deve ser maior que zero.");
        }
        if (this.disponivelApenasRestaurante == null) {
            throw new RegraNegocioException("Informar se a disponibilidade é apenas no restaurante é obrigatória.");
        }
    }

    public void validarParaAtualizacao() {
        if (this.nome == null || this.nome.isBlank()) {
            throw new RegraNegocioException("Nome do item é obrigatório.");
        }
        if (this.descricao == null || this.descricao.isBlank()) {
            throw new RegraNegocioException("Descrição do item é obrigatória.");
        }
        if (this.preco == null || this.preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Preço deve ser maior que zero.");
        }
        if (this.disponivelApenasRestaurante == null) {
            throw new RegraNegocioException("Informar se a disponibilidade é apenas no restaurante é obrigatória.");
        }
    }

    public void atualizarDados(String nome, String descricao, BigDecimal preco, Boolean disponivelApenasRestaurante,
            String fotoPath) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelApenasRestaurante = disponivelApenasRestaurante;
        this.fotoPath = fotoPath;
        validarParaAtualizacao();
        this.dataAlteracao = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
        this.dataAlteracao = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return this.ativo != null && Boolean.TRUE.equals(this.ativo);
    }

    public void validarAtivo() {
        if (this.ativo == null || Boolean.FALSE.equals(this.ativo)) {
            throw new RegraNegocioException("Item de cardápio inativo.");
        }
    }

}
