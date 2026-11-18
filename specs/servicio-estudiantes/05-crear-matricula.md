# EST-MAT-01: Crear matrícula

## Objetivo
Crear matrícula para la plataforma de gestión académica.

## Precondiciones
- El estudiante existe y está activo.
- El curso existe y está activo.
- El estudiante no tiene una matrícula activa en el curso.

## Flujo principal
1. El administrador selecciona estudiante y curso.
2. El servicio valida al estudiante y registra una operación durable.
3. El servicio solicita una reserva de plaza con una clave única.
4. Si se confirma, registra la matrícula.
5. El frontend muestra el resultado confirmado.

## Casos alternativos
- Sin plazas o duplicado: rechazar con mensaje claro.
- Servicio de cursos no responde: conservar estado pendiente y comunicar resultado incierto.
- Si falla el registro tras reservar, iniciar compensación y registrar cualquier fallo.

## Criterios de aceptación
- Se crea la matrícula cuando se cumplen las condiciones.
- No se duplican matrículas activas.
- No se supera capacidad bajo concurrencia.
- Los reintentos no consumen dos plazas.
- Los fallos pueden reconciliarse.

## Endpoint propuesto

`POST /api/v1/matriculas` recibe `{"estudianteId":"UUID","cursoId":"UUID"}` y la cabecera `Idempotency-Key` con un UUID estable. La operación y su estado se registran antes de llamar a cursos.

- `201`: matrícula `ACTIVA` confirmada; devuelve `id`, ambos IDs, estado y `Location` al detalle.
- `202`: operación aceptada pero pendiente o con resultado incierto; devuelve `id`, estado `PENDIENTE` y `Location` para consulta. No se presenta como éxito definitivo.
- `400`: JSON, UUID o clave inválida. `404`: estudiante o curso inexistente. `409`: alumno o curso inactivo, curso lleno, matrícula activa duplicada o clave reutilizada con otros datos. `422`: campo obligatorio ausente.
- `503`: el servicio no puede aceptar la operación y no se ha iniciado ninguna reserva. Una vez iniciada, el resultado incierto se representa mediante `202` y estado consultable.

## Criterios adicionales de aceptación

- Dos solicitudes concurrentes para el mismo estudiante y curso producen como máximo una matrícula activa y no dejan reservas huérfanas sin seguimiento.
- La misma clave con el mismo cuerpo devuelve la operación existente; una clave con otro cuerpo produce `409`.
- Si se pierde la respuesta de cursos, la operación queda consultable y se reconcilia usando la clave original. Si falla la compensación, se conserva para reintento.
- Las pruebas comprueban respuestas, persistencia de estados y recuperación; OpenAPI describe todos los casos.

## Trazabilidad
- Requisito: EST-MAT-01
- Contrato relacionado: CUR-PLZ-01; specs/comunicacion-entre-servicios.md
- Pruebas: unitarias de idempotencia, fallos y compensación con dependencias simuladas.
- Issue: [10](https://github.com/Jhostin283/plataforma-gestion-academica/issues/10).
- Pull request: pendiente.
- Estado: pendiente.

## Pruebas relacionadas

Casos unitarios planificados en el [banco de pruebas](../../pruebas/BANCO-DE-PRUEBAS.md):

- **PR-EST-MAT-01-04:** Enviar JSON, UUID o clave inválidos, o campos ausentes.
- **PR-EST-MAT-01-10:** Simular estudiante inactivo o matrícula duplicada.
- **PR-EST-MAT-01-11:** Simular reserva confirmada y persistencia exitosa.
- **PR-EST-MAT-01-12:** Simular fallo de registro tras reservar.
