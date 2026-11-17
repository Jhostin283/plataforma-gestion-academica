package pe.academica.cursos.application.port.in;

import java.util.UUID;
import pe.academica.cursos.domain.Curso;

public interface ActualizarCursoUseCase {
    Curso ejecutar(UUID id, String codigo, String nombre, String descripcion, int duracionHoras, int capacidad);
}
