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

## 2026-11-17 — EST-01 en progreso

- Se preparó el servicio de estudiantes con registro, consulta básica, migración y casos unitarios.

## 2026-11-17 — EST-02 bloqueada por dependencia

- Tarea y spec: EST-02, `specs/servicio-estudiantes/02-consultar-estudiantes.md`; issue #7.
- EST-01 continúa `in_progress` y su PR #29 está registrado como borrador; aún faltan pruebas de persistencia y concurrencia reales para sus criterios de aceptación. No se ha integrado en `devops`.
- Comprobaciones: `python harness/check.py` y `python harness/init.py` finalizaron correctamente. Tras instalar dependencias, `uv run --directory servicios/estudiantes --extra test pytest --cov=app --cov-report=term-missing` ejecutó 3 pruebas unitarias satisfactorias con 87 % de cobertura; no demuestra persistencia ni concurrencia reales. El usuario indicó después que las pruebas restantes se harán al final. GitHub CLI informó que no hay sesión autenticada.
- Bloqueos: validar EST-01 y resolver su integración respetando el acuerdo de mantener los PR en borrador hasta la validación final; acceso autenticado a GitHub para consultar y operar sobre el PR.
- Siguiente paso: resolver la integración de la base y el acceso a GitHub; ejecutar la validación pendiente al final según la indicación del usuario. La puerta de cobertura en CI para estudiantes aún no está configurada.

## 2026-11-17 — EST-02 en progreso

- Issue #7 y spec `specs/servicio-estudiantes/02-consultar-estudiantes.md`: se prepararon listado, búsqueda por nombres, apellidos o documento, paginación y documentación HTTP; el detalle proviene de EST-01.
- Se prepararon casos unitarios para filtros, resultados vacíos, paginación e identificador inválido. No se ejecutaron por indicación del usuario de realizar las pruebas al final; los criterios siguen sin aprobarse.
- La rama local `feature/est-02-consultar-estudiantes` parte provisionalmente de EST-01 porque esa base aún no está integrada en `devops`. El PR de EST-01 continúa registrado como borrador y GitHub CLI no tiene sesión autenticada. Se creó el PR #30 en borrador.
- Siguiente paso: validar al final EST-01 y EST-02, integrar la base según el acuerdo de PR en borrador y preparar el PR de EST-02 con su destino y autenticación disponibles.

## 2026-11-17 — EST-03 en progreso

- Issue #8 y spec `specs/servicio-estudiantes/03-actualizar-estudiante.md`: se preparó PUT para actualizar datos conservando el estado, con manejo de documento duplicado y estudiante inexistente.
- Se prepararon casos unitarios para actualización y rechazos. No se ejecutaron por indicación del usuario; persistencia, concurrencia y cobertura quedan pendientes de validación final.
- La rama local `feature/est-03-actualizar-estudiante` parte provisionalmente de EST-02 y EST-01; ambas siguen pendientes de validación e integración. Se creó el PR #31 en borrador tras autenticar GitHub CLI.
- Siguiente paso: revisar y publicar las ramas cuando exista acceso; ejecutar los criterios al final y mantener las tareas sin aprobar hasta entonces.

## 2026-11-17 — EST-04 en progreso

- Issue #9 y spec `specs/servicio-estudiantes/04-cambiar-estado.md`: se preparó el cambio de estado y casos unitarios para repetición, valores inválidos e ID inválido o inexistente.
- Las pruebas no se ejecutaron por indicación del usuario; persistencia, conservación del historial y cobertura siguen pendientes. La regla de rechazo de matrícula para estudiantes inactivos depende de EST-MAT-01, aún pendiente.
- PR #32 en borrador hacia `devops`; la rama parte provisionalmente de EST-03 y sus dependencias. La integración se posterga hasta la validación final.

## 2026-11-17 — EST-MAT-01 en progreso

- Issue #10, PR #33 en borrador y spec `specs/servicio-estudiantes/05-crear-matricula.md`: se preparó el puerto y cliente HTTP de reservas, con clave de operación, timeout y distinción entre rechazo y resultado incierto.
- Se prepararon casos para el adaptador, sin ejecutarlos por indicación del usuario. El cliente aún no está conectado al caso de uso ni constituye una matrícula operativa.
- Se prepararon modelo, migración y repositorio de operaciones durables, con restricciones de clave y matrícula vigente y bloqueo del estudiante. No están conectados al caso de uso ni se han probado.
- Se conectaron el caso de uso y endpoints de creación y detalle: la operación incierta queda pendiente y admite reintento con la misma clave. Se prepararon casos unitarios, sin ejecutarlos.
- Se preparó un proceso ejecutable para reintentar operaciones pendientes con la clave original y conservar el estado pendiente ante un resultado incierto al actualizar. No se ejecutó ni validó.
- Se prepararon transición durable de compensación, liberación idempotente y reconciliación periódica; se añadieron casos unitarios sin ejecutar.
- Pendiente: validar transiciones, compensación, concurrencia y persistencia reales y cobertura; la dependencia CUR-PLZ-01 sigue en borrador en el PR #28. Ningún criterio se declara aprobado.

## 2026-11-17 — EST-MAT-02 en progreso

- Issue #11, PR #34 en borrador y spec `specs/servicio-estudiantes/06-consultar-matriculas.md`: se preparó el listado con filtros por estudiante, curso y estado, paginación y detalle. La compensación pendiente se presenta como PENDIENTE.
- Se prepararon casos de filtro, vacío, detalle y errores, sin ejecutarlos por indicación del usuario. Persistencia, historial y criterios de aceptación siguen pendientes de validación.
- La rama parte provisionalmente de EST-MAT-01, aún en borrador; no se ha integrado a `devops`.

## 2026-11-17 — EST-MAT-03 en progreso

- Issue #12, PR #35 en borrador y spec `specs/servicio-estudiantes/07-cancelar-matricula.md`: se preparó la transición durable de cancelación, liberación idempotente y reintento desde el proceso de reconciliación.
- Se prepararon casos unitarios de cancelación y fallos, sin ejecutarlos por indicación del usuario. Persistencia, historial, idempotencia y recuperación siguen pendientes de validación.
- La rama parte provisionalmente de EST-MAT-02 y sus dependencias, aún en borrador.

## 2026-11-17 — WEB-PAN-01 en progreso

- Issue #13, PR #36 en borrador y spec `specs/frontend/01-panel.md`: se preparó una aplicación React y un panel con indicadores de cursos, estudiantes y matrículas activos, estados de carga, vacío y error, y reintento por sección.
- Se prepararon casos unitarios sin ejecutarlos por indicación del usuario. Compilación, revisión visual, accesibilidad e integración siguen pendientes. Las pantallas y accesos funcionales de gestión aún no existen; el conteo paginado puede variar ante cambios simultáneos.
- La rama parte provisionalmente de la cadena de estudiantes y matrículas; sus dependencias y las ramas de cursos siguen en borrador y no están integradas en `devops`.

## 2026-11-17 — WEB-CUR-01 en progreso

- Issue #14, PR #37 en borrador y spec `specs/frontend/02-cursos.md`: se prepararon navegación, búsqueda y listado de cursos, formulario de registro y edición y cambio de estado.
- Se preparó un caso unitario sin ejecutar por indicación del usuario. Faltan verificar los errores por campo y ejecutar pruebas de edición y cambio de estado, compilación, accesibilidad e integración; no se declaran criterios aprobados.
- La rama parte del panel y la cadena provisional de estudiantes; las API de cursos siguen en ramas en borrador no integradas en `devops`.

## 2026-11-17 — WEB-EST-01 en progreso

- Issue #15, PR #38 en borrador y spec `specs/frontend/03-estudiantes.md`: se preparó búsqueda y listado de estudiantes, registro, edición y cambio de estado, con estados de carga y error.
- Se preparó un caso unitario sin ejecutarlo por indicación del usuario. Faltan pruebas de edición, estado y errores, compilación, accesibilidad e integración; no se declaran criterios aprobados.
- La rama parte de los PR previos de frontend y estudiantes, aún en borrador.

## 2026-11-17 — WEB-MAT-01 en progreso

- Issue #16, PR #39 en borrador y spec `specs/frontend/04-matriculas.md`: se prepararon selección de estudiantes y cursos, consulta de plazas, creación con clave estable, historial y cancelación. Las respuestas pendientes se muestran como tales.
- Se preparó un caso para respuesta 202 sin ejecutarlo por indicación del usuario. Faltan pruebas de creación confirmada, errores y cancelación, compilación, accesibilidad e integración; no se declaran criterios aprobados.
- La rama parte de los PR previos de frontend y matrículas, aún en borrador.

## 2026-11-17 — INF-LOC-01 en progreso

- Issue #17, PR #40 en borrador y spec `specs/infraestructura/01-contenedores.md`: se prepararon Dockerfiles, Compose con dos bases y volúmenes separados, migración de estudiantes y proxy de frontend; no se incluyeron credenciales reales.
- No se construyeron ni ejecutaron imágenes ni se comprobaron migraciones o persistencia por indicación de reservar las pruebas para el final. El código de cursos se encuentra en una cadena separada aún en borrador, por lo que esta rama no dispone del contexto de construcción completo.
- Pendiente: integrar dependencias en una revisión y validar el entorno completo; no se declaran criterios aprobados.

## 2026-11-17 — INF-CI-01 en progreso

- Issue #18, PR #41 en borrador y spec `specs/infraestructura/02-github-actions-ecr.md`: se prepararon workflows manuales para pruebas y cobertura por servicio, reportes y publicación en ECR mediante OIDC con imágenes etiquetadas por commit.
- No se ejecutaron los workflows ni se crearon recursos AWS, por indicación del usuario y falta de configuración autorizada. La rama de cursos aún no está integrada.
- Pendiente: validación final del workflow y umbrales, habilitar el disparador de PR cuando se acuerde y preparar el mecanismo que proponga cambios de imágenes en Git. No se declaran criterios aprobados.

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
