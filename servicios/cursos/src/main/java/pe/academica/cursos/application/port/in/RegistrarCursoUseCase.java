package pe.academica.cursos.application.port.in;

import pe.academica.cursos.domain.Curso;

public interface RegistrarCursoUseCase {
    Curso ejecutar(String codigo, String nombre, String descripcion, int duracionHoras, int capacidad);
}
