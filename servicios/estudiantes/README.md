# Servicio de estudiantes

Servicio FastAPI con arquitectura hexagonal y PostgreSQL propio. El issue EST-01 incluye registro y consulta básica por ID. La configuración requiere `ESTUDIANTES_DB_URL` con una URL SQLAlchemy PostgreSQL.

Para preparar un entorno de desarrollo, instalar el proyecto con la opción `test`, aplicar `alembic upgrade head` y arrancar `uvicorn app.main:app`. El contrato OpenAPI se publica en `/openapi.json` y el estado de salud en `/health`.

La creación de matrículas es provisional. `CURSOS_BASE_URL` configura la URL del servicio de cursos. Para reintentar operaciones pendientes con sus claves originales se puede ejecutar `python -m app.reconciliar_matriculas` periódicamente. La compensación y las pruebas de concurrencia y recuperación siguen pendientes; no se debe considerar lista para producción.
