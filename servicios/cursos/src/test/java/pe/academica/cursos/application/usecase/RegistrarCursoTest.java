package pe.academica.cursos.application.usecase;

import pe.academica.cursos.application.exception.CursoDuplicadoException;
import pe.academica.cursos.application.exception.CursoNoEncontradoException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.domain.entity.Curso;

class RegistrarCursoTest {
    private final CursoRepository repository = mock(CursoRepository.class);
    private final RegistrarCurso registrar = new RegistrarCurso(repository);

    @Test void creaCursoActivoConDatosNormalizados() {
        when(repository.guardar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Curso curso = registrar.ejecutar("  CUR-01  ", "  Java  ", "Descripción", 20, 15);
        assertThat(curso.id()).isNotNull();
        assertThat(curso.codigo()).isEqualTo("CUR-01");
        assertThat(curso.nombre()).isEqualTo("Java");
        assertThat(curso.estado()).isEqualTo("ACTIVO");
        verify(repository).guardar(curso);
    }

    @Test void propagaConflictoDeCodigoDuplicado() {
        when(repository.guardar(any())).thenThrow(new CursoDuplicadoException());
        assertThatThrownBy(() -> registrar.ejecutar("C1", "Java", "Descripción", 20, 15))
                .isInstanceOf(CursoDuplicadoException.class);
    }

    @Test void consultaCursoExistente() {
        UUID id = UUID.randomUUID();
        Curso curso = new Curso(id, "C1", "Java", "Descripción", 20, 15, "ACTIVO");
        when(repository.buscarPorId(id)).thenReturn(Optional.of(curso));
        assertThat(new ConsultarCurso(repository).ejecutar(id)).isEqualTo(curso);
    }

    @Test void consultaCursoInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> new ConsultarCurso(repository).ejecutar(id))
                .isInstanceOf(CursoNoEncontradoException.class);
    }
}
