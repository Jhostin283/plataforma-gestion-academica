package pe.academica.cursos.application.port.out;

import pe.academica.cursos.domain.Curso;

public interface CursoRepository {
    Curso guardar(Curso curso);
}
