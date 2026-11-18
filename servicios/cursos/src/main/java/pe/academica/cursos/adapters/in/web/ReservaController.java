package pe.academica.cursos.adapters.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.academica.cursos.application.port.in.GestionarReservasUseCase;
import pe.academica.cursos.domain.entity.Reserva;

@RestController
@RequestMapping("/api/v1")
class ReservaController {
    private final GestionarReservasUseCase reservas;
    ReservaController(GestionarReservasUseCase reservas) { this.reservas = reservas; }

    @PostMapping("/cursos/{cursoId}/reservas")
    @Operation(summary = "Reservar una plaza", responses = {
        @ApiResponse(responseCode="201", description="Reserva creada"),
        @ApiResponse(responseCode="200", description="Reserva ya existente"),
        @ApiResponse(responseCode="400", description="Identificador o clave inválida"),
        @ApiResponse(responseCode="404", description="Curso no encontrado"),
        @ApiResponse(responseCode="409", description="Curso inactivo, lleno o clave en conflicto")})
    ResponseEntity<Reserva> reservar(@PathVariable UUID cursoId, @RequestHeader("Idempotency-Key") UUID operacionId) {
        var resultado = reservas.reservar(cursoId, operacionId);
        if (resultado.creada()) return ResponseEntity.created(URI.create("/api/v1/reservas/" + operacionId)).body(resultado.reserva());
        return ResponseEntity.ok(resultado.reserva());
    }

    @GetMapping("/reservas/{operacionId}")
    Reserva consultar(@PathVariable UUID operacionId) { return reservas.consultar(operacionId); }

    @PostMapping("/reservas/{operacionId}/liberacion")
    Reserva liberar(@PathVariable UUID operacionId) { return reservas.liberar(operacionId); }
}
