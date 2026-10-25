package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OperationCustomizer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;

@DisplayName("Testes de Configuração: OpenApiConfig")
class OpenApiConfigTest {

    private OpenApiConfig openApiConfig;

    @BeforeEach
    void setUp() {
        openApiConfig = new OpenApiConfig();
    }

    @Test
    @DisplayName("Deve configurar metadados OpenAPI corretamente")
    void deveConfigurarMetadadosOpenApi() {
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Restaurantes API - Fase 2");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(openAPI.getInfo().getDescription())
                .isEqualTo("API REST com Clean Architecture para gestão de restaurantes, cardápios e tipos de usuário.");
    }

    @Test
    @DisplayName("Deve adicionar header usuario-logado-id via OperationCustomizer quando não existir")
    void deveAdicionarHeaderUsuarioLogadoViaCustomizerQuandoNaoExistir() {
        OperationCustomizer customizer = openApiConfig.customizarHeaderUsuarioLogado();
        Operation operation = new Operation();

        Operation resultado = customizer.customize(operation, null);

        assertThat(resultado.getParameters()).isNotNull().hasSize(1);
        Parameter param = resultado.getParameters().get(0);
        assertThat(param.getName()).isEqualTo("usuario-logado-id");
        assertThat(param.getIn()).isEqualTo("header");
        assertThat(param.getRequired()).isTrue();
        assertThat(param.getDescription()).contains("UUID do usuário logado");
    }

    @Test
    @DisplayName("Não deve duplicar header usuario-logado-id se já existente na operação")
    void deveNaoDuplicarHeaderSeJaExistente() {
        OperationCustomizer customizer = openApiConfig.customizarHeaderUsuarioLogado();
        Operation operation = new Operation();
        operation.addParametersItem(new Parameter().name("usuario-logado-id").in("header"));

        Operation resultado = customizer.customize(operation, null);

        assertThat(resultado.getParameters()).hasSize(1);
        Parameter param = resultado.getParameters().get(0);
        assertThat(param.getName()).isEqualTo("usuario-logado-id");
        assertThat(param.getIn()).isEqualTo("header");
        assertThat(param.getRequired()).isTrue();
        assertThat(param.getDescription()).contains("UUID do usuário logado");
    }
}
