package pe.academica.cursos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:cursos;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
@AutoConfigureMockMvc
class RegistroCursoTest {
    @Autowired MockMvc mvc;

    private String curso(String codigo) {
        return """
                {"codigo":"%s","nombre":"Java","descripcion":"Curso básico","duracionHoras":20,"capacidad":15}
                """.formatted(codigo);
    }

    @Test void creaCursoConEstadoActivo() throws Exception {
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(curso(UUID.randomUUID().toString())))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.estado").value("ACTIVO")).andExpect(jsonPath("$.id").exists());
    }

    @Test void rechazaCodigoDuplicado() throws Exception {
        String body = curso(UUID.randomUUID().toString());
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated());
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.codigo").value("CURSO_DUPLICADO"))
                .andExpect(jsonPath("$.correlationId").exists());
    }

    @Test void rechazaCamposInvalidos() throws Exception {
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":\"\",\"nombre\":\"\",\"descripcion\":\"\",\"duracionHoras\":0,\"capacidad\":-1}"))
                .andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.detalles.codigo").exists())
                .andExpect(jsonPath("$.detalles.capacidad").exists());
    }

    @Test void rechazaJsonMalformado() throws Exception {
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("JSON_INVALIDO"));
    }

    @Test void publicaContratoOpenApi() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/cursos'].post").exists());
    }

    @Test void cursoCreadoSeConsultaUsandoLocation() throws Exception {
        String codigo = UUID.randomUUID().toString();
        var created = mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(curso(codigo)))
                .andExpect(status().isCreated()).andReturn();
        mvc.perform(get(created.getResponse().getHeader("Location")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.codigo").value(codigo))
                .andExpect(jsonPath("$.nombre").value("Java"));
    }

    @Test void detalleInexistenteEIdInvalidoRetornanErroresDocumentados() throws Exception {
        mvc.perform(get("/api/v1/cursos/" + UUID.randomUUID()))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.codigo").value("CURSO_NO_ENCONTRADO"));
        mvc.perform(get("/api/v1/cursos/no-es-uuid"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.codigo").value("PARAMETRO_INVALIDO"));
    }
}
