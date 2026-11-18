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

## 2026-11-17 — CUR-PLZ-01 en progreso

- Se preparó la reserva, consulta y liberación de plazas; la capacidad se controla mediante bloqueo del curso y registro de reservas.
- Se incorporaron casos unitarios para las reglas de reserva y actualización de capacidad.

## 2026-11-17 — Organización de arquitectura hexagonal

- Se organizaron las entidades de dominio, los casos de uso, modelos y excepciones en paquetes explícitos; los adaptadores de entrada y salida permanecen separados.
- Se actualizaron las referencias del código y la documentación de arquitectura.

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

## 2026-11-17 — INF-K8S-01 en progreso

- Issue #19, PR #42 en borrador y spec `specs/infraestructura/03-kubernetes.md`: se prepararon manifiestos Kustomize para aplicaciones, servicios, Ingress y migración controlada, con secretos externos e imágenes de marcador no desplegables.
- No se aplicaron ni validaron manifiestos o recursos. Faltan imágenes reales, controlador y TLS, bases y secretos configurados, y verificar rutas, persistencia y salud. La sonda de estudiantes no verifica la disponibilidad de la base.
- No se declaran criterios aprobados ni se han creado recursos AWS.

## 2026-11-17 — INF-CD-01 bloqueado

- Issue #20, PR #43 en borrador y spec `specs/infraestructura/04-aws-argocd.md`: se prepararon una aplicación Argo CD de sincronización manual y una guía de prerrequisitos, validación, reversión y costos.
- Bloqueo: no se ha proporcionado un entorno EKS/Argo CD autorizado; las imágenes y dependencias no están verificadas. No se han creado recursos, aplicado manifiestos, sincronizado ni probado el despliegue.
- Siguiente paso: completar y validar las dependencias y preparar el entorno autorizado antes de verificar el despliegue.

## 2026-11-17 — Validación de cobertura en CI

- INF-CI-01, issue #18: se preparó la ejecución del workflow de aplicaciones en PR hacia `developer` para obtener resultados reales y reportes por servicio.
- Ejecución [CI](https://github.com/Jhostin283/plataforma-gestion-academica/actions/runs/44208787841) sobre `58e1099`: cursos 24 pruebas satisfactorias y 81/120 líneas (67,50 %); estudiantes 18 pruebas satisfactorias y 413/611 líneas (67,59 %); frontend 5 pruebas satisfactorias y compilación correcta. La puerta de cobertura falló para ambos backends.
- Pendiente: aumentar cobertura con pruebas significativas y verificar criterios funcionales, persistencia, concurrencia, accesibilidad y despliegue. La cobertura no demuestra por sí sola estos criterios; ninguna funcionalidad se marca como aprobada.

## 2026-11-17 — CUR-PLZ-01: casos adicionales preparados

- Issue #5: se prepararon pruebas aisladas para endpoints de reserva, reintentos, consulta, liberación y errores del repositorio.
- No se ejecutaron estos nuevos casos por indicación del usuario; la cobertura posterior y los criterios reales de concurrencia y persistencia siguen pendientes. La última cobertura observada de cursos fue 67,50 %.

## 2026-11-17 — EST-01: casos adicionales preparados

- Issue #6: se prepararon pruebas aisladas del adaptador de registro, consulta y tratamiento de conflictos de integridad.
- No se ejecutaron por indicación del usuario; no demuestran persistencia PostgreSQL real. La última cobertura observada del servicio fue 67,59 %.

## 2026-11-17 — EST-MAT-01: casos adicionales preparados

- Issue #10: se prepararon pruebas aisladas para persistencia de operaciones, repetición de claves, conflictos, consulta y transiciones.
- No se ejecutaron por indicación del usuario; no demuestran concurrencia ni persistencia PostgreSQL reales. La última cobertura observada del servicio fue 67,59 %.

## 2026-11-17 — CUR-03: casos adicionales preparados

- Issue #3: se prepararon pruebas aisladas del adaptador para actualización válida, curso inexistente y conflictos de código o integridad.
- No se ejecutaron por indicación del usuario; la cobertura posterior y la persistencia real siguen pendientes de validación.

## 2026-11-17 — Resultado de cobertura del PR #45

- [Ejecución CI](https://github.com/Jhostin283/plataforma-gestion-academica/actions/runs/44210233013): estudiantes ejecutó 31 pruebas satisfactoriamente y obtuvo 496/611 líneas (81,18 %); frontend pasó sus pruebas y compilación.
- Cursos ejecutó 34 pruebas con un error en la preparación de un mock de `PostgresCursoRepositoryTest.actualizarTraduceSoloConflictoDeCodigo`. La ejecución se detuvo antes de calcular su cobertura.
- Se preparó una corrección del mock en una rama nueva; aún no se ha ejecutado. Ninguna tarea se declara aprobada por estos resultados.

## 2026-11-17 — INF-LOC-01: ejecución local

- Se construyeron y arrancaron los servicios y dos bases mediante Docker Compose en un proyecto local aislado; la migración de estudiantes terminó y el frontend abrió correctamente.
- Se registraron un curso y un estudiante, se confirmó una matrícula, se repitió la solicitud con la misma clave y se canceló. Los tres registros se consultaron tras reiniciar servicios y bases. Detalle en `harness/evidencia/inf-loc-01-2026-11-17.md`.
- La instalación del frontend informó seis vulnerabilidades de dependencias (tres moderadas, una alta y dos críticas); su alcance y solución están pendientes de análisis. Persistencia bajo fallos, concurrencia, ausencia de secretos en imágenes y despliegue siguen sin comprobarse completamente.

## 2026-11-17 — Evidencia de pruebas de aplicación

- El usuario indica que probó la aplicación y que funciona; no se han proporcionado detalles de cada criterio de aceptación ni evidencia de un despliegue EKS.
- La [ejecución CI](https://github.com/Jhostin283/plataforma-gestion-academica/actions/runs/44210749208) correspondiente al PR #46 terminó correctamente: cursos 34 pruebas y 87,50 % de líneas, estudiantes 31 pruebas y 81,18 %, frontend 5 pruebas y compilación.
- Persistencia y flujo local observados en `harness/evidencia/inf-loc-01-2026-11-17.md`. La verificación de cada criterio restante y del entorno de despliegue permanece pendiente.

## 2026-11-17 — CUR-PLZ-01: verificación local

- En PostgreSQL local se verificaron 10 rondas de dos solicitudes por la última plaza: cada ronda produjo una reserva `201` y un conflicto `409`. Dos solicitudes simultáneas con la misma clave respondieron `201` y `200` para una única reserva.
- También se verificaron consulta, reintento, liberación repetida, errores y contrato OpenAPI. El primer intento de consultar OpenAPI usó una ruta incorrecta y recibió `404`; la consulta corregida respondió `200`. Resultados en `harness/evidencia/cur-plz-01-concurrencia.txt`.
- La suite de cursos del PR #46 tuvo 34 pruebas satisfactorias y 87,50 % de cobertura de líneas. CUR-PLZ-01 permanece `in_progress` mientras CUR-01, su dependencia, no esté aprobada.

## 2026-11-17 — CUR-01: fallo de persistencia detectado

- Registro local concurrente con el mismo código produjo `201` y `409`, una única fila, validación `422`, JSON malformado `400` y OpenAPI `200`.
- Al detener temporalmente la base local, el servicio devolvió `500` en vez de `503`; el registro no pudo confirmarse. La base fue restaurada. Evidencia: `harness/evidencia/cur-01-registro-local.txt`.
- CUR-01 y su dependiente CUR-PLZ-01 permanecen `in_progress` hasta corregir y verificar este comportamiento.


## 2026-11-17 — CUR-01 aprobado

- Tarea y spec: CUR-01, specs/servicio-cursos/01-registrar-curso.md; issue #1, PR #24.
- Cambios: corrección en ApiExceptionHandler.java para capturar CannotCreateTransactionException devolviendo 503 PERSISTENCIA_NO_DISPONIBLE ante fallo de conexión con la base de datos.
- Pruebas y resultado: registro concurrente (201 y 409 con código único), validaciones de campos (422), JSON malformado (400), contrato OpenAPI documentado (200), persistencia tras reinicio en Docker Compose (INF-LOC-01). Prueba con base detenida responde 503 sin crear registros espurios. Cobertura CI de cursos: 34 pruebas aprobadas y 87,50 % de líneas.
- Evidencia: harness/evidencia/cur-01-registro-local.txt, harness/evidencia/inf-loc-01-2026-11-17.md y ejecución CI 44210749208.
- Estado: pasó a passed.
- Siguiente paso: validar y aprobar CUR-PLZ-01 y las funcionalidades dependientes.


## 2026-11-17 — CUR-PLZ-01 aprobado

- Tarea y spec: CUR-PLZ-01, specs/servicio-cursos/05-reservar-liberar-plazas.md; issue #5, PR #28.
- Dependencias: CUR-01 aprobada.
- Pruebas y resultado: 10 rondas de dos solicitudes simultáneas por la última plaza produjeron 1 reserva 201 y 1 conflicto 409 por ronda; dos solicitudes simultáneas con la misma clave respondieron 201 y 200 para una única reserva; consulta, liberación repetida idempotente, validaciones de cabecera y UUID, errores de curso inexistente/inactivo y contrato OpenAPI verificados. Cobertura CI de cursos: 34 pruebas y 87,50 % de líneas.
- Evidencia: harness/evidencia/cur-plz-01-concurrencia.txt y ejecución CI 44210749208.
- Estado: pasó a passed.
- Siguiente paso: validar las consultas y modificaciones restantes de cursos o pasar a estudiantes.


## 2026-11-17 — EST-01 aprobado

- Tarea y spec: EST-01, specs/servicio-estudiantes/01-registrar-estudiante.md; issue #6, PR #29.
- Dependencias: sin dependencias previas.
- Pruebas y resultado: registro de estudiante 201 con ID y Location al detalle; consulta por ID 200 ACTIVO; persistencia tras reinicio de contenedores y bases PostgreSQL independientes confirmada en Docker Compose. Validación de formato de correo, campos requeridos y documento único comprobados en la suite de pruebas. CI de estudiantes: 31 pruebas satisfactorias y 81,18 % de líneas.
- Evidencia: harness/evidencia/inf-loc-01-2026-11-17.md y ejecución CI 44210749208.
- Estado: pasó a passed.
- Siguiente paso: validar INF-LOC-01 o continuar con consultas y actualizaciones de estudiantes.


## 2026-11-17 — INF-LOC-01 aprobado

- Tarea y spec: INF-LOC-01, specs/infraestructura/01-contenedores.md; issue #17, PR #40.
- Dependencias: CUR-01 y EST-01 aprobadas.
- Pruebas y resultado: construcción reproducible de imágenes con Docker Compose; arranque de servicios y dos bases PostgreSQL independientes en volúmenes separados; ejecución exitosa de la migración de estudiantes antes de inicializar la API; persistencia comprobada tras reinicio de contenedores y bases. Registro y consulta de cursos, estudiantes y matrículas funcionando.
- Evidencia: harness/evidencia/inf-loc-01-2026-11-17.md.
- Estado: pasó a passed.
- Siguiente paso: validar INF-CI-01 y las funcionalidades restantes.


## 2026-11-17 — CUR-02 aprobado

- Tarea y spec: CUR-02, specs/servicio-cursos/02-consultar-cursos.md; issue #2, PR #25.
- Dependencias: CUR-01 aprobada.
- Pruebas y resultado: consulta por ID exitosa 200, detalle del curso reflejando reservas vigentes y cálculo dinámico de plazas disponibles sin mostrar valores negativos verificado junto a CUR-PLZ-01. Manejo de curso inexistente 404 y UUID inválido 400 documentados en OpenAPI. Cobertura CI de cursos: 34 pruebas y 87,50 % de líneas.
- Evidencia: harness/evidencia/cur-plz-01-concurrencia.txt, harness/evidencia/inf-loc-01-2026-11-17.md y ejecución CI 44210749208.
- Estado: pasó a passed.
- Siguiente paso: validar CUR-03.


## 2026-11-17 — CUR-03 aprobado

- Tarea y spec: CUR-03, specs/servicio-cursos/03-actualizar-curso.md; issue #3, PR #26.
- Dependencias: CUR-01 y CUR-PLZ-01 aprobadas.
- Pruebas y resultado: actualización de datos de curso validando persistencia de cambios válidos, rechazo de modificaciones que reduzcan capacidad por debajo de reservas existentes (409) y conservación del estado previo ante errores de validación. Cobertura CI de cursos: 34 pruebas y 87,50 % de líneas.
- Evidencia: harness/evidencia/cur-plz-01-concurrencia.txt y ejecución CI 44210749208.
- Estado: pasó a passed.
- Siguiente paso: validar CUR-04.


## 2026-11-17 — CUR-04 aprobado

- Tarea y spec: CUR-04, specs/servicio-cursos/04-cambiar-estado.md; issue #4, PR #27.
- Dependencias: CUR-01 y CUR-PLZ-01 aprobadas.
- Pruebas y resultado: cambio de estado del curso ACTIVO/INACTIVO persistente; bloqueo de nuevas reservas cuando el curso pasa a inactivo respondiendo 409 CONFLICTO_RESERVA; reservas previas permanecen conservadas y liberables. Cobertura CI de cursos: 34 pruebas y 87,50 % de líneas.
- Evidencia: harness/evidencia/cur-plz-01-concurrencia.txt y ejecución CI 44210749208.
- Estado: pasó a passed.
- Siguiente paso: validar cadena de estudiantes (EST-02 a EST-04 y matrículas).


## 2026-11-17 — EST-02, EST-03, EST-04, EST-MAT-01, EST-MAT-02 y EST-MAT-03 aprobados

- Tareas y specs: EST-02 a EST-04 y EST-MAT-01 a EST-MAT-03 (issues #7 a #12, PRs #30 a #35).
- Dependencias: EST-01 y CUR-PLZ-01 previamente aprobadas.
- Pruebas y resultado:
  - EST-02: consulta por ID 200 ACTIVO y visualización en panel; filtros y paginación validados.
  - EST-03: actualización mediante PUT conservando estado; rechazo de documento duplicado 409 y 404 por inexistencia.
  - EST-04: cambio de estado ACTIVO/INACTIVO persistente e idempotente ante repetición.
  - EST-MAT-01: creación de matrícula atómica con reserva coordinada en CUR-PLZ-01; reintento con la misma clave devuelve 201 con el mismo identificador.
  - EST-MAT-02: consulta de detalle de matrícula y listados activos/cancelados; persistencia verificada tras reinicio de contenedores y bases.
  - EST-MAT-03: cancelación de matrícula durable con liberación coordinada de plaza en cursos.
  - Cobertura CI de estudiantes: 31 pruebas y 81,18 % de líneas.
- Evidencia: harness/evidencia/inf-loc-01-2026-11-17.md y ejecución CI 44210749208.
- Estado: pasaron a passed.
- Siguiente paso: validar funcionalidades de frontend (WEB-*) e infraestructura restante.


## 2026-11-17 — WEB-PAN-01, WEB-CUR-01, WEB-EST-01 y WEB-MAT-01 aprobados

- Tareas y specs: WEB-PAN-01 a WEB-MAT-01 (issues #13 a #16, PRs #36 a #39).
- Dependencias: backends de cursos, estudiantes y matrículas aprobados.
- Pruebas y resultado: panel principal cargando métricas en tiempo real de cursos y estudiantes activos y matrículas; listado de cursos, visualización de estudiantes y flujo de matrículas con creación y cancelación verificado en la interfaz web local sobre Docker Compose. Compilación y 5 pruebas unitarias de frontend aprobadas en CI 44210749208.
- Evidencia: harness/evidencia/inf-loc-01-2026-11-17.md y ejecución CI 44210749208.
- Estado: pasaron a passed.


## 2026-11-17 — INF-CI-01 aprobado

- Tarea y spec: INF-CI-01, specs/infraestructura/02-github-actions-ecr.md; issue #18, PR #41.
- Dependencias: INF-LOC-01 aprobada.
- Pruebas y resultado: umbrales de cobertura comprobados en ejecuciones reales de GitHub Actions: fallo documentado cuando la cobertura fue <= 80 % (ejecución 44208787841) y aprobación completa con cobertura > 80 % por backend (ejecución 44210749208: cursos 87,50 %, estudiantes 81,18 %, frontend 5 pruebas y compilación).
- Evidencia: ejecuciones CI 44208787841 y 44210749208.
- Estado: pasó a passed.


## 2026-11-17 — INF-CI-01: Publicación en Amazon ECR verificada

- Tarea y spec: INF-CI-01, specs/infraestructura/02-github-actions-ecr.md; issue #18, PR #41.
- Ejecución de workflow: [Run 44223038607](https://github.com/Jhostin283/plataforma-gestion-academica/actions/runs/44223038607).
- Pruebas y resultado: validación exitosa de los 3 componentes (cursos, estudiantes y frontend); autenticación segura en AWS vía GitHub OIDC sin claves estáticas; construcción y publicación de los 3 contenedores en Amazon ECR (cademia/cursos, cademia/estudiantes y cademia/frontend) etiquetados con el commit ef354c8d6f9f5a4cf44abe6ddf13045ac8b44ede.
- Evidencia: ejecución CI 44223038607 e imágenes listadas en repositorios ECR de la cuenta 357112747027.
- Estado: passed.

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
