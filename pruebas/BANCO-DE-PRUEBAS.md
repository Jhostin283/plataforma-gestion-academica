# Banco de pruebas funcionales

**Estado inicial:** todos los casos están planificados y pendientes de automatización y ejecución. Ningún caso constituye evidencia de una prueba pasada. Cada ID relaciona un escenario con el spec de la funcionalidad. Los datos son sintéticos; los UUID de ejemplo deben ser válidos.

## Cómo utilizar el banco

1. Antes de implementar, lee el spec y sus casos. Añade casos cuando identifiques criterios o ramas relevantes no cubiertos.
2. Implementa únicamente pruebas unitarias: reglas y casos de uso con repositorios simulados, adaptadores HTTP aislados y componentes de interfaz con dependencias simuladas. No se conecta a bases reales ni se levantan contenedores.
3. En el PR registra cada ID ejecutado, resultado, comando y enlace al reporte o ejecución de CI. Si no puede ejecutarse, marca el caso como bloqueado y explica el motivo. La matriz del harness registra el enlace a la evidencia por funcionalidad.
4. Cada backend debe superar el **80 % de cobertura de líneas por separado**. Configura la medición y el bloqueo en CI; exactamente 80 % no cumple. El frontend necesita pruebas, pero todavía no tiene un umbral de cobertura acordado.
5. Revisa el reporte de líneas no cubiertas para identificar ramas de validación, excepciones y recuperación pendientes. El porcentaje no demuestra por sí solo que todas las reglas estén bien probadas.

Convenciones HTTP, paginación y errores: [convenciones API](../specs/convenciones-api.md). Todos los resultados esperados están sujetos a los specs y sus contratos. Los casos de infraestructura se verifican en sus propios specs; no forman parte de esta medición de cobertura. Una cobertura alta no demuestra migraciones, concurrencia ni persistencia reales.

## CUR-01 — Registrar curso

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-01-01 | Unitaria HTTP | Enviar un curso válido al controlador con el caso de uso simulado. | `201`, ID, `Location` y estado activo. |
| PR-CUR-01-02 | Unitaria HTTP | Simular conflicto de código duplicado en el caso de uso. | `409` con código de error documentado. |
| PR-CUR-01-03 | Unitaria HTTP | Omitir campo obligatorio o usar duración/capacidad no positivas. | `422` con detalles; sin escritura. |
| PR-CUR-01-04 | Unitaria HTTP | Enviar JSON malformado. | `400` sin detalles internos. |
| PR-CUR-01-06 | Unitaria HTTP | Simular fallo de persistencia al registrar el curso. | `503` con error claro y sin exponer detalles internos. |
| PR-CUR-01-07 | Unitaria | Registrar un curso con repositorio simulado y espacios en código y nombre. | Se normalizan los valores, se genera ID y se guarda con estado activo. |
| PR-CUR-01-08 | Unitaria | Simular que el repositorio informa un código duplicado. | El caso de uso propaga el conflicto. |

## CUR-02 — Consultar cursos

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-02-02 | Unitaria HTTP | Buscar texto sin coincidencias. | `200`, `items` vacío y total cero. |
| PR-CUR-02-03 | Unitaria HTTP | Consultar detalle con ID existente e inexistente. | `200` con capacidad y plazas para el existente; `404` para el inexistente. |
| PR-CUR-02-04 | Unitaria HTTP | Usar UUID o parámetros de paginación inválidos. | `400` con error documentado. |

## CUR-03 — Actualizar curso

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-03-05 | Unitaria HTTP | Actualizar UUID inexistente o inválido. | `404` o `400`, respectivamente. |
| PR-CUR-03-06 | Unitaria | Simular repositorio y actualizar un curso con datos válidos. | El caso de uso solicita guardar los nuevos valores. |
| PR-CUR-03-07 | Unitaria | Simular reservas mayores que la capacidad solicitada. | El caso de uso rechaza el cambio sin solicitar guardado. |

## CUR-04 — Cambiar estado del curso

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-04-03 | Unitaria HTTP | Repetir estado o enviar estado no permitido. | Repetición segura; estado inválido `422`. |
| PR-CUR-04-04 | Unitaria HTTP | Usar ID inexistente o malformado. | `404` o `400`, respectivamente. |
| PR-CUR-04-05 | Unitaria | Desactivar un curso activo mediante repositorio simulado. | El caso de uso guarda estado inactivo sin eliminar el curso. |

## CUR-PLZ-01 — Reservar y liberar plazas

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-CUR-PLZ-01-05 | Unitaria HTTP | Reservar en curso inexistente, inactivo o lleno. | `404` si no existe; `409` si está inactivo o lleno. |
| PR-CUR-PLZ-01-08 | Unitaria HTTP | Usar clave, UUID o reserva inexistente inválidos. | `400` para formato inválido; `404` para reserva inexistente. |
| PR-CUR-PLZ-01-09 | Unitaria | Simular curso lleno y solicitar reserva. | El caso de uso rechaza la reserva sin guardarla. |
| PR-CUR-PLZ-01-10 | Unitaria | Simular reserva existente para la misma clave. | El caso de uso devuelve la reserva existente sin crear otra. |

## EST-01 — Registrar estudiante

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-01-02 | Unitaria HTTP | Omitir datos o enviar correo inválido. | `422` con detalles; sin registro parcial. |
| PR-EST-01-04 | Unitaria HTTP | Enviar JSON malformado. | `400` sin datos internos. |
| PR-EST-01-05 | Unitaria | Registrar estudiante válido con repositorio simulado. | Se solicita guardar un estudiante activo con los datos esperados. |
| PR-EST-01-06 | Unitaria | Simular conflicto de documento en el repositorio. | El caso de uso propaga el conflicto. |

## EST-02 — Consultar estudiantes

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-02-02 | Unitaria HTTP | Buscar sin coincidencias. | `200`, lista vacía y total cero. |
| PR-EST-02-03 | Unitaria HTTP | Consultar detalle existente, inexistente y UUID inválido. | `200`, `404` y `400`, respectivamente. |
| PR-EST-02-04 | Unitaria HTTP | Probar paginación válida e inválida. | Página correcta o `400`; no se exponen campos internos. |

## EST-03 — Actualizar estudiante

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-03-03 | Unitaria HTTP | Enviar correo inválido o cuerpo incompleto. | `422`; no hay cambio parcial. |
| PR-EST-03-04 | Unitaria HTTP | Actualizar ID inexistente o inválido. | `404` o `400`, respectivamente. |
| PR-EST-03-06 | Unitaria | Actualizar un estudiante con repositorio simulado. | El caso de uso solicita guardar los nuevos datos. |

## EST-04 — Cambiar estado del estudiante

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-04-03 | Unitaria HTTP | Repetir estado o enviar valor no permitido. | Repetición segura; valor inválido `422`. |
| PR-EST-04-04 | Unitaria HTTP | Usar ID inexistente o inválido. | `404` o `400`, respectivamente. |
| PR-EST-04-05 | Unitaria | Desactivar un estudiante mediante repositorio simulado. | Se solicita guardar el estado inactivo sin borrar historial. |

## EST-MAT-01 — Crear matrícula

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-MAT-01-04 | Unitaria HTTP | Enviar JSON, UUID o clave inválidos, o campos ausentes. | `400` para formato inválido; `422` para campo ausente. |
| PR-EST-MAT-01-10 | Unitaria | Simular estudiante inactivo o matrícula duplicada. | El caso de uso rechaza la solicitud sin pedir reserva. |
| PR-EST-MAT-01-11 | Unitaria | Simular reserva confirmada y persistencia exitosa. | El caso de uso registra la matrícula con la reserva recibida. |
| PR-EST-MAT-01-12 | Unitaria | Simular fallo de registro tras reservar. | El caso de uso solicita compensación y conserva el estado de error. |

## EST-MAT-02 — Consultar matrículas

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-MAT-02-02 | Unitaria HTTP | Consultar sin coincidencias o con paginación inválida. | Lista vacía `200` o `400`, respectivamente. |
| PR-EST-MAT-02-03 | Unitaria HTTP | Consultar detalle existente, inexistente y UUID inválido. | `200`, `404` y `400`, respectivamente. |
| PR-EST-MAT-02-05 | Unitaria | Consultar matrículas con puerto de repositorio simulado. | Se aplican los filtros y se devuelve el resultado del repositorio. |

## EST-MAT-03 — Cancelar matrícula

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-EST-MAT-03-03 | Unitaria HTTP | Cancelar matrícula inexistente, UUID inválido o estado incompatible. | `404`, `400` o `409`, respectivamente. |
| PR-EST-MAT-03-05 | Unitaria | Cancelar con reserva confirmada usando puertos simulados. | Se solicita liberación y se registra la cancelación. |
| PR-EST-MAT-03-06 | Unitaria | Simular fallo al liberar una reserva. | El caso de uso conserva un estado pendiente de recuperación. |

## WEB-PAN-01 — Panel principal

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-PAN-01-01 | Unitaria componente | Abrir panel con datos conocidos en las API. | Los indicadores muestran datos reales y accesos funcionales. |
| PR-WEB-PAN-01-02 | Unitaria componente | Simular API caída o sin registros. | Se muestra error o estado vacío por sección, sin cifras inventadas. |

## WEB-CUR-01 — Pantallas de cursos

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-CUR-01-01 | Unitaria componente | Registrar, buscar, editar y cambiar estado con API simulada. | Cada acción envía datos correctos y muestra la respuesta confirmada. |
| PR-WEB-CUR-01-02 | Unitaria componente | Enviar campo inválido o recibir conflicto por duplicado. | Error asociado al campo o acción; datos recuperables conservados. |
| PR-WEB-CUR-01-03 | Unitaria componente | Simular carga, lista vacía y fallo de API. | Se muestran estados claros y acción de recuperación cuando corresponda. |

## WEB-EST-01 — Pantallas de estudiantes

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-EST-01-01 | Unitaria componente | Registrar, buscar, editar y cambiar estado de un estudiante. | Solicitudes correctas y confirmación basada en el servidor. |
| PR-WEB-EST-01-02 | Unitaria componente | Enviar correo inválido o recibir documento duplicado. | Error comprensible; no se muestra creación exitosa. |
| PR-WEB-EST-01-03 | Unitaria componente | Simular carga, vacío y fallo de servicio. | Estados visibles y recuperación disponible cuando corresponda. |

## WEB-MAT-01 — Pantallas de matrículas

| ID | Tipo | Preparación y acción | Resultado esperado |
| --- | --- | --- | --- |
| PR-WEB-MAT-01-01 | Unitaria componente | Seleccionar estudiante y curso con plazas; enviar matrícula. | Se muestran datos correctos y confirmación solo tras `201`. |
| PR-WEB-MAT-01-02 | Unitaria componente | Recibir `202` y después consultar el estado. | Se muestra pendiente hasta conocer el resultado final. |
| PR-WEB-MAT-01-03 | Unitaria componente | Recibir duplicado, curso lleno o fallo de API. | Mensaje claro; no se muestra éxito. |
| PR-WEB-MAT-01-04 | Unitaria componente | Consultar historial y cancelar una matrícula. | Historial y estado corresponden a las respuestas del servidor. |
