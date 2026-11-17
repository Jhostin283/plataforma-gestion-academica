package pe.academica.cursos.application;

import java.util.UUID;
import org.springframework.stereotype.Service;
import pe.academica.cursos.application.port.in.ConsultarCursoUseCase;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.Curso;

@Service
public class ConsultarCurso implements ConsultarCursoUseCase {
    private final CursoRepository repository;
    public ConsultarCurso(CursoRepository repository) { this.repository = repository; }
    @Override public Curso ejecutar(UUID id) {
        return repository.buscarPorId(id).orElseThrow(CursoNoEncontradoException::new);
    }
}
