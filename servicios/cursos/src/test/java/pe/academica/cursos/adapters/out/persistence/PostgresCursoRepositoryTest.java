package pe.academica.cursos.adapters.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import pe.academica.cursos.application.CursoDuplicadoException;
import pe.academica.cursos.domain.Curso;

class PostgresCursoRepositoryTest {
    private final SpringCursoRepository jpa = mock(SpringCursoRepository.class);
    private final PostgresCursoRepository adapter = new PostgresCursoRepository(jpa);
    private final Curso curso = new Curso(UUID.randomUUID(), "C1", "Java", "Curso", 20, 15, "ACTIVO");

    @Test void guardaYConsultaMediantePuertoDePersistencia() {
        assertThat(adapter.guardar(curso)).isEqualTo(curso);
        when(jpa.findById(curso.id())).thenReturn(Optional.of(new CursoEntity(curso)));
        assertThat(adapter.buscarPorId(curso.id())).contains(curso);
    }

    @Test void traduceRestriccionDeCodigoDuplicado() {
        when(jpa.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("uq_cursos_codigo"));
        assertThatThrownBy(() -> adapter.guardar(curso)).isInstanceOf(CursoDuplicadoException.class);
    }

    @Test void propagaOtrasViolacionesDeIntegridad() {
        when(jpa.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("otra restricción"));
        assertThatThrownBy(() -> adapter.guardar(curso)).isInstanceOf(DataIntegrityViolationException.class);
    }
}
