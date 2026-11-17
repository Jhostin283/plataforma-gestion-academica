package pe.academica.cursos.application.port.out;

import pe.academica.cursos.domain.entity.Curso;
import java.util.Optional;
import java.util.UUID;
import pe.academica.cursos.application.model.PaginaCursos;

public interface CursoRepository {
    Curso guardar(Curso curso);
    Optional<Curso> buscarPorId(UUID id);
    PaginaCursos listar(String buscar, int page, int size);
    Curso actualizar(UUID id, String codigo, String nombre, String descripcion, int duracionHoras, int capacidad);
    Curso cambiarEstado(UUID id, String estado);
    long contarReservasActivas(UUID id);
}
