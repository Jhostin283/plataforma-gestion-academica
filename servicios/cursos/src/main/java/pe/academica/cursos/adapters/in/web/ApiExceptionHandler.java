package pe.academica.cursos.adapters.in.web;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pe.academica.cursos.application.exception.CursoDuplicadoException;
import pe.academica.cursos.application.exception.CursoNoEncontradoException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.CannotCreateTransactionException;
import pe.academica.cursos.application.exception.ParametrosConsultaInvalidosException;
import pe.academica.cursos.application.exception.ConflictoReservaException;
import pe.academica.cursos.application.exception.ReservaNoEncontradaException;
import org.springframework.web.bind.MissingRequestHeaderException;

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
    @ExceptionHandler(ParametrosConsultaInvalidosException.class)
    ResponseEntity<ErrorApi> paginacionInvalida() { return error(HttpStatus.BAD_REQUEST, "PAGINACION_INVALIDA", "La paginación no es válida", Map.of()); }
    @ExceptionHandler(ConflictoReservaException.class)
    ResponseEntity<ErrorApi> conflictoReserva(ConflictoReservaException ex) { return error(HttpStatus.CONFLICT, "CONFLICTO_RESERVA", ex.getMessage(), Map.of()); }
    @ExceptionHandler(ReservaNoEncontradaException.class)
    ResponseEntity<ErrorApi> reservaNoEncontrada() { return error(HttpStatus.NOT_FOUND, "RESERVA_NO_ENCONTRADA", "La reserva no existe", Map.of()); }
    @ExceptionHandler(MissingRequestHeaderException.class)
    ResponseEntity<ErrorApi> cabeceraAusente() { return error(HttpStatus.BAD_REQUEST, "CABECERA_REQUERIDA", "Falta una cabecera obligatoria", Map.of()); }
    @ExceptionHandler({DataAccessException.class, CannotCreateTransactionException.class})
    ResponseEntity<ErrorApi> persistencia() {
        return error(HttpStatus.SERVICE_UNAVAILABLE, "PERSISTENCIA_NO_DISPONIBLE", "No se pudo guardar o consultar el curso", Map.of());
    }
}
