package pe.academica.cursos.adapters.in.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pe.academica.cursos.application.exception.ConflictoReservaException;
import pe.academica.cursos.application.exception.ReservaNoEncontradaException;
import pe.academica.cursos.application.model.ResultadoReserva;
import pe.academica.cursos.application.port.in.GestionarReservasUseCase;
import pe.academica.cursos.domain.entity.Reserva;

class ReservaControllerTest {
    private final GestionarReservasUseCase reservas = mock(GestionarReservasUseCase.class);
    private MockMvc mvc;
    private final UUID cursoId = UUID.randomUUID();
    private final UUID operacionId = UUID.randomUUID();

    @BeforeEach void configurar() {
        mvc = MockMvcBuilders.standaloneSetup(new ReservaController(reservas))
                .setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test void reservaNuevaYReintentoDevuelvenLaMismaOperacion() throws Exception {
        var reserva = new Reserva(operacionId, cursoId, "RESERVADA");
        when(reservas.reservar(cursoId, operacionId))
                .thenReturn(new ResultadoReserva(reserva, true), new ResultadoReserva(reserva, false));
        var ruta = "/api/v1/cursos/" + cursoId + "/reservas";
        mvc.perform(post(ruta).header("Idempotency-Key", operacionId))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/api/v1/reservas/" + operacionId))
                .andExpect(jsonPath("$.operacionId").value(operacionId.toString()));
        mvc.perform(post(ruta).header("Idempotency-Key", operacionId))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("RESERVADA"));
    }

    @Test void consultaYLiberaReserva() throws Exception {
        when(reservas.consultar(operacionId)).thenReturn(new Reserva(operacionId, cursoId, "RESERVADA"));
        when(reservas.liberar(operacionId)).thenReturn(new Reserva(operacionId, cursoId, "LIBERADA"));
        mvc.perform(get("/api/v1/reservas/" + operacionId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RESERVADA"));
        mvc.perform(post("/api/v1/reservas/" + operacionId + "/liberacion")).andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("LIBERADA"));
    }

    @Test void erroresDeClaveConflictoYReservaInexistente() throws Exception {
        var ruta = "/api/v1/cursos/" + cursoId + "/reservas";
        mvc.perform(post(ruta)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("CABECERA_REQUERIDA"));
        mvc.perform(post(ruta).header("Idempotency-Key", "no-es-uuid")).andExpect(status().isBadRequest());
        when(reservas.reservar(cursoId, operacionId)).thenThrow(new ConflictoReservaException("No quedan plazas disponibles"));
        mvc.perform(post(ruta).header("Idempotency-Key", operacionId)).andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("CONFLICTO_RESERVA"));
        when(reservas.consultar(operacionId)).thenThrow(new ReservaNoEncontradaException());
        mvc.perform(get("/api/v1/reservas/" + operacionId)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RESERVA_NO_ENCONTRADA"));
    }
}
