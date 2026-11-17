package pe.academica.cursos.application.port.out;

import pe.academica.cursos.domain.Curso;
import java.util.Optional;
import java.util.UUID;
import pe.academica.cursos.domain.PaginaCursos;

public interface CursoRepository {
    Curso guardar(Curso curso);
    Optional<Curso> buscarPorId(UUID id);
    PaginaCursos listar(String buscar, int page, int size);
    Curso actualizar(UUID id, String codigo, String nombre, String descripcion, int duracionHoras, int capacidad);
}
