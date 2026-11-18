# WEB-EST-01: Gestionar estudiantes desde el frontend

## Objetivo
Gestionar estudiantes desde el frontend para la plataforma de gestión académica.

## Precondiciones
- La API de estudiantes y el frontend están disponibles.

## Flujo principal
1. Listar o buscar estudiantes.
2. Registrar, editar o cambiar su estado.
3. Mostrar la respuesta confirmada.

## Casos alternativos
- Datos inválidos: mostrar errores por campo.
- API no disponible: comunicar el fallo.

## Criterios de aceptación
- Las operaciones de estudiantes funcionan desde la interfaz.
- Se muestran carga, errores y resultados vacíos.
- La interfaz es adaptable y accesible.

## Trazabilidad
- Requisito: WEB-EST-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: Interfaz, API y accesibilidad.
- Issue: [15](https://github.com/Jhostin283/plataforma-gestion-academica/issues/15).
- Pull request: [#38](https://github.com/Jhostin283/plataforma-gestion-academica/pull/38) (borrador).
- Estado: en progreso; pantalla y casos preparados, validación pendiente.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-WEB-EST-01-01:** Registrar, buscar, editar y cambiar estado de un estudiante.
- **PR-WEB-EST-01-02:** Enviar correo inválido o recibir documento duplicado.
- **PR-WEB-EST-01-03:** Simular carga, vacío y fallo de servicio.
