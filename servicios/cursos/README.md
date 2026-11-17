# Servicio de cursos

Implementación inicial de `CUR-01` con Java 21, Spring Boot, PostgreSQL y arquitectura hexagonal. Estado: **en progreso**; falta comprobar el reinicio completo de la aplicación con PostgreSQL.

## Configuración

Crear previamente una base PostgreSQL y configurar `CURSOS_DB_URL`, `CURSOS_DB_USER` y `CURSOS_DB_PASSWORD`. Flyway aplica la migración al iniciar. No incluir credenciales reales en archivos versionados.

Con Java 21 y Maven instalados:

```text
mvn verify
mvn spring-boot:run
```

Las pruebas predeterminadas usan H2 en memoria y no sustituyen la verificación con PostgreSQL. JaCoCo genera su reporte en `target/site/jacoco/`. Están implementados `POST /api/v1/cursos` y la consulta básica `GET /api/v1/cursos/{id}`; Springdoc publica el contrato en `/v3/api-docs`. El listado, filtros y disponibilidad de plazas siguen pendientes en `CUR-02` y `CUR-PLZ-01`.

Para incluir `PostgresRegistroCursoTest`, usa una base PostgreSQL aislada para pruebas, configura `CURSOS_DB_URL`, `CURSOS_DB_USER`, `CURSOS_DB_PASSWORD` y `CURSOS_TEST_POSTGRES=true`, y ejecuta `mvn verify`. Las pruebas crean datos; no las ejecutes contra una base compartida o de producción.

Para comprobar la persistencia tras reiniciar la aplicación, con la misma base de pruebas y tras ejecutar `mvn clean verify`, ejecuta desde `servicios/cursos`: `powershell -ExecutionPolicy Bypass -File scripts/verificar-reinicio.ps1`. El script inicia y detiene sus propios procesos del servicio, registra un curso y lo consulta después del reinicio. Deja ese registro en la base de pruebas.
