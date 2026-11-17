package pe.academica.cursos.adapters.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.academica.cursos.application.port.in.RegistrarCursoUseCase;
import pe.academica.cursos.domain.Curso;

@RestController
@RequestMapping("/api/v1/cursos")
class CursoController {
    private final RegistrarCursoUseCase registrar;
    CursoController(RegistrarCursoUseCase registrar) { this.registrar = registrar; }

    record CrearCurso(@NotBlank @Size(max=100) String codigo, @NotBlank @Size(max=255) String nombre,
                      @NotBlank String descripcion, @NotNull @Positive Integer duracionHoras,
                      @NotNull @Positive Integer capacidad) { }

    @PostMapping
    @Operation(summary = "Registrar un curso", responses = {
        @ApiResponse(responseCode="201", description="Curso creado"),
        @ApiResponse(responseCode="400", description="JSON malformado"),
        @ApiResponse(responseCode="409", description="Código duplicado"),
        @ApiResponse(responseCode="422", description="Campos inválidos")})
    ResponseEntity<Curso> crear(@Valid @RequestBody CrearCurso request) {
        Curso curso = registrar.ejecutar(request.codigo(), request.nombre(), request.descripcion(),
                request.duracionHoras(), request.capacidad());
        return ResponseEntity.created(URI.create("/api/v1/cursos/" + curso.id())).body(curso);
    }
}
