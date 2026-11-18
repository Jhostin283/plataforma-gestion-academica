# WEB-PAN-01: Consultar panel principal

## Objetivo
Consultar panel principal para la plataforma de gestión académica.

## Precondiciones
- Las API requeridas están disponibles o su estado puede notificarse.

## Flujo principal
1. Abrir el panel.
2. Consultar indicadores reales.
3. Mostrar accesos e información disponible.

## Casos alternativos
- Una API falla: mostrar error para su sección.
- Sin registros: mostrar estado vacío.

## Criterios de aceptación
- Los indicadores corresponden a datos reales.
- El panel es usable en móvil y con teclado.

## Trazabilidad
- Requisito: WEB-PAN-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: componentes de interfaz, estados y accesibilidad.
- Issue: [13](https://github.com/Jhostin283/plataforma-gestion-academica/issues/13).
- Pull request: [#36](https://github.com/Jhostin283/plataforma-gestion-academica/pull/36) (borrador).
- Estado: en progreso; panel preparado, accesos a pantallas y validación pendientes.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-WEB-PAN-01-01:** Abrir panel con datos conocidos en las API.
- **PR-WEB-PAN-01-02:** Simular API caída o sin registros.
