package br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.AcessoNaoAutorizadoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.DonoRestauranteInvalidoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RecursoNaoEncontradoException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.core.exception.RegraNegocioException;
import br.com.fiap.fase2tc.restaurantes_backend_fase2.infra.web.json.ErroResponseJson;

@DisplayName("Testes de Tratamento Global de Erros: GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve retornar 403 FORBIDDEN para AcessoNaoAutorizadoException")
    void deveRetornar403AcessoNaoAutorizado() {
        AcessoNaoAutorizadoException ex = new AcessoNaoAutorizadoException("Não autorizado.");
        ResponseEntity<ErroResponseJson> response = handler.handleAcessoNaoAutorizado(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(403);
        assertThat(response.getBody().getMensagem()).isEqualTo("Não autorizado.");
    }

    @Test
    @DisplayName("Deve retornar 403 FORBIDDEN para DonoRestauranteInvalidoException")
    void deveRetornar403DonoInvalido() {
        DonoRestauranteInvalidoException ex = new DonoRestauranteInvalidoException("Operador não é o dono.");
        ResponseEntity<ErroResponseJson> response = handler.handleAcessoNaoAutorizado(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(403);
        assertThat(response.getBody().getMensagem()).isEqualTo("Operador não é o dono.");
    }

    @Test
    @DisplayName("Deve retornar 404 NOT_FOUND para RecursoNaoEncontradoException")
    void deveRetornar404RecursoNaoEncontrado() {
        RecursoNaoEncontradoException ex = new RecursoNaoEncontradoException("Recurso não encontrado.");
        ResponseEntity<ErroResponseJson> response = handler.handleRecursoNaoEncontrado(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getMensagem()).isEqualTo("Recurso não encontrado.");
    }

    @Test
    @DisplayName("Deve retornar 422 UNPROCESSABLE_ENTITY para RegraNegocioException")
    void deveRetornar422RegraNegocio() {
        RegraNegocioException ex = new RegraNegocioException("Erro de negócio.");
        ResponseEntity<ErroResponseJson> response = handler.handleRegraNegocio(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(422);
        assertThat(response.getBody().getMensagem()).isEqualTo("Erro de negócio.");
    }

    @Test
    @DisplayName("Deve retornar 400 BAD_REQUEST para MissingRequestHeaderException")
    void deveRetornar400HeaderFaltante() throws NoSuchMethodException {
        var method = this.getClass().getDeclaredMethod("setUp");
        var parameter = new MethodParameter(method, -1);
        MissingRequestHeaderException ex = new MissingRequestHeaderException("usuario-logado-id", parameter);

        ResponseEntity<ErroResponseJson> response = handler.handleHeaderFaltante(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMensagem()).contains("usuario-logado-id");
    }

    @Test
    @DisplayName("Deve retornar 400 BAD_REQUEST com lista de erros para MethodArgumentNotValidException")
    void deveRetornar400Validacao() throws NoSuchMethodException {
        var method = this.getClass().getDeclaredMethod("setUp");
        var parameter = new MethodParameter(method, -1);
        var bindingResult = new BeanPropertyBindingResult(new Object(), "objeto");
        bindingResult.addError(new FieldError("objeto", "nome", "não pode ser vazio"));
        var ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErroResponseJson> response = handler.handleValidacao(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getErros()).containsExactly("nome: não pode ser vazio");
    }

    @Test
    @DisplayName("Deve retornar 400 BAD_REQUEST para HttpMessageNotReadableException")
    void deveRetornar400MensagemNaoLegivel() {
        var ex = new HttpMessageNotReadableException("JSON malformado", new MockHttpInputMessage(new byte[0]));
        ResponseEntity<ErroResponseJson> response = handler.handleMensagemNaoLegivel(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMensagem()).contains("Corpo da requisição inválido ou mal formatado.");
    }

    @Test
    @DisplayName("Deve retornar 400 BAD_REQUEST para MethodArgumentTypeMismatchException")
    void deveRetornar400TipoInvalido() {
        var ex = new MethodArgumentTypeMismatchException("abc", UUID.class, "id", null, new IllegalArgumentException());
        ResponseEntity<ErroResponseJson> response = handler.handleTipoInvalido(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMensagem()).contains("id").contains("UUID");
    }

    @Test
    @DisplayName("Deve retornar 405 METHOD_NOT_ALLOWED para HttpRequestMethodNotSupportedException")
    void deveRetornar405MetodoNaoSuportado() {
        var ex = new HttpRequestMethodNotSupportedException("PATCH");
        ResponseEntity<ErroResponseJson> response = handler.handleMetodoNaoSuportado(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(405);
        assertThat(response.getBody().getMensagem()).contains("PATCH");
    }

    @Test
    @DisplayName("Deve retornar 500 INTERNAL_SERVER_ERROR para Exception genérica")
    void deveRetornar500Generico() {
        Exception ex = new RuntimeException("Erro inesperado");
        ResponseEntity<ErroResponseJson> response = handler.handleGenerico(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getMensagem()).isEqualTo("Ocorreu um erro interno no servidor.");
    }
}
