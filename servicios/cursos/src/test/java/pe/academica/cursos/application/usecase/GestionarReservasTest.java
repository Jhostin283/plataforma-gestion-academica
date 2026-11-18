package pe.academica.cursos.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import pe.academica.cursos.application.model.ResultadoReserva;
import pe.academica.cursos.application.port.out.ReservaRepository;
import pe.academica.cursos.domain.entity.Reserva;

class GestionarReservasTest {
    @Test void delegaReservaConsultaYLiberacionAlPuerto() {
        var repo = mock(ReservaRepository.class);
        var caso = new GestionarReservas(repo);
        var curso = UUID.randomUUID();
        var clave = UUID.randomUUID();
        var reserva = new Reserva(clave, curso, "RESERVADA");
        var liberada = new Reserva(clave, curso, "LIBERADA");
        when(repo.reservar(curso, clave)).thenReturn(new ResultadoReserva(reserva, true));
        when(repo.consultar(clave)).thenReturn(reserva);
        when(repo.liberar(clave)).thenReturn(liberada);
        assertThat(caso.reservar(curso, clave).reserva()).isEqualTo(reserva);
        assertThat(caso.consultar(clave)).isEqualTo(reserva);
        assertThat(caso.liberar(clave)).isEqualTo(liberada);
        verify(repo).reservar(curso, clave);
        verify(repo).consultar(clave);
        verify(repo).liberar(clave);
    }
}
