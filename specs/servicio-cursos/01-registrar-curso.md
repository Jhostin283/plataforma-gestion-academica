# CUR-CUR-01: Registrar curso

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
- Requisito: CUR-CUR-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, validación y persistencia PostgreSQL.
- Issue: pendiente.
- Pull request: pendiente.
- Estado: pendiente.
