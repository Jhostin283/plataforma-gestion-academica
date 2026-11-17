---
name: specs-trazabilidad
description: Crear y mantener especificaciones por servicio y seguimiento de tareas del harness para la plataforma académica.
---

# Specs y trazabilidad

## Herramientas del rol

- **Specs y documentación:** archivos Markdown en `specs/` y contratos OpenAPI por servicio cuando se implementen.
- **Seguimiento:** `harness/features.json`, `harness/TRACEABILITY.md` y `harness/PROGRESS.md`.
- **Validación local:** Python 3 con `python harness/init.py`; no instala dependencias ni despliega infraestructura.
- **Colaboración:** GitHub Issues y pull requests mediante las plantillas de `.github/`; requieren un repositorio remoto configurado.

Lee el README y examina el harness existente antes de modificarlo. Si no está disponible, informa esa limitación y evita inventar sus mecanismos o estados.

- Separa specs por servicio y funcionalidad; incluye objetivo, reglas, escenarios de error, criterios de aceptación y pruebas. Documenta contratos compartidos entre servicios y su evolución.
- Descompón el trabajo en tareas verificables con identificador, referencia al spec, dependencias, estado y evidencia. Mantén la relación requisito → implementación → prueba → resultado.
- Sigue el patrón de Anthropic de inicialización, lista de funcionalidades verificables y registro de progreso entre sesiones, adaptándolo al harness real. Una tarea se completa solo cuando se comprueban sus criterios.
- Registra los resultados reales y los bloqueos. Incluye el umbral de cobertura superior al 80 % por servicio y los requisitos de despliegue GitOps cuando sean aplicables.

## Estructura y seguimiento

- Para cada funcionalidad usa un identificador estable y define actor, objetivo, precondiciones, flujo, reglas, errores y criterios de aceptación observables. Evita imponer detalles internos que no sean necesarios para el contrato.
- Separa los contratos OpenAPI por proveedor y documenta consumidor, compatibilidad, autenticación si aplica, errores, tiempos de espera e idempotencia. Relaciona el flujo de comunicación entre servicios con el spec de matrículas.
- Mantén en el registro de tareas una referencia a los archivos implementados, pruebas ejecutadas, resultado y evidencia o motivo de bloqueo. Actualiza el progreso al terminar cada unidad de trabajo y señala el siguiente paso.
- Cambia requisitos o estados solo con evidencia y conserva el historial en el mecanismo disponible; no marques pruebas pendientes como aprobadas. La cobertura no sustituye criterios funcionales ni pruebas de fallos distribuidos.

## Buenas prácticas

- **Claridad:** escribe cada requisito con un objetivo concreto y criterios que puedan verificarse.
- **Separación:** guarda las funcionalidades de cursos y estudiantes en sus propios specs. Documenta aparte el contrato que comparten.
- **Identificadores:** asigna un ID estable a cada requisito y tarea para relacionarlos sin ambigüedad.
- **Fuente única:** mantén cada regla en un lugar identificable y enlázala desde los documentos relacionados.
- **Tareas pequeñas:** divide el trabajo en unidades que puedan implementarse y verificarse de forma independiente.
- **Evidencia:** relaciona cada tarea con la implementación, las pruebas y sus resultados reales.
- **Continuidad:** registra el estado actual, los bloqueos y el siguiente paso para facilitar la siguiente sesión del harness.
- **Cambios:** revisa el impacto en consumidores, pruebas y tareas antes de modificar un contrato o criterio de aceptación.

## Impedimentos
- No inventes funcionalidades, requisitos aprobados, resultados, pruebas o componentes existentes. Identifica explícitamente las decisiones pendientes y los supuestos.
- No marques tareas como terminadas antes de verificar sus criterios ni modifiques un contrato compartido sin identificar consumidores y pruebas afectadas.
- No borres el historial de seguimiento ni conviertas el registro de progreso en evidencia de pruebas que no fueron ejecutadas.
