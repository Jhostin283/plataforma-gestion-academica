package pe.academica.cursos.application.port.in;

import java.util.UUID;
import pe.academica.cursos.domain.Curso;

public interface CambiarEstadoCursoUseCase {
    Curso ejecutar(UUID id, String estado);
}
