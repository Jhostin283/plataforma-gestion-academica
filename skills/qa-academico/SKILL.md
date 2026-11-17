---
name: qa-academico
description: Diseñar y ejecutar pruebas de calidad, contratos y cobertura para la plataforma académica.
---

# QA académico

## Stack tecnológico

- **Backend Java, propuesto:** JUnit 5 y herramientas de prueba de Spring; JaCoCo para cobertura de líneas.
- **Backend Python, propuesto:** pytest y pytest-cov o coverage.py para cobertura de líneas.
- **Persistencia:** PostgreSQL aislado para pruebas de integración, restricciones y concurrencia. La herramienta para crear el entorno de prueba está pendiente de selección.
- **Contratos:** OpenAPI para comprobar solicitudes, respuestas y compatibilidad entre servicios; la biblioteca de pruebas de contrato está pendiente de selección.
- **Frontend:** pruebas de componentes y de navegador con herramientas por seleccionar según el stack React implementado.
- **Automatización:** GitHub Actions ejecutará pruebas y conservará reportes. La cobertura de líneas debe superar el 80 % por separado en cada backend.
- **Portabilidad:** toda prueba de aplicación debe poder ejecutarse en GitHub Actions de forma reproducible. Evita dependencias exclusivas de Windows o pasos manuales dentro de la suite de CI; documenta cualquier requisito de base de datos y configura la automatización en su issue correspondiente.

Las herramientas propuestas aún no están instaladas ni fijadas a una versión; documenta la elección al implementarlas.

Parte de los criterios de aceptación y contratos de los specs pertinentes; registra qué se probó, el resultado y las limitaciones.

- Comprueba por separado que cada servicio supere el 80 % de cobertura de líneas; exactamente 80 % no cumple. Conserva los reportes como evidencia y asegura que CI falle al incumplir el umbral.
- Prioriza pruebas significativas de reglas de negocio, API y persistencia. Verifica contratos OpenAPI e integración entre servicios.
- Para matrículas prueba capacidad bajo concurrencia, duplicados, reintentos idempotentes, fallos, compensación y reconciliación según los specs implementados.
- Verifica los flujos de usuario y estados de error del frontend. No marques tareas como completadas sin evidencia de sus criterios de aceptación.

## Estrategia de pruebas

- Distribuye las pruebas entre unitarias de reglas, integración de API y base de datos, contratos y un conjunto pequeño de recorridos completos. No uses la cobertura como sustituto de las aserciones sobre comportamiento.
- Para Java considera JUnit 5, pruebas Spring y JaCoCo; para Python usa pytest y coverage.py o pytest-cov. Ajusta las herramientas a las dependencias reales del proyecto y configura ambos umbrales de forma estrictamente superior al 80 %.
- Ejecuta pruebas de persistencia y concurrencia sobre PostgreSQL real y aislado cuando la funcionalidad dependa de sus transacciones o restricciones. Evita que los resultados dependan del orden de ejecución.
- Verifica campos obligatorios, límites, códigos HTTP y esquemas OpenAPI; cubre compatibilidad entre el cliente del servicio de estudiantes y el proveedor de cursos.
- Simula pérdida de respuesta tras una reserva exitosa, fallos durante la compensación y reintentos duplicados. Comprueba que la recuperación no genere plazas perdidos ni reservas duplicadas.
- Publica resultados legibles y reportes de cobertura como artefactos de CI. Si una prueba requiere infraestructura no disponible, informa qué quedó sin verificar.

## Buenas prácticas

- **Trazabilidad:** vincula cada prueba con el requisito o criterio de aceptación que verifica.
- **Banco de pruebas:** consulta `pruebas/BANCO-DE-PRUEBAS.md` por ID de funcionalidad. Implementa los casos relevantes y registra por ID el resultado real, comando y evidencia; amplía el banco cuando falten escenarios.
- **Claridad:** organiza cada prueba en preparación, ejecución y verificación. Usa nombres que describan el comportamiento esperado.
- **Independencia:** utiliza datos aislados. Las pruebas deben poder ejecutarse en cualquier orden sin depender de resultados anteriores.
- **Casos relevantes:** cubre operaciones exitosas, validaciones, límites y errores. Prioriza los riesgos de matrículas y plazas.
- **Pruebas reales:** verifica las reglas de persistencia y concurrencia contra PostgreSQL cuando dependan de su comportamiento.
- **Cobertura:** mide por separado cada servicio. Exige más del 80 % de líneas y conserva los reportes de CI.
- **Evidencia:** registra comandos, resultados y limitaciones. Distingue una prueba que falló de una que no pudo ejecutarse.

## Impedimentos
- No reduzcas el umbral, excluyas código sin justificar ni combines coberturas de ambos servicios para superar artificialmente el objetivo. Documenta cualquier exclusión justificada.
- No dependas de credenciales reales ni modifiques datos productivos para probar. Usa datos sintéticos y entornos aislados.
- No ocultes pruebas fallidas ni marques como aprobadas verificaciones que no se ejecutaron. Los dobles no reemplazan las pruebas reales de integración y concurrencia cuando estas son necesarias.
- No contabilices como cobertura JaCoCo la ejecución de procesos externos que no estén instrumentados; informa esos resultados por separado.
