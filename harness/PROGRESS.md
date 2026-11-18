# Registro de progreso

## 2026-11-17 — Inicialización del harness

- Estado: preparación inicial; funcionalidades pendientes.
- Cambios: catálogo de funcionalidades, specs por componente, plantillas de issue y PR, comprobaciones locales y guía de trabajo.
- Pruebas: consultar el resultado real de `python harness/check.py` en la sesión de inicialización. No hay todavía pruebas de aplicación ni reportes de cobertura.
- Bloqueos iniciales: en ese momento no había repositorio Git ni remoto; no se crearon issues o PR.
- Siguiente paso inicial: preparar el repositorio y crear el primer issue a partir de una tarea sin dependencias.

## 2026-11-17 — Ampliación de especificaciones

- Se documentó la arquitectura de aplicación y despliegue; se separaron los specs por funcionalidad y se creó su índice.
- Cada spec funcional tiene una tarea independiente; puede tener su propio issue y PR.
- Pendiente: implementar las funcionalidades y recopilar evidencia de cada spec.

## 2026-11-17 — Formato y seguimiento por funcionalidad

- Se sustituyeron los IDs con sufijos de letras por identificadores funcionales, como `EST-MAT-01` y `CUR-PLZ-01`.
- Se ampliaron los specs con objetivo, precondiciones, flujo, casos alternativos, criterios de aceptación y trazabilidad.
- Cada spec tiene ahora su propia tarea en el catálogo y fila en la matriz. Los IDs anteriores no corresponden a issues creados.

## 2026-11-17 — Contratos y limpieza

- Se retiraron los resúmenes redundantes que habían quedado de la agrupación anterior y se actualizaron referencias.
- Se añadieron endpoints propuestos, respuestas, errores y criterios verificables a los specs de backend. Se documentaron convenciones compartidas de API.
- Los contratos son propuestas sin implementar. La generación OpenAPI, las pruebas y los despliegues continúan pendientes.

## 2026-11-17 — Versionado de API

- Se adoptó el prefijo `/api/v1/` para las rutas públicas e internas propuestas; se actualizaron los specs, la arquitectura y la guía de backend.

## 2026-11-17 — Arquitectura de los servicios

- Se definió arquitectura hexagonal para cada backend y se documentaron núcleo, puertos, adaptadores y dirección de dependencias.

## 2026-11-17 — Repositorio inicial

- Se preparó la rama `main` y el commit inicial con la estructura, los specs y el harness.
- Pendiente: configurar el remoto y autenticar GitHub para subirlo; después crear el primer issue.

## 2026-11-17 — Reinicio de la estrategia

- Se restableció la base y se preparó una estrategia centrada exclusivamente en pruebas unitarias; no se ha implementado ningún servicio en esta base.
- Se sincronizaron los identificadores simplificados, el banco de pruebas y los enlaces a los 20 issues existentes. Todos los estados siguen pendientes.
- Las pruebas unitarias no demuestran persistencia, migraciones ni concurrencia reales; esos criterios permanecen en los specs y no se deben declarar verificados mediante mocks.
- Siguiente paso: sincronizar `devops` con esta base y empezar la tarea correctiva acordada.

## 2026-11-17 — CUR-01 sobre devops corregido

- Se reconstruyó la rama de CUR-01 desde la base corregida de devops, conservando la implementación y únicamente pruebas unitarias.
- `mvn -o test jacoco:report`: 12 pruebas unitarias aprobadas, que incluyen los 7 casos del banco vinculados a CUR-01.
- El issue sigue en progreso; el PR #24 está en borrador.

## 2026-11-17 — Revisión de documentación

- Se actualizaron referencias de pruebas en el README y los specs para reflejar la estrategia unitaria acordada.
- Se corrigió el flujo del harness para consultar el estado real de cada componente.

## 2026-11-17 — CUR-02 en progreso

- Se implementaron búsqueda, paginación y casos unitarios asociados. El cálculo de plazas depende de CUR-PLZ-01.
- Siguiente paso: completar las funcionalidades dependientes y su revisión.

## 2026-11-17 — CUR-03 en progreso

- Se implementó la actualización de los datos del curso y casos unitarios del caso de uso.
- El control de capacidad con reservas queda vinculado a CUR-PLZ-01.

## 2026-11-17 — CUR-04 en progreso

- Se implementó el cambio de estado del curso y casos unitarios asociados.
- La restricción de reservas de cursos inactivos depende de CUR-PLZ-01.

## Formato para próximas sesiones

```text
Fecha:
Tarea y spec:
Issue / PR:
Cambios:
Pruebas y resultado:
Evidencia:
Bloqueos:
Siguiente paso:
```
