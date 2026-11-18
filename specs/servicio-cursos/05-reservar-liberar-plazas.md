# CUR-PLZ-01: Reservar y liberar plazas

## Objetivo
Reservar y liberar plazas para la plataforma de gestión académica.

## Precondiciones
- El curso existe.
- La solicitud incluye una clave estable de operación.

## Flujo principal
1. Consultar estado del curso y capacidad.
2. Reservar una plaza de forma atómica.
3. Consultar o liberar la reserva mediante su identificador o clave.

## Casos alternativos
- Curso inactivo o lleno: rechazar.
- Clave repetida con datos distintos: responder conflicto.
- Respuesta perdida: permitir consultar o reintentar.

## Criterios de aceptación
- Las solicitudes simultáneas no superan la capacidad.
- Repetir una reserva o liberación no duplica el efecto.
- El estado de la reserva puede consultarse.

## Endpoints propuestos

- `POST /api/v1/cursos/{cursoId}/reservas`: requiere `Idempotency-Key` con UUID de operación; devuelve `201` con `operacionId`, `cursoId`, `estado: RESERVADA` y `Location`. Un reintento idéntico confirmado devuelve `200` con la misma reserva.
- `GET /api/v1/reservas/{operacionId}`: devuelve `200` con el estado `RESERVADA` o `LIBERADA`; `404` si no existe.
- `POST /api/v1/reservas/{operacionId}/liberacion`: libera la reserva y devuelve `200` con estado `LIBERADA`; repetir devuelve el mismo resultado. `404` si no existe.
- Para reserva: `404` si el curso no existe; `409` por curso inactivo, sin plazas o clave reutilizada con otro curso; `400` por UUID o clave inválida.

## Criterios adicionales de aceptación

- Dos solicitudes distintas por la última plaza producen una reserva exitosa y un `409`; la cantidad reservada nunca supera la capacidad.
- Una solicitud repetida con la misma clave no crea otra reserva, incluso si la primera respuesta se pierde. La liberación repetida no modifica dos veces la disponibilidad.
- La base garantiza la operación atómica; errores no exponen datos internos. Las rutas internas se documentan en OpenAPI y en el contrato compartido.

## Trazabilidad
- Requisito: CUR-PLZ-01
- Contrato relacionado: CUR-PLZ-01; specs/comunicacion-entre-servicios.md
- Pruebas: PostgreSQL, concurrencia, contrato e idempotencia.
- Issue: [5](https://github.com/Jhostin283/plataforma-gestion-academica/issues/5).
- Pull request: [#28](https://github.com/Jhostin283/plataforma-gestion-academica/pull/28) (borrador).
- Estado: aprobado; concurrencia, idempotencia, contrato y cobertura verificados satisfactoriamente.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-CUR-PLZ-01-05:** Reservar en curso inexistente, inactivo o lleno.
- **PR-CUR-PLZ-01-08:** Usar clave, UUID o reserva inexistente inválidos.
- **PR-CUR-PLZ-01-09:** Simular curso lleno y solicitar reserva.
- **PR-CUR-PLZ-01-10:** Simular reserva existente para la misma clave.
