# EST-04: Cambiar estado del estudiante

## Objetivo
Cambiar estado del estudiante para la plataforma de gestión académica.

## Precondiciones
- El estudiante existe.

## Flujo principal
1. Seleccionar activar o desactivar.
2. Guardar el nuevo estado.
3. Confirmar conservando el historial.

## Casos alternativos
- Estudiante inexistente: devolver error.
- Fallo al guardar: informar.

## Criterios de aceptación
- El estado se conserva y se puede consultar.
- Un estudiante inactivo no obtiene nuevas matrículas.

## Endpoint propuesto

`PATCH /api/v1/estudiantes/{estudianteId}/estado` recibe `{"estado":"ACTIVO"}` o `{"estado":"INACTIVO"}` y responde `200` con el estudiante actualizado. Devuelve `404` si no existe, `400` por JSON o UUID inválido y `422` por estado no admitido.

## Criterios adicionales de aceptación

- Repetir el mismo cambio no elimina información ni historial.
- Un estudiante inactivo recibe `409` al intentar crear una matrícula; las matrículas existentes se conservan.

## Trazabilidad
- Requisito: EST-04
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, persistencia y regla de matrícula.
- Issue: [9](https://github.com/Jhostin283/plataforma-gestion-academica/issues/9).
- Pull request: [#32](https://github.com/Jhostin283/plataforma-gestion-academica/pull/32) (borrador).
- Estado: en progreso; cambio de estado preparado, validación y regla de matrícula pendientes.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-EST-04-03:** Repetir estado o enviar valor no permitido.
- **PR-EST-04-04:** Usar ID inexistente o inválido.
- **PR-EST-04-05:** Desactivar un estudiante mediante repositorio simulado.
