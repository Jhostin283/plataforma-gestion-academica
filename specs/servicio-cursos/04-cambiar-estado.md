# CUR-04: Cambiar estado del curso

## Objetivo
Cambiar estado del curso para la plataforma de gestión académica.

## Precondiciones
- El curso existe.

## Flujo principal
1. Seleccionar activar o desactivar.
2. Actualizar el estado.
3. Confirmar el resultado y conservar el historial.

## Casos alternativos
- Curso inexistente: responder con error.
- Fallo al guardar: no afirmar éxito.

## Criterios de aceptación
- El estado persiste y se muestra en consultas.
- Un curso inactivo rechaza reservas nuevas.
- Las reservas existentes se conservan.

## Endpoint propuesto

`PATCH /api/v1/cursos/{cursoId}/estado` recibe `{"estado":"ACTIVO"}` o `{"estado":"INACTIVO"}` y devuelve `200` con el curso actualizado. `404` indica curso inexistente; `400`, JSON o UUID inválido; `422`, estado ausente o no permitido.

## Criterios adicionales de aceptación

- Repetir el mismo cambio conserva el estado y no modifica reservas previas.
- Al desactivar un curso, las reservas nuevas se rechazan con `409`; las existentes permanecen consultables y liberables.

## Trazabilidad
- Requisito: CUR-04
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, persistencia y reserva de curso inactivo.
- Issue: [4](https://github.com/Jhostin283/plataforma-gestion-academica/issues/4).
- Pull request: [#27](https://github.com/Jhostin283/plataforma-gestion-academica/pull/27) (borrador).
- Estado: en progreso; la validación de reservas corresponde a CUR-PLZ-01.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-CUR-04-03:** Repetir estado o enviar estado no permitido.
- **PR-CUR-04-04:** Usar ID inexistente o malformado.
- **PR-CUR-04-05:** Desactivar un curso activo mediante repositorio simulado.
