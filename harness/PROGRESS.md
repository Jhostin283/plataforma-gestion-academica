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

## 2026-11-17 — CUR-01 en progreso

- Se inició el servicio Java de cursos y se organizó en dominio, aplicación, puertos y adaptadores de entrada y salida. Se actualizó el diagrama de arquitectura.
- `mvn verify`: cinco pruebas pasaron usando H2 en memoria; incluyen creación, duplicado secuencial, validación, JSON inválido y presencia del endpoint en OpenAPI. H2 no verifica el comportamiento de PostgreSQL ni la migración real.
- Pendiente: verificar migración y persistencia con PostgreSQL, duplicados concurrentes y comportamiento tras reinicio; completar la consulta de detalle de CUR-02. Docker no respondió durante la verificación.
- Estado: `in_progress`. No se cierran el issue ni el PR hasta reunir las evidencias pendientes.

## 2026-11-17 — Pruebas de CUR-01 con PostgreSQL

- `mvn clean verify` con PostgreSQL 16 temporal: siete pruebas pasaron, incluidas migración Flyway, registro y solicitudes simultáneas con código duplicado. Las otras pruebas usan H2.
- JaCoCo reportó 28 de 30 líneas cubiertas (93,33 %) en el código actual del servicio; es una medición inicial, no la cobertura de funcionalidades aún no implementadas.
- Al reiniciar el contenedor PostgreSQL, los dos registros de prueba permanecieron. Esto comprueba la persistencia de la base tras su reinicio, no el reinicio completo del servicio.
- Pendiente para CUR-01: consulta de detalle, prevista en CUR-02, y comprobación de reinicio completo de la aplicación. Estado `in_progress`.

## 2026-11-17 — CUR-01: consulta y verificación de reinicio

- Se agregó consulta básica por ID para verificar el curso registrado, con respuestas `200`, `400` y `404`; el listado y la disponibilidad siguen en CUR-02 y CUR-PLZ-01.
- `mvn -o clean verify`: siete pruebas H2 pasaron; dos pruebas PostgreSQL se omitieron porque el usuario ejecutará esa verificación. JaCoCo indicó 38/40 líneas (95 %) para esta ejecución local.
- Se preparó `servicios/cursos/scripts/verificar-reinicio.ps1` y se verificó su sintaxis, pero no se ejecutó: requiere la base de pruebas iniciada por el usuario.
- CUR-01 permanece `in_progress` hasta recibir evidencia de la suite PostgreSQL y del script de reinicio en la versión actual.

## 2026-11-17 — Evidencia de reinicio y error de persistencia

- El usuario ejecutó el script de reinicio y confirmó la consulta del curso `2fc30ee2-6b7e-47cd-959e-5fdb9abfb47d` tras reiniciar. Se verificaron reportes locales: 9 pruebas aprobadas, sin omisiones, con 95 % de cobertura para la versión anterior.
- Se agregó manejo de fallos de persistencia con `503` y se documentó la comparación del código en OpenAPI. La prueba local del controlador pasó; en esa ejecución las dos pruebas PostgreSQL se omitieron.
- `mvn clean verify` no pudo limpiar el JAR por un proceso Java que lo mantiene abierto. Se ajustó la espera de cierre del script. Pendiente volver a ejecutar la suite completa con PostgreSQL para la versión final.

## 2026-11-17 — Portabilidad de pruebas

- Se adaptó el script de reinicio para evitar el uso de `WindowStyle` en Linux y se documentó su ejecución con `pwsh`; su validación en GitHub Actions está pendiente.
- La ejecución de reinicio se informa por separado de JaCoCo, ya que inicia procesos Java independientes sin instrumentación. El workflow y la política de cobertura corresponden a INF-CI-01.

## 2026-11-17 — Estrategia de pruebas unitarias

- Por solicitud del usuario, la suite se limita a pruebas unitarias con dependencias simuladas; se retiraron los casos de integración de la suite y del banco actual. Los criterios funcionales de los specs se conservan.
- Para CUR-01 se ejecutaron 12 pruebas unitarias sin PostgreSQL ni contenedores: 39 de 42 líneas cubiertas (92,86 %).
- Las pruebas unitarias no demuestran persistencia ni concurrencia reales. La evidencia anterior se conserva como historial y CUR-01 sigue en progreso hasta revisar todos los criterios.

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
