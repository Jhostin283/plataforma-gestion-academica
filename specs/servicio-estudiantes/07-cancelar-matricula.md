# EST-MAT-03: Cancelar matrícula

## Objetivo
Cancelar matrícula para la plataforma de gestión académica.

## Precondiciones
- La matrícula existe y está activa.

## Flujo principal
1. Seleccionar la matrícula.
2. Registrar la cancelación y solicitar liberación de la reserva.
3. Confirmar el estado y conservar el historial.

## Casos alternativos
- Matrícula inexistente: informar.
- Liberación fallida o incierta: conservar estado para reintento y reconciliación.

## Criterios de aceptación
- Se conserva el historial de cancelación.
- Repetir la operación no libera dos veces.
- Un fallo puede recuperarse sin perder el seguimiento.

## Endpoint propuesto

`POST /api/v1/matriculas/{matriculaId}/cancelacion` solicita cancelar una matrícula y liberar su reserva.

- `200`: estado `CANCELADA`, incluida una repetición ya finalizada.
- `202`: estado `CANCELACION_PENDIENTE` mientras se confirma o reconcilia la liberación; `Location` permite consultar el detalle.
- `400`: UUID inválido. `404`: matrícula inexistente. `409`: transición incompatible, por ejemplo una matrícula `FALLIDA` sin reserva activa.

## Criterios adicionales de aceptación

- La cancelación conserva el historial y nunca libera más de una vez; los reintentos devuelven el estado existente.
- Si cursos no responde tras iniciarse la liberación, se conserva el estado pendiente y se reconcilia antes de declarar `CANCELADA`.
- La prueba verifica fallos antes y después de la liberación y el resultado final tras reintentar.

## Trazabilidad
- Requisito: EST-MAT-03
- Contrato relacionado: CUR-PLZ-01; specs/comunicacion-entre-servicios.md
- Pruebas: Integración, idempotencia y recuperación.
- Issue: [12](https://github.com/Jhostin283/plataforma-gestion-academica/issues/12).
- Pull request: pendiente.
- Estado: pendiente.

## Pruebas relacionadas

Casos planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md); aún no ejecutados:

- **PR-EST-MAT-03-01:** Cancelar una matrícula activa con reserva.
- **PR-EST-MAT-03-02:** Repetir cancelación finalizada.
- **PR-EST-MAT-03-03:** Cancelar matrícula inexistente, UUID inválido o estado incompatible.
- **PR-EST-MAT-03-04:** Perder respuesta o fallar al liberar la reserva.
