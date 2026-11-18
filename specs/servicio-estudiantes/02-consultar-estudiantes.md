# EST-02: Consultar estudiantes

## Objetivo
Consultar estudiantes para la plataforma de gestión académica.

## Precondiciones
- El servicio de estudiantes está disponible.

## Flujo principal
1. Solicitar listado o búsqueda por nombre o documento.
2. Devolver resultados.
3. Consultar el detalle seleccionado.

## Casos alternativos
- Sin resultados: devolver lista vacía.
- Estudiante inexistente: devolver error claro.

## Criterios de aceptación
- Búsqueda, listado y detalle devuelven datos correctos.
- Filtros y errores están documentados en OpenAPI.

## Endpoints propuestos

- `GET /api/v1/estudiantes?buscar=texto&page=0&size=20`: busca por nombres, apellidos o documento; devuelve listado paginado con `200`.
- `GET /api/v1/estudiantes/{estudianteId}`: devuelve detalle con `200`, o `404` si no existe.
- `400` ante UUID o paginación inválidos; una búsqueda sin resultados responde `200` con `items: []`.

## Criterios adicionales de aceptación

- Los resultados y el total corresponden a los filtros aplicados. No se devuelven campos internos o datos sensibles innecesarios.
- El contrato OpenAPI documenta filtros, paginación, respuestas y errores.

## Trazabilidad
- Requisito: EST-02
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, filtros y casos vacíos.
- Issue: [7](https://github.com/Jhostin283/plataforma-gestion-academica/issues/7).
- Pull request: [#30](https://github.com/Jhostin283/plataforma-gestion-academica/pull/30) (borrador).
- Estado: aprobado; búsqueda, detalle y cobertura CI (81,18 %) verificados.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-EST-02-02:** Buscar sin coincidencias.
- **PR-EST-02-03:** Consultar detalle existente, inexistente y UUID inválido.
- **PR-EST-02-04:** Probar paginación válida e inválida.
