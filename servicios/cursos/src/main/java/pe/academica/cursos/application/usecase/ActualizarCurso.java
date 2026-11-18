package pe.academica.cursos.application.usecase;

import java.util.UUID;
import org.springframework.stereotype.Service;
import pe.academica.cursos.application.port.in.ActualizarCursoUseCase;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.entity.Curso;

@Service
public class ActualizarCurso implements ActualizarCursoUseCase {
    private final CursoRepository repository;
    public ActualizarCurso(CursoRepository repository) { this.repository = repository; }

    @Override public Curso ejecutar(UUID id, String codigo, String nombre, String descripcion, int duracionHoras, int capacidad) {
        return repository.actualizar(id, codigo.trim(), nombre.trim(), descripcion, duracionHoras, capacidad);
    }
}
