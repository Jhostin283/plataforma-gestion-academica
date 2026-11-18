package pe.academica.cursos.application.port.in;

import java.util.UUID;
import pe.academica.cursos.domain.entity.Reserva;
import pe.academica.cursos.application.model.ResultadoReserva;

public interface GestionarReservasUseCase {
    ResultadoReserva reservar(UUID cursoId, UUID operacionId);
    Reserva consultar(UUID operacionId);
    Reserva liberar(UUID operacionId);
}
