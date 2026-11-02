package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.TipoUsuarioEntity;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.entity.UsuarioEntity;

@SpringBootTest
@Transactional
@DisplayName("Testes de Repositório JPA: UsuarioRepository")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Test
    @DisplayName("Deve salvar usuário e consultar por e-mail e por restaurante")
    void deveSalvarEConsultarPorEmailERestaurante() {
        var tipo = tipoUsuarioRepository.save(TipoUsuarioEntity.builder()
                .tipo(TipoUsuarioEnum.CLIENTE)
                .nome("Cliente")
                .ativo(true)
                .build());

        var restauranteId = UUID.randomUUID();
        var usuario = usuarioRepository.save(UsuarioEntity.builder()
                .nome("Ana Souza")
                .email("ana@cliente.com")
                .restauranteId(restauranteId)
                .tipoUsuario(tipo)
                .ativo(true)
                .build());

        assertThat(usuario.getId()).isNotNull();

        Optional<UsuarioEntity> porEmail = usuarioRepository.findByEmailIgnoreCaseAndAtivoTrue("ana@cliente.com");
        assertThat(porEmail).isPresent();
        assertThat(porEmail.get().getNome()).isEqualTo("Ana Souza");

        List<UsuarioEntity> porRestaurante = usuarioRepository.findByRestauranteIdAndAtivoTrue(restauranteId);
        assertThat(porRestaurante).hasSize(1);
        assertThat(porRestaurante.get(0).getEmail()).isEqualTo("ana@cliente.com");

        boolean existeEmail = usuarioRepository.existsByEmailIgnoreCase("ANA@CLIENTE.COM");
        assertThat(existeEmail).isTrue();
    }
}
