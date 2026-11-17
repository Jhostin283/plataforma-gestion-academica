# CUR-CUR-03: Actualizar curso

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
- Requisito: CUR-CUR-03
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, persistencia y reglas de capacidad.
- Issue: pendiente.
- Pull request: pendiente.
- Estado: pendiente.
