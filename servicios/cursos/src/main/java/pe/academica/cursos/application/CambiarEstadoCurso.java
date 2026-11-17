package pe.academica.cursos.application;

import java.util.UUID;
import org.springframework.stereotype.Service;
import pe.academica.cursos.application.port.in.CambiarEstadoCursoUseCase;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.Curso;

@Service
public class CambiarEstadoCurso implements CambiarEstadoCursoUseCase {
    private final CursoRepository repository;
    public CambiarEstadoCurso(CursoRepository repository) { this.repository = repository; }
    @Override public Curso ejecutar(UUID id, String estado) { return repository.cambiarEstado(id, estado); }
}
