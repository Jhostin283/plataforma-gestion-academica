package pe.academica.cursos.adapters.out.persistence;

import jakarta.persistence.*;
import java.util.UUID;
import pe.academica.cursos.domain.entity.Reserva;

@Entity
@Table(name = "reservas")
class ReservaEntity {
    @Id @Column(name = "operacion_id") UUID operacionId;
    @Column(name = "curso_id", nullable = false) UUID cursoId;
    @Column(nullable = false, length = 20) String estado;
    protected ReservaEntity() { }
    ReservaEntity(UUID operacionId, UUID cursoId) { this.operacionId = operacionId; this.cursoId = cursoId; this.estado = "RESERVADA"; }
    Reserva aDominio() { return new Reserva(operacionId, cursoId, estado); }
}
