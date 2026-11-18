package pe.academica.cursos.adapters.out.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import pe.academica.cursos.application.CursoDuplicadoException;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.Curso;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pe.academica.cursos.domain.PaginaCursos;
import org.springframework.transaction.annotation.Transactional;
import pe.academica.cursos.application.CursoNoEncontradoException;

@Repository
class PostgresCursoRepository implements CursoRepository {
    private final SpringCursoRepository repository;
    PostgresCursoRepository(SpringCursoRepository repository) { this.repository = repository; }
    @Override public Optional<Curso> buscarPorId(UUID id) {
        return repository.findById(id).map(CursoEntity::aDominio);
    }
    @Override public PaginaCursos listar(String buscar, int page, int size) {
        var resultado = repository.buscar(buscar, PageRequest.of(page, size, Sort.by("codigo").ascending()));
        return new PaginaCursos(resultado.getContent().stream().map(CursoEntity::aDominio).toList(),
                page, size, resultado.getTotalElements());
    }
    @Override @Transactional
    public Curso actualizar(UUID id, String codigo, String nombre, String descripcion, int duracionHoras, int capacidad) {
        CursoEntity actual = repository.buscarParaActualizar(id).orElseThrow(CursoNoEncontradoException::new);
        // El control de reservas vigentes se incorpora junto con CUR-PLZ-01.
        actual.codigo = codigo; actual.nombre = nombre; actual.descripcion = descripcion;
        actual.duracionHoras = duracionHoras; actual.capacidad = capacidad;
        try { return repository.saveAndFlush(actual).aDominio(); }
        catch (DataIntegrityViolationException ex) {
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("uq_cursos_codigo"))
                throw new CursoDuplicadoException();
            throw ex;
        }
    }
    @Override @Transactional
    public Curso cambiarEstado(UUID id, String estado) {
        CursoEntity actual = repository.buscarParaActualizar(id).orElseThrow(CursoNoEncontradoException::new);
        actual.estado = estado;
        return repository.saveAndFlush(actual).aDominio();
    }
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
