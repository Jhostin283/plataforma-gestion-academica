package pe.academica.cursos.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.entity.Curso;

class CambiarEstadoCursoTest {
    @Test void desactivaCursoMediantePuerto() {
        var repository = mock(CursoRepository.class);
        UUID id = UUID.randomUUID();
        var resultado = new Curso(id, "C1", "Java", "Curso", 20, 15, "INACTIVO");
        when(repository.cambiarEstado(id, "INACTIVO")).thenReturn(resultado);
        assertThat(new CambiarEstadoCurso(repository).ejecutar(id, "INACTIVO").estado()).isEqualTo("INACTIVO");
        verify(repository).cambiarEstado(id, "INACTIVO");
    }
}
