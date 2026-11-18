package pe.academica.cursos.application.usecase;

import java.util.UUID;
import org.springframework.stereotype.Service;
import pe.academica.cursos.application.port.in.GestionarReservasUseCase;
import pe.academica.cursos.application.port.out.ReservaRepository;
import pe.academica.cursos.domain.entity.Reserva;
import pe.academica.cursos.application.model.ResultadoReserva;

@Service
public class GestionarReservas implements GestionarReservasUseCase {
    private final ReservaRepository repository;
    public GestionarReservas(ReservaRepository repository) { this.repository = repository; }
    @Override public ResultadoReserva reservar(UUID cursoId, UUID operacionId) { return repository.reservar(cursoId, operacionId); }
    @Override public Reserva consultar(UUID operacionId) { return repository.consultar(operacionId); }
    @Override public Reserva liberar(UUID operacionId) { return repository.liberar(operacionId); }
}
