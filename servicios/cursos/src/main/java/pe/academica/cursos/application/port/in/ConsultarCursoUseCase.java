package pe.academica.cursos.application.port.in;

import java.util.UUID;
import pe.academica.cursos.domain.entity.Curso;

public interface ConsultarCursoUseCase {
    Curso ejecutar(UUID id);
    long plazasDisponibles(Curso curso);
}
