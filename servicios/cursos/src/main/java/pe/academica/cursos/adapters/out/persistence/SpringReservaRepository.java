package pe.academica.cursos.adapters.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringReservaRepository extends JpaRepository<ReservaEntity, UUID> {
    long countByCursoIdAndEstado(UUID cursoId, String estado);
}
