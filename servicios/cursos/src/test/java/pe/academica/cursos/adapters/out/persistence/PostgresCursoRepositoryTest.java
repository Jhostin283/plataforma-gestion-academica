package pe.academica.cursos.adapters.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import pe.academica.cursos.application.exception.CursoDuplicadoException;
import pe.academica.cursos.domain.entity.Curso;
import pe.academica.cursos.application.exception.ConflictoReservaException;
import pe.academica.cursos.application.exception.CursoNoEncontradoException;

class PostgresCursoRepositoryTest {
    private final SpringCursoRepository jpa = mock(SpringCursoRepository.class);
    private final SpringReservaRepository reservas = mock(SpringReservaRepository.class);
    private final PostgresCursoRepository adapter = new PostgresCursoRepository(jpa, reservas);
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

    @Test void impideReducirCapacidadPorDebajoDeReservas() {
        when(jpa.buscarParaActualizar(curso.id())).thenReturn(Optional.of(new CursoEntity(curso)));
        when(reservas.countByCursoIdAndEstado(curso.id(), "RESERVADA")).thenReturn(3L);
        assertThatThrownBy(() -> adapter.actualizar(curso.id(), "C1", "Java", "Curso", 20, 2))
                .isInstanceOf(ConflictoReservaException.class);
        verify(jpa, never()).saveAndFlush(any());
    }

    @Test void actualizaDatosSinAlterarElEstado() {
        var actual = new CursoEntity(curso);
        when(jpa.buscarParaActualizar(curso.id())).thenReturn(Optional.of(actual));
        when(reservas.countByCursoIdAndEstado(curso.id(), "RESERVADA")).thenReturn(2L);
        when(jpa.saveAndFlush(any(CursoEntity.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
        var resultado = adapter.actualizar(curso.id(), "C2", "Redes", "Contenido", 40, 10);
        assertThat(resultado.codigo()).isEqualTo("C2");
        assertThat(resultado.nombre()).isEqualTo("Redes");
        assertThat(resultado.capacidad()).isEqualTo(10);
        assertThat(resultado.estado()).isEqualTo("ACTIVO");
    }

    @Test void actualizarCursoInexistenteNoGuardaCambios() {
        when(jpa.buscarParaActualizar(curso.id())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> adapter.actualizar(curso.id(), "C2", "Redes", "Contenido", 40, 10))
                .isInstanceOf(CursoNoEncontradoException.class);
        verify(jpa, never()).saveAndFlush(any());
    }

    @Test void actualizarTraduceSoloConflictoDeCodigo() {
        when(jpa.buscarParaActualizar(curso.id())).thenReturn(Optional.of(new CursoEntity(curso)));
        when(jpa.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("uq_cursos_codigo"));
        assertThatThrownBy(() -> adapter.actualizar(curso.id(), "C2", "Redes", "Contenido", 40, 10))
                .isInstanceOf(CursoDuplicadoException.class);
        doThrow(new DataIntegrityViolationException("otra restricción")).when(jpa).saveAndFlush(any());
        assertThatThrownBy(() -> adapter.actualizar(curso.id(), "C2", "Redes", "Contenido", 40, 10))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
