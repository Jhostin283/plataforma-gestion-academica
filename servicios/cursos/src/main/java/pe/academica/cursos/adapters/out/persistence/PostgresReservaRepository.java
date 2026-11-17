package pe.academica.cursos.adapters.out.persistence;

import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.academica.cursos.application.exception.ConflictoReservaException;
import pe.academica.cursos.application.exception.CursoNoEncontradoException;
import pe.academica.cursos.application.exception.ReservaNoEncontradaException;
import pe.academica.cursos.application.port.out.ReservaRepository;
import pe.academica.cursos.domain.entity.Reserva;
import pe.academica.cursos.application.model.ResultadoReserva;

@Repository
class PostgresReservaRepository implements ReservaRepository {
    private final SpringCursoRepository cursos;
    private final SpringReservaRepository reservas;
    PostgresReservaRepository(SpringCursoRepository cursos, SpringReservaRepository reservas) {
        this.cursos = cursos; this.reservas = reservas;
    }

    @Override @Transactional
    public ResultadoReserva reservar(UUID cursoId, UUID operacionId) {
        var curso = cursos.buscarParaActualizar(cursoId).orElseThrow(CursoNoEncontradoException::new);
        var anterior = reservas.findById(operacionId);
        if (anterior.isPresent()) {
            if (!anterior.get().cursoId.equals(cursoId)) throw new ConflictoReservaException("La clave pertenece a otro curso");
            return new ResultadoReserva(anterior.get().aDominio(), false);
        }
        if (!curso.estado.equals("ACTIVO")) throw new ConflictoReservaException("El curso está inactivo");
        if (reservas.countByCursoIdAndEstado(cursoId, "RESERVADA") >= curso.capacidad)
            throw new ConflictoReservaException("No quedan plazas disponibles");
        try {
            var reserva = reservas.saveAndFlush(new ReservaEntity(operacionId, cursoId));
            return new ResultadoReserva(reserva.aDominio(), true);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictoReservaException("La clave de operación ya fue utilizada");
        }
    }

    @Override @Transactional(readOnly = true)
    public Reserva consultar(UUID operacionId) {
        return reservas.findById(operacionId).orElseThrow(ReservaNoEncontradaException::new).aDominio();
    }

    @Override @Transactional
    public Reserva liberar(UUID operacionId) {
        var inicial = reservas.findById(operacionId).orElseThrow(ReservaNoEncontradaException::new);
        cursos.buscarParaActualizar(inicial.cursoId).orElseThrow(CursoNoEncontradoException::new);
        var reserva = reservas.findById(operacionId).orElseThrow(ReservaNoEncontradaException::new);
        reserva.estado = "LIBERADA";
        return reservas.saveAndFlush(reserva).aDominio();
    }
}
