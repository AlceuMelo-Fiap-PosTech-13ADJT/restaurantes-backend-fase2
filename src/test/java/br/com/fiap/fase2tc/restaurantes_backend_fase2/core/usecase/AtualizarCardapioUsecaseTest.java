package br.com.fiap.fase2tc.restaurantes_backend_fase2.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.AtualizarItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioInputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.dto.ItemCardapioOutputDto;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UseCase: AtualizarCardapioUsecase")
class AtualizarCardapioUsecaseTest {

    @Mock
    private AtualizarItemCardapioUsecase atualizarItemCardapioUsecase;

    @InjectMocks
    private AtualizarCardapioUsecaseImpl usecase;

    @Test
    @DisplayName("Deve delegar atualização para AtualizarItemCardapioUsecase")
    void deveDelegarAtualizacao() {
        var input = ItemCardapioInputDto.builder()
                .id(UUID.randomUUID())
                .restauranteId(UUID.randomUUID())
                .usuarioOperadorId(UUID.randomUUID())
                .nome("Pizza")
                .preco(BigDecimal.TEN)
                .build();

        var outputEsperado = ItemCardapioOutputDto.builder().nome("Pizza").build();
        when(atualizarItemCardapioUsecase.executar(any(AtualizarItemCardapioInputDto.class))).thenReturn(outputEsperado);

        ItemCardapioOutputDto output = usecase.executar(input);

        assertThat(output).isNotNull();
        assertThat(output.getNome()).isEqualTo("Pizza");
        verify(atualizarItemCardapioUsecase).executar(any(AtualizarItemCardapioInputDto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando input for nulo")
    void deveLancarExcecaoQuandoInputNulo() {
        assertThatThrownBy(() -> usecase.executar(null))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Dados do item do cardápio são obrigatórios.");
    }
}
