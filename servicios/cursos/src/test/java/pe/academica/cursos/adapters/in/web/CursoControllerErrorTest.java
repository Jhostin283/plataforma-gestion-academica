package pe.academica.cursos.adapters.in.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import java.util.UUID;
import pe.academica.cursos.application.exception.CursoDuplicadoException;
import pe.academica.cursos.application.exception.CursoNoEncontradoException;
import pe.academica.cursos.domain.entity.Curso;
import pe.academica.cursos.application.port.in.ConsultarCursoUseCase;
import pe.academica.cursos.application.port.in.RegistrarCursoUseCase;
import pe.academica.cursos.application.port.in.ListarCursosUseCase;
import pe.academica.cursos.application.port.in.ActualizarCursoUseCase;
import pe.academica.cursos.application.port.in.CambiarEstadoCursoUseCase;
import pe.academica.cursos.application.model.PaginaCursos;
import java.util.List;

class CursoControllerErrorTest {
    MockMvc mvc;
    RegistrarCursoUseCase registrar;
    ConsultarCursoUseCase consultar;
    ListarCursosUseCase listar;
    ActualizarCursoUseCase actualizar;
    CambiarEstadoCursoUseCase cambiarEstado;

    @BeforeEach void configurar() {
        registrar = mock(RegistrarCursoUseCase.class);
        consultar = mock(ConsultarCursoUseCase.class);
        listar = mock(ListarCursosUseCase.class);
        actualizar = mock(ActualizarCursoUseCase.class);
        cambiarEstado = mock(CambiarEstadoCursoUseCase.class);
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mvc = MockMvcBuilders.standaloneSetup(new CursoController(registrar, consultar, listar, actualizar, cambiarEstado))
                .setControllerAdvice(new ApiExceptionHandler()).setValidator(validator).build();
    }

    private final String body = "{\"codigo\":\"C1\",\"nombre\":\"Java\",\"descripcion\":\"Curso\",\"duracionHoras\":20,\"capacidad\":15}";

    @Test void listaSinResultadosYValidaParametros() throws Exception {
        when(listar.ejecutar("sin coincidencia", 0, 20)).thenReturn(new PaginaCursos(List.of(), 0, 20, 0));
        mvc.perform(get("/api/v1/cursos").param("buscar", "sin coincidencia"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.total").value(0));
        mvc.perform(get("/api/v1/cursos").param("page", "abc"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("PARAMETRO_INVALIDO"));
    }

    @Test void cambiaEstadoYRechazaValoresInvalidos() throws Exception {
        UUID id = UUID.randomUUID();
        when(cambiarEstado.ejecutar(id, "INACTIVO")).thenReturn(new Curso(id, "C1", "Java", "Curso", 20, 15, "INACTIVO"));
        mvc.perform(patch("/api/v1/cursos/" + id + "/estado").contentType(MediaType.APPLICATION_JSON).content("{\"estado\":\"INACTIVO\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("INACTIVO"));
        mvc.perform(patch("/api/v1/cursos/" + id + "/estado").contentType(MediaType.APPLICATION_JSON).content("{\"estado\":\"OTRO\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test void registroExitosoDevuelveLocationYEstado() throws Exception {
        UUID id = UUID.randomUUID();
        when(registrar.ejecutar(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(new Curso(id, "C1", "Java", "Curso", 20, 15, "ACTIVO"));
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/api/v1/cursos/" + id))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test void codigoDuplicadoDevuelveConflicto() throws Exception {
        when(registrar.ejecutar(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new CursoDuplicadoException());
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.codigo").value("CURSO_DUPLICADO"));
    }

    @Test void entradasInvalidasYJsonMalformado() throws Exception {
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":\"\",\"nombre\":\"\",\"descripcion\":\"\",\"duracionHoras\":0,\"capacidad\":-1}"))
                .andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.detalles.codigo").exists())
                .andExpect(jsonPath("$.detalles.capacidad").exists());
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("JSON_INVALIDO"));
    }

    @Test void consultaCursoYErroresDeId() throws Exception {
        UUID id = UUID.randomUUID();
        UUID desconocido = UUID.randomUUID();
        when(consultar.ejecutar(id)).thenReturn(new Curso(id, "C1", "Java", "Curso", 20, 15, "ACTIVO"));
        when(consultar.ejecutar(desconocido)).thenThrow(new CursoNoEncontradoException());
        mvc.perform(get("/api/v1/cursos/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.codigo").value("C1"));
        mvc.perform(get("/api/v1/cursos/" + desconocido)).andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/cursos/no-es-uuid")).andExpect(status().isBadRequest());
    }

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

    @Test void falloAlAbrirTransaccionProduce503SinExponerDetalles() throws Exception {
        when(registrar.ejecutar(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new CannotCreateTransactionException("detalle interno sensible"));
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.codigo").value("PERSISTENCIA_NO_DISPONIBLE"))
                .andExpect(jsonPath("$.mensaje").value("No se pudo guardar o consultar el curso"))
                .andExpect(jsonPath("$.correlationId").exists());
    }
}
