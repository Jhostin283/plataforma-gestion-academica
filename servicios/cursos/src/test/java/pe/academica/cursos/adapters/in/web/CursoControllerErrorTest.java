package pe.academica.cursos.adapters.in.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.academica.cursos.application.port.in.ConsultarCursoUseCase;
import pe.academica.cursos.application.port.in.RegistrarCursoUseCase;

@WebMvcTest(CursoController.class)
class CursoControllerErrorTest {
    @Autowired MockMvc mvc;
    @MockBean RegistrarCursoUseCase registrar;
    @MockBean ConsultarCursoUseCase consultar;

    @Test void falloDePersistenciaProduceErrorClaroSinExponerDetalles() throws Exception {
        when(registrar.ejecutar(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new DataAccessResourceFailureException("detalle interno sensible"));
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":\"C1\",\"nombre\":\"Java\",\"descripcion\":\"Curso\",\"duracionHoras\":20,\"capacidad\":15}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.codigo").value("PERSISTENCIA_NO_DISPONIBLE"))
                .andExpect(jsonPath("$.mensaje").value("No se pudo guardar o consultar el curso"))
                .andExpect(jsonPath("$.correlationId").exists());
    }
}
