package pe.academica.cursos.application.port.out;

import java.util.UUID;
import pe.academica.cursos.domain.entity.Reserva;
import pe.academica.cursos.application.model.ResultadoReserva;

public interface ReservaRepository {
    ResultadoReserva reservar(UUID cursoId, UUID operacionId);
    Reserva consultar(UUID operacionId);
    Reserva liberar(UUID operacionId);
}
