# WEB-MAT-01: Gestionar matrículas desde el frontend

## Objetivo
Gestionar matrículas desde el frontend para la plataforma de gestión académica.

## Precondiciones
- Las API y el frontend están disponibles.

## Flujo principal
1. Seleccionar estudiante y curso.
2. Consultar plazas y solicitar matrícula.
3. Consultar historial o cancelar una matrícula.

## Casos alternativos
- Duplicado o curso lleno: mostrar el motivo.
- Respuesta incierta: mostrar estado pendiente sin afirmar éxito.

## Criterios de aceptación
- Es posible matricular, consultar y cancelar.
- La interfaz comunica errores y estados correctamente.
- El flujo funciona con teclado y en móvil.

## Trazabilidad
- Requisito: WEB-MAT-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: componentes de interfaz, estados y accesibilidad.
- Issue: [16](https://github.com/Jhostin283/plataforma-gestion-academica/issues/16).
- Pull request: pendiente.
- Estado: pendiente.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-WEB-MAT-01-01:** Seleccionar estudiante y curso con plazas; enviar matrícula.
- **PR-WEB-MAT-01-02:** Recibir `202` y después consultar el estado.
- **PR-WEB-MAT-01-03:** Recibir duplicado, curso lleno o fallo de API.
- **PR-WEB-MAT-01-04:** Consultar historial y cancelar una matrícula.
