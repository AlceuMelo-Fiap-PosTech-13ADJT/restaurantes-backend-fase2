package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.domain.TipoUsuarioEnum;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.ItemCardapioRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.RestauranteRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.TipoUsuarioRepository;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.database.jpa.repository.UsuarioRepository;

@SpringBootTest
@DisplayName("Testes de Inicializacao e Seeds SQL: data.sql e init.sql")
class SqlScriptsInitializationTest {

    private static final UUID ID_TIPO_DONO = UUID.fromString("e1a8a992-1262-4f0e-b8d2-1c6d860d5b01");
    private static final UUID ID_TIPO_CLIENTE = UUID.fromString("e1a8a992-1262-4f0e-b8d2-1c6d860d5b02");

    private static final UUID ID_RESTAURANTE_1 = UUID.fromString("c3b01f92-563b-4f68-b7e1-88892df9a001");
    private static final UUID ID_RESTAURANTE_2 = UUID.fromString("c3b01f92-563b-4f68-b7e1-88892df9a002");

    private static final UUID ID_DONO_R1 = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1001");
    private static final UUID ID_CLIENTE_R1 = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1002");
    private static final UUID ID_DONO_R2 = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1003");
    private static final UUID ID_CLIENTE_R2 = UUID.fromString("b1a0f443-4318-4720-bd44-df41865c1004");

    private static final UUID ID_ITEM_1 = UUID.fromString("f4c02a83-674c-4e79-c8f2-99903ef0b001");
    private static final UUID ID_ITEM_2 = UUID.fromString("f4c02a83-674c-4e79-c8f2-99903ef0b002");
    private static final UUID ID_ITEM_3 = UUID.fromString("f4c02a83-674c-4e79-c8f2-99903ef0b003");
    private static final UUID ID_ITEM_4 = UUID.fromString("f4c02a83-674c-4e79-c8f2-99903ef0b004");

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Test
    @DisplayName("Deve carregar corretamente os tipos de usuario a partir do data.sql")
    void deveCarregarTiposUsuarioDoDataSql() {
        var dono = tipoUsuarioRepository.findById(ID_TIPO_DONO);
        assertThat(dono).isPresent();
        assertThat(dono.get().getTipo()).isEqualTo(TipoUsuarioEnum.DONO_RESTAURANTE);
        assertThat(dono.get().getNome()).isEqualTo("Dono de restaurante");
        assertThat(dono.get().getAtivo()).isTrue();

        var cliente = tipoUsuarioRepository.findById(ID_TIPO_CLIENTE);
        assertThat(cliente).isPresent();
        assertThat(cliente.get().getTipo()).isEqualTo(TipoUsuarioEnum.CLIENTE);
        assertThat(cliente.get().getNome()).isEqualTo("Cliente");
        assertThat(cliente.get().getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve carregar os usuarios de teste e seus relacionamentos a partir do data.sql")
    void deveCarregarUsuariosDoDataSql() {
        var donoR1 = usuarioRepository.findById(ID_DONO_R1);
        assertThat(donoR1).isPresent();
        assertThat(donoR1.get().getEmail()).isEqualTo("carlos.silva@bellanapoli.com");
        assertThat(donoR1.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_1);
        assertThat(donoR1.get().getTipoUsuario().getId()).isEqualTo(ID_TIPO_DONO);

        var clienteR1 = usuarioRepository.findById(ID_CLIENTE_R1);
        assertThat(clienteR1).isPresent();
        assertThat(clienteR1.get().getEmail()).isEqualTo("mariana.costa@email.com");
        assertThat(clienteR1.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_1);
        assertThat(clienteR1.get().getTipoUsuario().getId()).isEqualTo(ID_TIPO_CLIENTE);

        var donoR2 = usuarioRepository.findById(ID_DONO_R2);
        assertThat(donoR2).isPresent();
        assertThat(donoR2.get().getEmail()).isEqualTo("jose.santos@sabordosertao.com");
        assertThat(donoR2.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_2);

        var clienteR2 = usuarioRepository.findById(ID_CLIENTE_R2);
        assertThat(clienteR2).isPresent();
        assertThat(clienteR2.get().getEmail()).isEqualTo("fernanda.lima@email.com");
        assertThat(clienteR2.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_2);
    }

    @Test
    @DisplayName("Deve carregar os restaurantes iniciais com seus donos a partir do data.sql")
    void deveCarregarRestaurantesDoDataSql() {
        var r1 = restauranteRepository.findById(ID_RESTAURANTE_1);
        assertThat(r1).isPresent();
        assertThat(r1.get().getNome()).isEqualTo("Cantina Bella Roma");
        assertThat(r1.get().getTipoCozinha()).isEqualTo("Italiana");
        assertThat(r1.get().getDono().getId()).isEqualTo(ID_DONO_R1);

        var r2 = restauranteRepository.findById(ID_RESTAURANTE_2);
        assertThat(r2).isPresent();
        assertThat(r2.get().getNome()).isEqualTo("Sabor do Sertao");
        assertThat(r2.get().getTipoCozinha()).isEqualTo("Nordestina");
        assertThat(r2.get().getDono().getId()).isEqualTo(ID_DONO_R2);
    }

    @Test
    @DisplayName("Deve carregar os itens de cardapio vinculados aos restaurantes a partir do data.sql")
    void deveCarregarItensCardapioDoDataSql() {
        var item1 = itemCardapioRepository.findById(ID_ITEM_1);
        assertThat(item1).isPresent();
        assertThat(item1.get().getNome()).isEqualTo("Pizza Margherita");
        assertThat(item1.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_1);
        assertThat(item1.get().getDisponivelApenasRestaurante()).isFalse();

        var item2 = itemCardapioRepository.findById(ID_ITEM_2);
        assertThat(item2).isPresent();
        assertThat(item2.get().getNome()).isEqualTo("Lasanha Bolonhesa Classica");
        assertThat(item2.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_1);
        assertThat(item2.get().getDisponivelApenasRestaurante()).isTrue();

        var item3 = itemCardapioRepository.findById(ID_ITEM_3);
        assertThat(item3).isPresent();
        assertThat(item3.get().getNome()).isEqualTo("Baiao de Dois Especial");
        assertThat(item3.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_2);

        var item4 = itemCardapioRepository.findById(ID_ITEM_4);
        assertThat(item4).isPresent();
        assertThat(item4.get().getNome()).isEqualTo("Carne de Sol com Macaxeira");
        assertThat(item4.get().getRestauranteId()).isEqualTo(ID_RESTAURANTE_2);

        var itensR1 = itemCardapioRepository.findByRestauranteIdAndAtivoTrue(ID_RESTAURANTE_1);
        assertThat(itensR1).hasSize(2);

        var itensR2 = itemCardapioRepository.findByRestauranteIdAndAtivoTrue(ID_RESTAURANTE_2);
        assertThat(itensR2).hasSize(2);
    }

    @Test
    @DisplayName("Deve validar a presenca e integridade do arquivo init.sql para MySQL Docker")
    void deveValidarArquivoInitSql() throws Exception {
        Path pathInitSql = Path.of("infra", "scripts", "init.sql");
        assertThat(Files.exists(pathInitSql)).isTrue();

        String conteudo = Files.readString(pathInitSql);
        assertThat(conteudo).contains("CREATE DATABASE IF NOT EXISTS restaurantes_db");
        assertThat(conteudo).contains("CREATE TABLE IF NOT EXISTS tipos_usuario");
        assertThat(conteudo).contains("CREATE TABLE IF NOT EXISTS usuarios");
        assertThat(conteudo).contains("CREATE TABLE IF NOT EXISTS restaurantes");
        assertThat(conteudo).contains("CREATE TABLE IF NOT EXISTS itens_cardapio");
        assertThat(conteudo).contains("e1a8a992-1262-4f0e-b8d2-1c6d860d5b01");
        assertThat(conteudo).contains("c3b01f92-563b-4f68-b7e1-88892df9a001");
        assertThat(conteudo).contains("b1a0f443-4318-4720-bd44-df41865c1001");
        assertThat(conteudo).contains("f4c02a83-674c-4e79-c8f2-99903ef0b001");
    }
}
