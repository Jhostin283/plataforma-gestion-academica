package pe.academica.cursos.adapters.out.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import pe.academica.cursos.application.CursoDuplicadoException;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.Curso;

@Repository
class PostgresCursoRepository implements CursoRepository {
    private final SpringCursoRepository repository;
    PostgresCursoRepository(SpringCursoRepository repository) { this.repository = repository; }
    @Override public Curso guardar(Curso curso) {
        try { repository.saveAndFlush(new CursoEntity(curso)); return curso; }
        catch (DataIntegrityViolationException ex) {
            // La restricción única es la autoridad ante registros concurrentes.
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("uq_cursos_codigo"))
                throw new CursoDuplicadoException();
            throw ex;
        }
    }
}
