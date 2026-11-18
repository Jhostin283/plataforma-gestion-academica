# Servicio de cursos

Implementación inicial de `CUR-01` con Java 21, Spring Boot, PostgreSQL y arquitectura hexagonal. Estado: **en progreso**; la suite actual contiene únicamente pruebas unitarias con dependencias simuladas.

## Configuración

Crear previamente una base PostgreSQL y configurar `CURSOS_DB_URL`, `CURSOS_DB_USER` y `CURSOS_DB_PASSWORD`. Flyway aplica la migración al iniciar. No incluir credenciales reales en archivos versionados.

Con Java 21 y Maven instalados:

```text
mvn verify
mvn spring-boot:run
```

La suite ejercita casos de uso y controladores con dependencias simuladas. JaCoCo genera su reporte en `target/site/jacoco/`. Están implementados `POST /api/v1/cursos`, `GET /api/v1/cursos/{id}` y `GET /api/v1/cursos` con búsqueda y paginación; Springdoc publica el contrato en `/v3/api-docs`. La disponibilidad de plazas sigue pendiente en `CUR-PLZ-01`.


Las pruebas unitarias no comprueban migraciones, concurrencia ni persistencia reales. Las comprobaciones realizadas antes del cambio permanecen como evidencia histórica en el registro de progreso.
