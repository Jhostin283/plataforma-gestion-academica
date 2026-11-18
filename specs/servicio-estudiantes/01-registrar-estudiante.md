# EST-01: Registrar estudiante

## Objetivo
Registrar estudiante para la plataforma de gestión académica.

## Precondiciones
- El administrador dispone de documento, nombres, apellidos y correo.

## Flujo principal
1. Ingresar los datos.
2. Validar campos, correo y documento único.
3. Guardar y confirmar el registro.

## Casos alternativos
- Documento duplicado o datos inválidos: rechazar.
- Fallo de almacenamiento: informar sin confirmar.

## Criterios de aceptación
- El estudiante válido se registra y se puede consultar.
- Se rechazan duplicados y valores inválidos.
- Los datos persisten en la base propia.

## Endpoint propuesto

`POST /api/v1/estudiantes` recibe `documento`, `nombres`, `apellidos` y `correo`; crea un estudiante `ACTIVO`. Devuelve `201` con `id`, datos creados y `Location` al detalle.

- `400`: JSON malformado. `422`: campos faltantes, vacíos o correo inválido. `409`: documento duplicado.

## Criterios adicionales de aceptación

- Solicitudes concurrentes con el mismo documento crean como máximo un estudiante.
- Un rechazo no registra datos parciales; los errores siguen las [convenciones](../convenciones-api.md).

## Trazabilidad
- Requisito: EST-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, validación y PostgreSQL.
- Issue: [6](https://github.com/Jhostin283/plataforma-gestion-academica/issues/6).
- Pull request: [#29](https://github.com/Jhostin283/plataforma-gestion-academica/pull/29) (borrador).
- Estado: aprobado; registro, persistencia tras reinicio y cobertura CI (81,18 %) verificados.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-EST-01-02:** Omitir datos o enviar correo inválido.
- **PR-EST-01-04:** Enviar JSON malformado.
- **PR-EST-01-05:** Registrar estudiante válido con repositorio simulado.
- **PR-EST-01-06:** Simular conflicto de documento en el repositorio.
