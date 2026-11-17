# Flujo del harness

Este harness adapta el patrón de Anthropic para trabajo prolongado: lista de funcionalidades verificables, inicialización repetible, registro de progreso y cambios pequeños con pruebas. No ejecuta agentes automáticamente ni crea recursos externos.

## Antes de comenzar

1. Lee `README.md`, `AGENTS.md`, este archivo y `PROGRESS.md`.
2. Ejecuta `python harness/check.py` para validar la estructura y `python harness/init.py` para conocer las comprobaciones disponibles. Estos comandos no crean infraestructura ni instalan dependencias.
3. Selecciona una tarea pendiente en `features.json`. Lee su spec en `specs/INDICE.md`, sus dependencias y contratos. Comprueba que sus dependencias estén aprobadas antes de implementarla.
4. Identifica el issue enlazado a la tarea. Crea una rama `feature/<id>-<descripcion>` desde `devops`; abre su PR hacia `devops`. La rama `main` queda para la versión integrada.

## Durante la implementación

1. Cambia el estado a `in_progress` y registra el issue cuando exista. Implementa el alcance aprobado y mantén los criterios de aceptación visibles.
2. Ejecuta las pruebas aplicables, incluidas pruebas de contrato e integración si afectan a servicios. Para cada backend, configura una puerta de CI que rechace una cobertura de líneas menor o igual al 80 %.
3. Actualiza la evidencia en `features.json` y `TRACEABILITY.md`. Solo marca `passed` cuando todos los criterios aplicables estén comprobados y la evidencia identifique pruebas y resultados reales. Si hay un bloqueo usa `blocked` e indica el motivo.
4. Añade una entrada a `PROGRESS.md` con fecha, tarea, cambios, pruebas, resultados, bloqueos y siguiente paso. Si usas commits, incluye el ID de tarea en el mensaje.
5. Abre un pull request hacia `devops` que enlace el issue, el spec y la evidencia. Registra cada vínculo en la matriz. GitHub no cierra automáticamente un issue con `Closes #N` al fusionar hacia una rama que no sea la predeterminada: tras la integración y verificación en `devops`, cierra el issue manualmente con referencia al PR, o espera al PR de `devops` hacia la rama predeterminada. No cierres issues por trabajo no verificado.
6. Una vez completadas y verificadas todas las tareas, integra `devops` en `main` mediante PR. Crea la etiqueta `v1.0.0` sobre el commit de la versión verificada en `main`; no anticipes la etiqueta si quedan tareas o comprobaciones pendientes.

## Estados

- `pending`: trabajo no comenzado o sin evidencia suficiente para terminar.
- `in_progress`: trabajo iniciado.
- `blocked`: depende de información, recurso o decisión no disponible; registra el motivo.
- `passed`: criterios de aceptación verificados con evidencia.

Una tarea puede volver a un estado anterior si una comprobación posterior revela un fallo. No reduzcas el alcance ni elimines criterios para marcarla como aprobada.

## Límites de esta versión

Los specs son un punto de partida para crear issues; no son evidencia de implementación. No hay aún backend, frontend, pipeline, despliegue ni infraestructura operativa. `check.py` valida metadatos y referencias locales, no verifica que el producto funcione.
