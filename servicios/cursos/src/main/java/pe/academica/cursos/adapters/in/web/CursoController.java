package pe.academica.cursos.adapters.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.academica.cursos.application.port.in.RegistrarCursoUseCase;
import pe.academica.cursos.application.port.in.ConsultarCursoUseCase;
import pe.academica.cursos.application.port.in.ListarCursosUseCase;
import pe.academica.cursos.domain.Curso;
import pe.academica.cursos.domain.PaginaCursos;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/cursos")
class CursoController {
    private final RegistrarCursoUseCase registrar;
    private final ConsultarCursoUseCase consultar;
    private final ListarCursosUseCase listar;
    CursoController(RegistrarCursoUseCase registrar, ConsultarCursoUseCase consultar, ListarCursosUseCase listar) {
        this.registrar = registrar; this.consultar = consultar; this.listar = listar;
    }

    @Schema(example = "{\"codigo\":\"CUR-001\",\"nombre\":\"Java\",\"descripcion\":\"Fundamentos\",\"duracionHoras\":20,\"capacidad\":30}")
    record CrearCurso(@NotBlank @Size(max=100) String codigo, @NotBlank @Size(max=255) String nombre,
                      @NotBlank String descripcion, @NotNull @Positive Integer duracionHoras,
                      @NotNull @Positive Integer capacidad) { }

    @PostMapping
    @Operation(summary = "Registrar un curso", description = "El código distingue mayúsculas y minúsculas; se eliminan espacios al inicio y al final antes de guardarlo.", responses = {
        @ApiResponse(responseCode="201", description="Curso creado"),
        @ApiResponse(responseCode="400", description="JSON malformado"),
        @ApiResponse(responseCode="409", description="Código duplicado"),
        @ApiResponse(responseCode="422", description="Campos inválidos"),
        @ApiResponse(responseCode="503", description="Persistencia no disponible")})
    ResponseEntity<Curso> crear(@Valid @RequestBody CrearCurso request) {
        Curso curso = registrar.ejecutar(request.codigo(), request.nombre(), request.descripcion(),
                request.duracionHoras(), request.capacidad());
        return ResponseEntity.created(URI.create("/api/v1/cursos/" + curso.id())).body(curso);
    }

    @GetMapping("/{cursoId}")
    @Operation(summary = "Consultar un curso por ID", responses = {
        @ApiResponse(responseCode="200", description="Curso encontrado"),
        @ApiResponse(responseCode="400", description="ID inválido"),
        @ApiResponse(responseCode="404", description="Curso no encontrado")})
    ResponseEntity<Curso> obtener(@PathVariable UUID cursoId) {
        return ResponseEntity.ok(consultar.ejecutar(cursoId));
    }

    @GetMapping
    @Operation(summary = "Listar y buscar cursos", description = "Busca por nombre o código. page comienza en 0 y size admite valores de 1 a 100.", responses = {
        @ApiResponse(responseCode="200", description="Página de cursos"),
        @ApiResponse(responseCode="400", description="Parámetros inválidos"),
        @ApiResponse(responseCode="503", description="Persistencia no disponible")})
    ResponseEntity<PaginaCursos> listar(@RequestParam(defaultValue = "") String buscar,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(listar.ejecutar(buscar, page, size));
    }
}
