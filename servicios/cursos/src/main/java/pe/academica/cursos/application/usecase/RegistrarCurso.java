package pe.academica.cursos.application.usecase;

import java.util.UUID;
import org.springframework.stereotype.Service;
import pe.academica.cursos.domain.entity.Curso;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.application.port.in.RegistrarCursoUseCase;

@Service
public class RegistrarCurso implements RegistrarCursoUseCase {
    private final CursoRepository repository;
    public RegistrarCurso(CursoRepository repository) { this.repository = repository; }
    @Override public Curso ejecutar(String codigo, String nombre, String descripcion, int duracionHoras, int capacidad) {
        return repository.guardar(new Curso(UUID.randomUUID(), codigo.trim(), nombre.trim(), descripcion,
                duracionHoras, capacidad, "ACTIVO"));
    }
}
