# CUR-03: Actualizar curso

## Objetivo
Actualizar curso para la plataforma de gestión académica.

## Precondiciones
- El curso existe y el administrador proporciona los cambios.

## Flujo principal
1. Consultar el curso.
2. Validar los datos nuevos y su código.
3. Guardar los cambios y responder.

## Casos alternativos
- Código duplicado o dato inválido: rechazar.
- Capacidad menor que reservas vigentes: rechazar.
- Curso inexistente: responder con error.

## Criterios de aceptación
- Los cambios válidos persisten.
- Los cambios inválidos no alteran el registro.
- No se reduce la capacidad por debajo de los plazas reservados.

## Endpoint propuesto

`PUT /api/v1/cursos/{cursoId}` recibe `codigo`, `nombre`, `descripcion`, `duracionHoras` y `capacidad`, y reemplaza los campos editables; el estado se cambia por el endpoint específico. Devuelve `200` con el recurso actualizado.

- `400`: JSON o UUID inválido. `404`: curso inexistente. `422`: campos inválidos.
- `409`: código duplicado o capacidad inferior a reservas vigentes.

## Criterios adicionales de aceptación

- Si cualquier validación falla, ningún campo cambia. La comprobación de capacidad es segura frente a reservas concurrentes.
- La respuesta exitosa refleja los valores persistidos; OpenAPI documenta entradas y errores.

## Trazabilidad
- Requisito: CUR-03
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, persistencia y reglas de capacidad.
- Issue: [3](https://github.com/Jhostin283/plataforma-gestion-academica/issues/3).
- Pull request: [#26](https://github.com/Jhostin283/plataforma-gestion-academica/pull/26) (borrador).
- Estado: aprobado; actualización, validación de capacidad con reservas y cobertura CI (87,50 %) verificados.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-CUR-03-05:** Actualizar UUID inexistente o inválido.
- **PR-CUR-03-06:** Simular repositorio y actualizar un curso con datos válidos.
- **PR-CUR-03-07:** Simular reservas mayores que la capacidad solicitada.
