package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;

@Configuration
public class OpenApiConfig {

    public static final String HEADER_USUARIO_LOGADO = "usuario-logado-id";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurantes API - Fase 2")
                        .version("1.0.0")
                        .description("API REST com Clean Architecture para gestão de restaurantes, cardápios e tipos de usuário."));
    }

    @Bean
    public OperationCustomizer customizarHeaderUsuarioLogado() {
        return (operation, handlerMethod) -> {
            Parameter headerParam = null;
            if (operation.getParameters() != null) {
                headerParam = operation.getParameters().stream()
                        .filter(p -> HEADER_USUARIO_LOGADO.equalsIgnoreCase(p.getName()))
                        .findFirst()
                        .orElse(null);
            }

            if (headerParam == null) {
                headerParam = new Parameter()
                        .name(HEADER_USUARIO_LOGADO)
                        .in("header");
                operation.addParametersItem(headerParam);
            }

            headerParam.setDescription("UUID do usuário logado realizando a operação (controle de acesso e tenant)");
            headerParam.setRequired(true);
            if (headerParam.getSchema() == null) {
                headerParam.setSchema(new StringSchema().format("uuid").example("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"));
            }

            return operation;
        };
    }
}
