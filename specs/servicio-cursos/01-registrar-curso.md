# CUR-01: Registrar curso

## Objetivo
Registrar curso para la plataforma de gestión académica.

## Precondiciones
- El administrador dispone de los datos del curso.

## Flujo principal
1. Ingresar código, nombre, descripción, duración y capacidad.
2. Validar los datos y el código.
3. Guardar el curso y confirmar su creación.

## Casos alternativos
- Código duplicado o datos inválidos: rechazar e indicar los campos afectados.
- Fallo de persistencia: informar el error sin confirmar la creación.

## Criterios de aceptación
- Se registra y consulta un curso válido.
- El código es único y se rechazan datos inválidos.
- Los datos sobreviven al reinicio del servicio.

## Endpoint propuesto

`POST /api/v1/cursos` recibe `codigo`, `nombre`, `descripcion`, `duracionHoras` y `capacidad`. El código y nombre no pueden estar vacíos; duración y capacidad son enteros positivos. La creación establece `estado: ACTIVO`.

- `201`: devuelve `id` y el recurso creado; `Location` apunta al detalle.
- `400`: JSON malformado. `422`: campos faltantes o inválidos. `409`: código duplicado.

## Criterios adicionales de aceptación

- El código duplicado, aun en solicitudes simultáneas, produce un único registro; la comparación de mayúsculas se definirá en OpenAPI.
- Cada error contiene código, mensaje y detalles según [convenciones](../convenciones-api.md). No se persiste información ante validación fallida.
- La documentación OpenAPI incluye ejemplo de cuerpo, respuestas y errores.

## Trazabilidad
- Requisito: CUR-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, validación y persistencia PostgreSQL.
- Issue: [1](https://github.com/Jhostin283/plataforma-gestion-academica/issues/1).
- Pull request: [#24](https://github.com/Jhostin283/plataforma-gestion-academica/pull/24) (borrador).
- Estado: en progreso; pruebas unitarias aprobadas, criterios de persistencia real pendientes.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-CUR-01-01:** Enviar un curso válido al controlador con el caso de uso simulado.
- **PR-CUR-01-02:** Simular conflicto de código duplicado en el caso de uso.
- **PR-CUR-01-03:** Omitir campo obligatorio o usar duración/capacidad no positivas.
- **PR-CUR-01-04:** Enviar JSON malformado.
- **PR-CUR-01-06:** Simular fallo de persistencia al registrar el curso.
- **PR-CUR-01-07:** Registrar un curso con repositorio simulado y espacios en código y nombre.
- **PR-CUR-01-08:** Simular que el repositorio informa un código duplicado.
