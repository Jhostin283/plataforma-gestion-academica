package pe.academica.cursos.adapters.in.web;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pe.academica.cursos.application.CursoDuplicadoException;
import pe.academica.cursos.application.CursoNoEncontradoException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataAccessException;

@RestControllerAdvice
class ApiExceptionHandler {
    record ErrorApi(String codigo, String mensaje, Map<String, String> detalles, String correlationId) { }
    private ResponseEntity<ErrorApi> error(HttpStatus status, String codigo, String mensaje, Map<String, String> detalles) {
        return ResponseEntity.status(status).body(new ErrorApi(codigo, mensaje, detalles, UUID.randomUUID().toString()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorApi> validacion(MethodArgumentNotValidException ex) {
        var detalles = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                e -> e.getField(), e -> e.getDefaultMessage(), (a,b) -> a));
        return error(HttpStatus.UNPROCESSABLE_ENTITY, "DATOS_INVALIDOS", "Los datos del curso no son válidos", detalles);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorApi> json() { return error(HttpStatus.BAD_REQUEST, "JSON_INVALIDO", "El cuerpo no es válido", Map.of()); }
    @ExceptionHandler(CursoDuplicadoException.class)
    ResponseEntity<ErrorApi> duplicado() { return error(HttpStatus.CONFLICT, "CURSO_DUPLICADO", "El código ya existe", Map.of("codigo", "duplicado")); }
    @ExceptionHandler(CursoNoEncontradoException.class)
    ResponseEntity<ErrorApi> noEncontrado() { return error(HttpStatus.NOT_FOUND, "CURSO_NO_ENCONTRADO", "El curso no existe", Map.of()); }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ErrorApi> tipoInvalido() { return error(HttpStatus.BAD_REQUEST, "PARAMETRO_INVALIDO", "El parámetro no es válido", Map.of()); }
    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<ErrorApi> persistencia() {
        return error(HttpStatus.SERVICE_UNAVAILABLE, "PERSISTENCIA_NO_DISPONIBLE", "No se pudo guardar o consultar el curso", Map.of());
    }
}
