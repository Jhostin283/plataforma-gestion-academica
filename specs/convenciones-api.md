# Convenciones propuestas para las API

Estos contratos son requisitos iniciales; aún no existen implementaciones ni documentos OpenAPI ejecutables. Cada servicio debe publicar su propio contrato OpenAPI con esquemas, ejemplos y respuestas coherentes con estas convenciones.

## Versionado

Las rutas HTTP usan el prefijo `/api/v1/` tanto en las API públicas como en la comunicación interna entre servicios. `v1` identifica la primera versión mayor del contrato; una modificación incompatible requiere planificar una nueva versión y su migración. El Ingress enruta las rutas públicas sin retirar el prefijo; las rutas de reserva son internas y se dirigen al servicio de cursos.

## Identificadores y formatos

- Los IDs de cursos, estudiantes, matrículas y operaciones serán UUID serializados como cadenas.
- Las solicitudes y respuestas usan `application/json`; fechas y horas con zona se expresan en ISO 8601 UTC.
- Los listados aceptan `page` desde 0 y `size` entre 1 y 100. La respuesta incluye `items`, `page`, `size` y `total`. Sin coincidencias devuelve `200` con `items: []`.
- Los estados de curso y estudiante son `ACTIVO` e `INACTIVO`. Los estados de matrícula son `PENDIENTE`, `ACTIVA`, `CANCELACION_PENDIENTE`, `CANCELADA` y `FALLIDA`; los detalles de transición se definen en los specs de matrícula.

## Respuestas de error

Los errores devuelven JSON con `codigo`, `mensaje`, `detalles` y `correlationId`. `detalles` puede contener campos inválidos y sus causas; no incluye trazas internas ni credenciales. `correlationId` permite relacionar la respuesta con los registros.

- `400`: JSON malformado, UUID o parámetro de consulta inválido.
- `404`: recurso solicitado inexistente.
- `409`: conflicto de estado, duplicado o regla de negocio que impide la operación.
- `422`: cuerpo JSON válido con campos ausentes o valores que no cumplen validaciones.
- `503`: dependencia requerida no disponible y operación no aceptada; si una operación distribuida ya fue aceptada o su resultado es incierto, seguir el contrato de estado pendiente correspondiente.

Las operaciones distribuidas usan la cabecera `Idempotency-Key` como identificador estable; repetir una clave con una solicitud diferente devuelve `409`. El alcance de unicidad y la retención de claves deben documentarse en OpenAPI y configurarse antes de producción.
