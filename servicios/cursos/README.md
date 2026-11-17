# Servicio de cursos

Implementación inicial de `CUR-01` con Java 21, Spring Boot, PostgreSQL y arquitectura hexagonal. Estado: **en progreso**; faltan pruebas contra PostgreSQL, reinicio y concurrencia, además de la consulta de detalle prevista en `CUR-02`.

## Configuración

Crear previamente una base PostgreSQL y configurar `CURSOS_DB_URL`, `CURSOS_DB_USER` y `CURSOS_DB_PASSWORD`. Flyway aplica la migración al iniciar. No incluir credenciales reales en archivos versionados.

Con Java 21 y Maven instalados:

```text
mvn verify
mvn spring-boot:run
```

Las pruebas disponibles usan H2 en memoria y no sustituyen la verificación con PostgreSQL. JaCoCo genera su reporte en `target/site/jacoco/`. El endpoint actual es `POST /api/v1/cursos`; Springdoc publica el contrato en `/v3/api-docs`. La URL de detalle en `Location` se implementará con `CUR-02` y aún no se puede consultar.
