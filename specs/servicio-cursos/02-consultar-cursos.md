# CUR-02: Consultar cursos

## Objetivo
Consultar cursos para la plataforma de gestión académica.

## Precondiciones
- El servicio de cursos está disponible.

## Flujo principal
1. Solicitar listado o búsqueda por nombre o código.
2. Consultar los registros y devolver resultados.
3. Seleccionar un curso para ver el detalle.

## Casos alternativos
- Sin coincidencias: mostrar lista vacía.
- Curso inexistente: responder con error claro.
- Servicio no disponible: informar el fallo.

## Criterios de aceptación
- El listado, búsqueda y detalle devuelven datos correctos.
- Se documentan filtros, paginación y errores en OpenAPI.

## Endpoints propuestos

- `GET /api/v1/cursos?buscar=texto&page=0&size=20`: busca por nombre o código y devuelve `items`, `page`, `size` y `total` con `200`.
- `GET /api/v1/cursos/{cursoId}`: devuelve el curso, capacidad y plazas disponibles con `200`; `404` si no existe.
- `400` para UUID o paginación inválidos; una búsqueda sin coincidencias devuelve lista vacía con `200`.

## Criterios adicionales de aceptación

- El detalle refleja reservas vigentes y nunca muestra plazas negativos.
- Los filtros, paginación y campos de respuesta están descritos en OpenAPI y cubiertos por pruebas.

## Trazabilidad
- Requisito: CUR-02
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, filtros y resultados vacíos.
- Issue: [2](https://github.com/Jhostin283/plataforma-gestion-academica/issues/2).
- Pull request: [#25](https://github.com/Jhostin283/plataforma-gestion-academica/pull/25) (borrador).
- Estado: aprobado; consulta, detalle con cálculo de plazas y cobertura CI (87,50 %) verificados.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-CUR-02-02:** Buscar texto sin coincidencias.
- **PR-CUR-02-03:** Consultar detalle con ID existente e inexistente.
- **PR-CUR-02-04:** Usar UUID o parámetros de paginación inválidos.
