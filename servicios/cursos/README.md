# Servicio de cursos

Implementación inicial de `CUR-01` con Java 21, Spring Boot, PostgreSQL y arquitectura hexagonal. Estado: **en progreso**; la suite actual contiene únicamente pruebas unitarias con dependencias simuladas.

## Configuración

Crear previamente una base PostgreSQL y configurar `CURSOS_DB_URL`, `CURSOS_DB_USER` y `CURSOS_DB_PASSWORD`. Flyway aplica la migración al iniciar. No incluir credenciales reales en archivos versionados.

Con Java 21 y Maven instalados:

```text
mvn verify
mvn spring-boot:run
```

Las pruebas predeterminadas ejercitan casos de uso y controladores con dependencias simuladas, sin base de datos. La suite no utiliza bases de datos. JaCoCo genera su reporte en `target/site/jacoco/`. Están implementados `POST /api/v1/cursos` y la consulta básica `GET /api/v1/cursos/{id}`; Springdoc publica el contrato en `/v3/api-docs`. El listado, filtros y disponibilidad de plazas siguen pendientes en `CUR-02` y `CUR-PLZ-01`.


Las pruebas unitarias no comprueban migraciones, concurrencia ni persistencia reales. Las comprobaciones realizadas antes del cambio permanecen como evidencia histórica en el registro de progreso.
