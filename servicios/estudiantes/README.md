# Servicio de estudiantes

Servicio FastAPI con arquitectura hexagonal y PostgreSQL propio. El issue EST-01 incluye registro y consulta básica por ID. La configuración requiere `ESTUDIANTES_DB_URL` con una URL SQLAlchemy PostgreSQL.

Para preparar un entorno de desarrollo, instalar el proyecto con la opción `test`, aplicar `alembic upgrade head` y arrancar `uvicorn app.main:app`. El contrato OpenAPI se publica en `/openapi.json` y el estado de salud en `/health`.

La creación de matrículas es provisional. `CURSOS_BASE_URL` configura la URL del servicio de cursos. El servicio intenta reconciliar las operaciones pendientes periódicamente; el intervalo se configura con `MATRICULAS_RECONCILIACION_SEGUNDOS` (60 segundos por defecto). También se puede ejecutar un ciclo manual con `python -m app.reconciliar_matriculas`. Las pruebas de persistencia, compensación, concurrencia y recuperación siguen pendientes; no se debe considerar lista para producción.
