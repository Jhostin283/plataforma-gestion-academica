package pe.academica.cursos.application;

import org.springframework.stereotype.Service;
import pe.academica.cursos.application.port.in.ListarCursosUseCase;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.PaginaCursos;

@Service
public class ListarCursos implements ListarCursosUseCase {
    private final CursoRepository repository;
    public ListarCursos(CursoRepository repository) { this.repository = repository; }

    @Override public PaginaCursos ejecutar(String buscar, int page, int size) {
        if (page < 0 || size < 1 || size > 100) throw new ParametrosConsultaInvalidosException();
        return repository.listar(buscar == null ? "" : buscar.trim(), page, size);
    }
}
