package pe.academica.cursos.adapters.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import pe.academica.cursos.application.exception.ConflictoReservaException;
import pe.academica.cursos.application.exception.CursoNoEncontradoException;
import pe.academica.cursos.application.exception.ReservaNoEncontradaException;
import pe.academica.cursos.domain.entity.Curso;
import org.springframework.dao.DataIntegrityViolationException;

class PostgresReservaRepositoryTest {
    private final SpringCursoRepository cursos = mock(SpringCursoRepository.class);
    private final SpringReservaRepository reservas = mock(SpringReservaRepository.class);
    private final PostgresReservaRepository adapter = new PostgresReservaRepository(cursos, reservas);
    private final UUID cursoId = UUID.randomUUID();
    private final UUID operacionId = UUID.randomUUID();
    private final CursoEntity curso = new CursoEntity(new Curso(cursoId, "C1", "Java", "Curso", 20, 1, "ACTIVO"));

    @Test void reservaPlazaDisponible() {
        when(cursos.buscarParaActualizar(cursoId)).thenReturn(Optional.of(curso));
        when(reservas.findById(operacionId)).thenReturn(Optional.empty());
        when(reservas.saveAndFlush(any(ReservaEntity.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(adapter.reservar(cursoId, operacionId).creada()).isTrue();
    }

    @Test void devuelveLaMismaReservaAlReintentar() {
        when(cursos.buscarParaActualizar(cursoId)).thenReturn(Optional.of(curso));
        when(reservas.findById(operacionId)).thenReturn(Optional.of(new ReservaEntity(operacionId, cursoId)));
        assertThat(adapter.reservar(cursoId, operacionId).creada()).isFalse();
        verify(reservas, never()).saveAndFlush(any(ReservaEntity.class));
    }

    @Test void rechazaCursoLlenoOInactivo() {
        when(cursos.buscarParaActualizar(cursoId)).thenReturn(Optional.of(curso));
        when(reservas.findById(operacionId)).thenReturn(Optional.empty());
        when(reservas.countByCursoIdAndEstado(cursoId, "RESERVADA")).thenReturn(1L);
        assertThatThrownBy(() -> adapter.reservar(cursoId, operacionId)).isInstanceOf(ConflictoReservaException.class);
        curso.estado = "INACTIVO";
        assertThatThrownBy(() -> adapter.reservar(cursoId, operacionId)).isInstanceOf(ConflictoReservaException.class);
    }

    @Test void liberarUnaReservaEsIdempotente() {
        var reserva = new ReservaEntity(operacionId, cursoId);
        when(reservas.findById(operacionId)).thenReturn(Optional.of(reserva));
        when(cursos.buscarParaActualizar(cursoId)).thenReturn(Optional.of(curso));
        when(reservas.saveAndFlush(any(ReservaEntity.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(adapter.liberar(operacionId).estado()).isEqualTo("LIBERADA");
        assertThat(adapter.liberar(operacionId).estado()).isEqualTo("LIBERADA");
    }

    @Test void consultaReservaYRechazaReservaInexistente() {
        var reserva = new ReservaEntity(operacionId, cursoId);
        when(reservas.findById(operacionId)).thenReturn(Optional.of(reserva));
        assertThat(adapter.consultar(operacionId).operacionId()).isEqualTo(operacionId);
        when(reservas.findById(operacionId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> adapter.consultar(operacionId)).isInstanceOf(ReservaNoEncontradaException.class);
        assertThatThrownBy(() -> adapter.liberar(operacionId)).isInstanceOf(ReservaNoEncontradaException.class);
    }

    @Test void rechazaCursoInexistenteYClaveDeOtroCurso() {
        when(cursos.buscarParaActualizar(cursoId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> adapter.reservar(cursoId, operacionId)).isInstanceOf(CursoNoEncontradoException.class);
        when(cursos.buscarParaActualizar(cursoId)).thenReturn(Optional.of(curso));
        when(reservas.findById(operacionId)).thenReturn(Optional.of(new ReservaEntity(operacionId, UUID.randomUUID())));
        assertThatThrownBy(() -> adapter.reservar(cursoId, operacionId)).isInstanceOf(ConflictoReservaException.class);
    }

    @Test void traduceConflictoDeClaveAlGuardar() {
        when(cursos.buscarParaActualizar(cursoId)).thenReturn(Optional.of(curso));
        when(reservas.findById(operacionId)).thenReturn(Optional.empty());
        when(reservas.saveAndFlush(any(ReservaEntity.class))).thenThrow(new DataIntegrityViolationException("conflicto"));
        assertThatThrownBy(() -> adapter.reservar(cursoId, operacionId)).isInstanceOf(ConflictoReservaException.class);
    }
}
