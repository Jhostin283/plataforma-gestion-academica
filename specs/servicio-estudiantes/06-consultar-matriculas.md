# EST-MAT-02: Consultar matrículas

## Objetivo
Consultar matrículas para la plataforma de gestión académica.

## Precondiciones
- El servicio de estudiantes está disponible.

## Flujo principal
1. Seleccionar un estudiante o curso.
2. Solicitar sus matrículas.
3. Mostrar estado e historial según filtros.

## Casos alternativos
- Sin matrículas: devolver lista vacía.
- Filtro inválido: devolver error claro.

## Criterios de aceptación
- Se consulta por estudiante y por curso.
- Los estados y registros históricos son correctos.
- Los filtros se documentan en OpenAPI.

## Endpoints propuestos

- `GET /api/v1/matriculas?estudianteId=UUID&cursoId=UUID&estado=ACTIVA&page=0&size=20`: filtra por uno o ambos IDs y devuelve `200` con lista paginada. Sin coincidencias devuelve `items: []`.
- `GET /api/v1/matriculas/{matriculaId}`: devuelve `200` con IDs, estado y datos disponibles de la operación; `404` si no existe.
- `400` ante UUID o paginación inválidos. Los estados pendientes se muestran explícitamente.

## Criterios adicionales de aceptación

- La consulta por curso o estudiante incluye el historial según el filtro de estado documentado. La información procede de la base propia.
- La consulta del detalle permite verificar el resultado posterior de una operación aceptada con `202`.

## Trazabilidad
- Requisito: EST-MAT-02
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, filtros e historial.
- Issue: [11](https://github.com/Jhostin283/plataforma-gestion-academica/issues/11).
- Pull request: [#34](https://github.com/Jhostin283/plataforma-gestion-academica/pull/34) (borrador).
- Estado: en progreso; filtros, detalle y casos preparados, validación pendiente.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-EST-MAT-02-02:** Consultar sin coincidencias o con paginación inválida.
- **PR-EST-MAT-02-03:** Consultar detalle existente, inexistente y UUID inválido.
- **PR-EST-MAT-02-05:** Consultar matrículas con puerto de repositorio simulado.
