package pe.academica.cursos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

/** Requires an isolated PostgreSQL database supplied via CURSOS_DB_* environment variables. */
@EnabledIfEnvironmentVariable(named = "CURSOS_TEST_POSTGRES", matches = "true")
@SpringBootTest
@AutoConfigureMockMvc
class PostgresRegistroCursoTest {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;

    private String body(String codigo) {
        return """
                {"codigo":"%s","nombre":"Curso PostgreSQL","descripcion":"Prueba de integración","duracionHoras":20,"capacidad":15}
                """.formatted(codigo);
    }

    @Test void migracionYRegistroPersistenEnPostgres() throws Exception {
        String codigo = UUID.randomUUID().toString();
        mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(body(codigo)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.estado").value("ACTIVO"));
        Integer cantidad = jdbc.queryForObject("select count(*) from cursos where codigo = ?", Integer.class, codigo);
        assertThat(cantidad).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from flyway_schema_history where success = true", Integer.class)).isGreaterThan(0);
    }

    @Test void dosRegistrosConcurrentesConMismoCodigoSoloCreanUno() throws Exception {
        String codigo = UUID.randomUUID().toString();
        CountDownLatch inicio = new CountDownLatch(1);
        Callable<Integer> registro = () -> {
            inicio.await();
            return mvc.perform(post("/api/v1/cursos").contentType(MediaType.APPLICATION_JSON).content(body(codigo)))
                    .andReturn().getResponse().getStatus();
        };
        try (var executor = Executors.newFixedThreadPool(2)) {
            var uno = executor.submit(registro);
            var dos = executor.submit(registro);
            inicio.countDown();
            assertThat(List.of(uno.get(), dos.get())).containsExactlyInAnyOrder(201, 409);
        }
        assertThat(jdbc.queryForObject("select count(*) from cursos where codigo = ?", Integer.class, codigo)).isEqualTo(1);
    }
}
