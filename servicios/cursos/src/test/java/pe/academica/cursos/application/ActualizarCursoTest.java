package pe.academica.cursos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.Curso;

class ActualizarCursoTest {
    @Test void normalizaDatosYDelegarActualizacion() {
        var repository = mock(CursoRepository.class);
        UUID id = UUID.randomUUID();
        Curso resultado = new Curso(id, "C1", "Java", "Curso", 20, 15, "ACTIVO");
        when(repository.actualizar(id, "C1", "Java", "Curso", 20, 15)).thenReturn(resultado);
        assertThat(new ActualizarCurso(repository).ejecutar(id, " C1 ", " Java ", "Curso", 20, 15)).isEqualTo(resultado);
    }

    @Test void propagaRecursoInexistente() {
        var repository = mock(CursoRepository.class);
        UUID id = UUID.randomUUID();
        when(repository.actualizar(id, "C1", "Java", "Curso", 20, 15)).thenThrow(new CursoNoEncontradoException());
        assertThatThrownBy(() -> new ActualizarCurso(repository).ejecutar(id, "C1", "Java", "Curso", 20, 15))
                .isInstanceOf(CursoNoEncontradoException.class);
    }
}
