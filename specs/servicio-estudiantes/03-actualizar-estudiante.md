# EST-03: Actualizar estudiante

## Objetivo
Actualizar estudiante para la plataforma de gestión académica.

## Precondiciones
- El estudiante existe y se proporcionan los cambios.

## Flujo principal
1. Consultar el estudiante.
2. Validar documento, correo y otros campos.
3. Guardar cambios y responder.

## Casos alternativos
- Documento duplicado o datos inválidos: rechazar.
- Estudiante inexistente: responder con error.

## Criterios de aceptación
- Los cambios válidos persisten.
- Las entradas inválidas no alteran el registro.
- No se exponen datos personales innecesarios.

## Endpoint propuesto

`PUT /api/v1/estudiantes/{estudianteId}` recibe `documento`, `nombres`, `apellidos` y `correo` y devuelve `200` con los datos actualizados. El estado se gestiona por separado.

- `400`: JSON o UUID inválido. `404`: estudiante inexistente. `409`: documento ya utilizado. `422`: cuerpo inválido.

## Criterios adicionales de aceptación

- Rechazar un cambio no modifica parcialmente los datos. La unicidad del documento se mantiene ante solicitudes simultáneas.
- El contrato y las pruebas cubren cada respuesta; los errores no muestran trazas internas.

## Trazabilidad
- Requisito: EST-03
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: API, unicidad y persistencia.
- Issue: [8](https://github.com/Jhostin283/plataforma-gestion-academica/issues/8).
- Pull request: pendiente.
- Estado: pendiente.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-EST-03-03:** Enviar correo inválido o cuerpo incompleto.
- **PR-EST-03-04:** Actualizar ID inexistente o inválido.
- **PR-EST-03-06:** Actualizar un estudiante con repositorio simulado.
