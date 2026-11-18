package pe.academica.cursos.application.port.in;

import pe.academica.cursos.application.model.PaginaCursos;

public interface ListarCursosUseCase {
    PaginaCursos ejecutar(String buscar, int page, int size);
}
