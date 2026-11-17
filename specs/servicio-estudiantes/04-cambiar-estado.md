# EST-EST-04: Cambiar estado del estudiante

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
- Requisito: EST-EST-04
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, persistencia y regla de matrícula.
- Issue: pendiente.
- Pull request: pendiente.
- Estado: pendiente.
