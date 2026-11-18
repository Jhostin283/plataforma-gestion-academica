# WEB-CUR-01: Gestionar cursos desde el frontend

## Objetivo
Gestionar cursos desde el frontend para la plataforma de gestión académica.

## Precondiciones
- La API de cursos y el frontend están disponibles.

## Flujo principal
1. Listar o buscar cursos.
2. Registrar, editar o cambiar el estado.
3. Mostrar la respuesta confirmada del servidor.

## Casos alternativos
- Datos inválidos: mostrar errores por campo.
- API no disponible: comunicar fallo conservando entradas recuperables.

## Criterios de aceptación
- Las operaciones de cursos funcionan desde la interfaz.
- Carga, errores y resultados vacíos son comprensibles.
- La interfaz es adaptable y accesible.

## Trazabilidad
- Requisito: WEB-CUR-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: Interfaz, API y accesibilidad.
- Issue: [14](https://github.com/Jhostin283/plataforma-gestion-academica/issues/14).
- Pull request: [#37](https://github.com/Jhostin283/plataforma-gestion-academica/pull/37) (borrador).
- Estado: en progreso; gestión preparada, validación pendiente.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-WEB-CUR-01-01:** Registrar, buscar, editar y cambiar estado con API simulada.
- **PR-WEB-CUR-01-02:** Enviar campo inválido o recibir conflicto por duplicado.
- **PR-WEB-CUR-01-03:** Simular carga, lista vacía y fallo de API.
