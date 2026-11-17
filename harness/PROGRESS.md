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

## 2026-11-17 — Issues y rama de integración

- Se publicó la rama `devops` y se crearon los 20 issues enlazados desde el catálogo y la matriz.
- Las funcionalidades siguen pendientes; no se ha creado ninguna etiqueta de versión.

## 2026-11-17 — Banco de pruebas

- Se documentaron casos identificados para las funcionalidades de los dos backends y del frontend; infraestructura se verifica según sus propios specs.
- Los casos están planificados: no hay pruebas de aplicación ejecutadas ni reportes de cobertura todavía.
- Pendiente: implementar las pruebas junto con cada funcionalidad y registrar sus resultados y evidencia en el PR y la matriz.

## 2026-11-17 — Identificadores y pruebas por spec

- Se simplificaron los identificadores redundantes de cursos y estudiantes a `CUR-01` y `EST-01` (y sus secuencias).
- Cada spec de aplicación enumera los IDs de sus pruebas planificadas y enlaza el banco; aún no se han ejecutado.

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
