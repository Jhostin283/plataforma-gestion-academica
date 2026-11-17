# Banco de pruebas funcionales

**Estado inicial:** todos los casos están planificados y pendientes de automatización y ejecución. Ningún caso constituye evidencia de una prueba pasada. Cada ID relaciona un escenario con el spec de la funcionalidad. Los datos son sintéticos; los UUID de ejemplo deben ser válidos.

## Cómo utilizar el banco

1. Antes de implementar, lee el spec y sus casos. Añade casos cuando identifiques criterios o ramas relevantes no cubiertos.
2. Implementa pruebas automatizadas adecuadas al escenario: unitarias para reglas aisladas, integración para API y PostgreSQL, contrato para OpenAPI y navegador para flujos de interfaz. Usa PostgreSQL aislado para restricciones y concurrencia reales.
3. En el PR registra cada ID ejecutado, resultado, comando y enlace al reporte o ejecución de CI. Si no puede ejecutarse, marca el caso como bloqueado y explica el motivo. La matriz del harness registra el enlace a la evidencia por funcionalidad.
4. Cada backend debe superar el **80 % de cobertura de líneas por separado**. Configura la medición y el bloqueo en CI; exactamente 80 % no cumple. El frontend necesita pruebas, pero todavía no tiene un umbral de cobertura acordado.
5. Revisa el reporte de líneas no cubiertas para identificar ramas de validación, excepciones y recuperación pendientes. El porcentaje no demuestra por sí solo que todas las reglas estén bien probadas.

Convenciones HTTP, paginación y errores: [convenciones API](../specs/convenciones-api.md). Todos los resultados esperados están sujetos a los specs y sus contratos. Los casos de infraestructura se verifican en sus propios specs; no forman parte de esta medición de cobertura de aplicación.

## CUR-CUR-01 — Registrar curso

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-CUR-01-01 | Integración | Enviar un curso válido a `POST /api/v1/cursos`. | `201`, ID y `Location`; curso activo persistido. |
| PR-CUR-CUR-01-02 | Integración | Enviar código ya registrado. | `409`; no se crea otro curso. |
| PR-CUR-CUR-01-03 | API | Omitir campo obligatorio o usar duración/capacidad no positivas. | `422` con detalles; sin escritura. |
| PR-CUR-CUR-01-04 | API | Enviar JSON malformado. | `400` sin detalles internos. |
| PR-CUR-CUR-01-05 | Integración | Enviar dos registros concurrentes con igual código. | Como máximo un registro creado; el otro recibe conflicto. |

## CUR-CUR-02 — Consultar cursos

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-CUR-02-01 | Integración | Crear cursos y buscar por nombre y código. | `200`; resultados y total corresponden al filtro. |
| PR-CUR-CUR-02-02 | API | Buscar texto sin coincidencias. | `200`, `items` vacío y total cero. |
| PR-CUR-CUR-02-03 | API | Consultar detalle con ID existente e inexistente. | `200` con capacidad y plazas para el existente; `404` para el inexistente. |
| PR-CUR-CUR-02-04 | API | Usar UUID o parámetros de paginación inválidos. | `400` con error documentado. |
| PR-CUR-CUR-02-05 | Integración | Consultar detalle tras reservar y liberar una plaza. | Disponibilidad actualizada y nunca negativa. |

## CUR-CUR-03 — Actualizar curso

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-CUR-03-01 | Integración | Actualizar campos de un curso existente con datos válidos. | `200`; cambios persistidos. |
| PR-CUR-CUR-03-02 | Integración | Usar código de otro curso o datos inválidos. | `409` por duplicado o `422` por validación; datos previos intactos. |
| PR-CUR-CUR-03-03 | Integración | Reducir capacidad por debajo de reservas vigentes. | `409`; capacidad y reservas sin cambios. |
| PR-CUR-CUR-03-04 | Integración | Ejecutar simultáneamente una reserva y una reducción de capacidad. | Ningún resultado final deja reservas por encima de capacidad. |
| PR-CUR-CUR-03-05 | API | Actualizar UUID inexistente o inválido. | `404` o `400`, respectivamente. |

## CUR-CUR-04 — Cambiar estado del curso

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-CUR-04-01 | Integración | Desactivar y volver a activar un curso. | `200` en cada cambio; estado persistido. |
| PR-CUR-CUR-04-02 | Integración | Desactivar curso con reserva y solicitar otra. | Nueva reserva `409`; la anterior sigue consultable y liberable. |
| PR-CUR-CUR-04-03 | API | Repetir estado o enviar estado no permitido. | Repetición segura; estado inválido `422`. |
| PR-CUR-CUR-04-04 | API | Usar ID inexistente o malformado. | `404` o `400`, respectivamente. |

## CUR-PLZ-01 — Reservar y liberar plazas

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-PLZ-01-01 | Integración | Reservar en curso activo con disponibilidad. | `201`, reserva `RESERVADA` y disponibilidad reducida una vez. |
| PR-CUR-PLZ-01-02 | Integración | Repetir la reserva con igual clave y datos. | `200`, misma reserva; no se consume otra plaza. |
| PR-CUR-PLZ-01-03 | Integración | Reutilizar clave para otro curso. | `409`; no se modifica ninguna reserva. |
| PR-CUR-PLZ-01-04 | Concurrencia | Enviar dos claves distintas por la última plaza. | Una reserva exitosa y un `409`; capacidad respetada. |
| PR-CUR-PLZ-01-05 | API | Reservar en curso inexistente, inactivo o lleno. | `404` si no existe; `409` si está inactivo o lleno. |
| PR-CUR-PLZ-01-06 | Integración | Consultar reserva y liberarla dos veces. | Estado `LIBERADA`; disponibilidad aumenta una sola vez. |
| PR-CUR-PLZ-01-07 | Integración | Simular pérdida de respuesta y reintentar con igual clave. | Se recupera la reserva original sin duplicación. |
| PR-CUR-PLZ-01-08 | API | Usar clave, UUID o reserva inexistente inválidos. | `400` para formato inválido; `404` para reserva inexistente. |

## EST-EST-01 — Registrar estudiante

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-EST-01-01 | Integración | Registrar estudiante válido. | `201`, ID y `Location`; estado activo persistido. |
| PR-EST-EST-01-02 | API | Omitir datos o enviar correo inválido. | `422` con detalles; sin registro parcial. |
| PR-EST-EST-01-03 | Integración | Registrar documento existente o dos solicitudes concurrentes con igual documento. | Solo un registro; las solicitudes duplicadas reciben `409`. |
| PR-EST-EST-01-04 | API | Enviar JSON malformado. | `400` sin datos internos. |

## EST-EST-02 — Consultar estudiantes

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-EST-02-01 | Integración | Buscar por nombres, apellidos y documento. | `200`; resultados y total coherentes. |
| PR-EST-EST-02-02 | API | Buscar sin coincidencias. | `200`, lista vacía y total cero. |
| PR-EST-EST-02-03 | API | Consultar detalle existente, inexistente y UUID inválido. | `200`, `404` y `400`, respectivamente. |
| PR-EST-EST-02-04 | API | Probar paginación válida e inválida. | Página correcta o `400`; no se exponen campos internos. |

## EST-EST-03 — Actualizar estudiante

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-EST-03-01 | Integración | Actualizar datos válidos de un estudiante. | `200` y valores persistidos. |
| PR-EST-EST-03-02 | Integración | Usar documento de otro estudiante. | `409`; registro anterior intacto. |
| PR-EST-EST-03-03 | API | Enviar correo inválido o cuerpo incompleto. | `422`; no hay cambio parcial. |
| PR-EST-EST-03-04 | API | Actualizar ID inexistente o inválido. | `404` o `400`, respectivamente. |
| PR-EST-EST-03-05 | Concurrencia | Actualizar dos estudiantes al mismo documento. | La restricción impide duplicados. |

## EST-EST-04 — Cambiar estado del estudiante

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-EST-04-01 | Integración | Desactivar y activar un estudiante. | `200`; cambios persistidos e historial conservado. |
| PR-EST-EST-04-02 | Integración | Intentar matricular estudiante inactivo. | `409`; no se genera matrícula ni reserva. |
| PR-EST-EST-04-03 | API | Repetir estado o enviar valor no permitido. | Repetición segura; valor inválido `422`. |
| PR-EST-EST-04-04 | API | Usar ID inexistente o inválido. | `404` o `400`, respectivamente. |

## EST-MAT-01 — Crear matrícula

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-MAT-01-01 | Integración | Matricular estudiante activo en curso activo con plazas. | `201`, matrícula activa y una reserva asociada. |
| PR-EST-MAT-01-02 | Integración | Intentar duplicado activo o estudiante inactivo. | `409`; sin nueva matrícula activa ni reserva huérfana. |
| PR-EST-MAT-01-03 | Integración | Solicitar curso inexistente, inactivo o lleno. | `404` si no existe; `409` si inactivo o lleno. |
| PR-EST-MAT-01-04 | API | Enviar JSON, UUID o clave inválidos, o campos ausentes. | `400` para formato inválido; `422` para campo ausente. |
| PR-EST-MAT-01-05 | Integración | Repetir clave con igual cuerpo y luego con cuerpo diferente. | Misma operación en repetición; `409` para datos distintos. |
| PR-EST-MAT-01-06 | Concurrencia | Crear simultáneamente la misma matrícula con claves distintas. | Como máximo una activa; sin reserva huérfana sin seguimiento. |
| PR-EST-MAT-01-07 | Integración | Perder la respuesta tras reservar exitosamente. | `202` mientras sea incierto; consulta y reconciliación con la clave original. |
| PR-EST-MAT-01-08 | Integración | Fallar al registrar matrícula tras reserva y luego fallar la liberación. | Operación rastreable; compensación pendiente y recuperación comprobable. |
| PR-EST-MAT-01-09 | Integración | Fallar antes de aceptar o iniciar reserva. | `503`; no existe reserva iniciada. |

## EST-MAT-02 — Consultar matrículas

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-MAT-02-01 | Integración | Filtrar matrículas por estudiante, curso y estado. | `200`; resultados e historial correctos. |
| PR-EST-MAT-02-02 | API | Consultar sin coincidencias o con paginación inválida. | Lista vacía `200` o `400`, respectivamente. |
| PR-EST-MAT-02-03 | API | Consultar detalle existente, inexistente y UUID inválido. | `200`, `404` y `400`, respectivamente. |
| PR-EST-MAT-02-04 | Integración | Consultar una operación previamente aceptada con `202`. | El detalle muestra su estado pendiente o final real. |

## EST-MAT-03 — Cancelar matrícula

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-MAT-03-01 | Integración | Cancelar una matrícula activa con reserva. | `200`, estado cancelado, historial conservado y plaza liberada. |
| PR-EST-MAT-03-02 | Integración | Repetir cancelación finalizada. | `200`; no se libera una segunda plaza. |
| PR-EST-MAT-03-03 | API | Cancelar matrícula inexistente, UUID inválido o estado incompatible. | `404`, `400` o `409`, respectivamente. |
| PR-EST-MAT-03-04 | Integración | Perder respuesta o fallar al liberar la reserva. | `202` pendiente; reintento o reconciliación alcanza estado consistente. |

## WEB-PAN-01 — Panel principal

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-PAN-01-01 | Interfaz | Abrir panel con datos conocidos en las API. | Los indicadores muestran datos reales y accesos funcionales. |
| PR-WEB-PAN-01-02 | Interfaz | Simular API caída o sin registros. | Se muestra error o estado vacío por sección, sin cifras inventadas. |
| PR-WEB-PAN-01-03 | Navegador | Revisar panel en móvil y recorrerlo con teclado. | Controles visibles y utilizables; foco claro. |

## WEB-CUR-01 — Pantallas de cursos

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-CUR-01-01 | Interfaz | Registrar, buscar, editar y cambiar estado con API simulada. | Cada acción envía datos correctos y muestra la respuesta confirmada. |
| PR-WEB-CUR-01-02 | Interfaz | Enviar campo inválido o recibir conflicto por duplicado. | Error asociado al campo o acción; datos recuperables conservados. |
| PR-WEB-CUR-01-03 | Interfaz | Simular carga, lista vacía y fallo de API. | Se muestran estados claros y acción de recuperación cuando corresponda. |
| PR-WEB-CUR-01-04 | Navegador | Usar listado y formulario en móvil y con teclado. | Información y acciones accesibles sin perder encabezados relevantes. |

## WEB-EST-01 — Pantallas de estudiantes

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-EST-01-01 | Interfaz | Registrar, buscar, editar y cambiar estado de un estudiante. | Solicitudes correctas y confirmación basada en el servidor. |
| PR-WEB-EST-01-02 | Interfaz | Enviar correo inválido o recibir documento duplicado. | Error comprensible; no se muestra creación exitosa. |
| PR-WEB-EST-01-03 | Interfaz | Simular carga, vacío y fallo de servicio. | Estados visibles y recuperación disponible cuando corresponda. |
| PR-WEB-EST-01-04 | Navegador | Completar formulario con teclado y en pantalla móvil. | Etiquetas, foco y controles utilizables. |

## WEB-MAT-01 — Pantallas de matrículas

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-MAT-01-01 | Interfaz | Seleccionar estudiante y curso con plazas; enviar matrícula. | Se muestran datos correctos y confirmación solo tras `201`. |
| PR-WEB-MAT-01-02 | Interfaz | Recibir `202` y después consultar el estado. | Se muestra pendiente hasta conocer el resultado final. |
| PR-WEB-MAT-01-03 | Interfaz | Recibir duplicado, curso lleno o fallo de API. | Mensaje claro; no se muestra éxito. |
| PR-WEB-MAT-01-04 | Interfaz | Consultar historial y cancelar una matrícula. | Historial y estado corresponden a las respuestas del servidor. |
| PR-WEB-MAT-01-05 | Navegador | Completar el flujo en móvil y mediante teclado. | La selección y los mensajes son accesibles. |
