package pe.academica.cursos.adapters.in.web;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pe.academica.cursos.application.CursoDuplicadoException;

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
}
