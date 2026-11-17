package pe.academica.cursos.application.port.out;

import pe.academica.cursos.domain.Curso;
import java.util.Optional;
import java.util.UUID;

public interface CursoRepository {
    Curso guardar(Curso curso);
    Optional<Curso> buscarPorId(UUID id);
}
