# Comunicación entre servicios para reservar plazas

El servicio de cursos es proveedor de reservas; el servicio de estudiantes es consumidor. Las rutas y respuestas propuestas se detallan en [CUR-PLZ-01](servicio-cursos/05-reservar-liberar-plazas.md) y las convenciones en [API](convenciones-api.md). El OpenAPI definitivo debe reflejar esas decisiones antes de implementar el cliente. No se considera implementado en este documento.

- Crear reserva: recibe curso y una clave estable de operación. Repetir la solicitud para el mismo curso y clave devuelve el resultado de la misma operación sin consumir una segunda plaza; reutilizar la clave con otra entrada devuelve conflicto.
- Consultar reserva: permite conocer su estado por identificador o clave para resolver respuestas perdidas.
- Liberar reserva: debe poder repetirse sin liberar más de una vez. Debe identificar la reserva original.
- Se rechazan cursos inexistentes o inactivos y capacidad agotada; se documentan respuestas y errores.
- El consumidor aplica timeout y reintentos limitados, guarda el estado pendiente y reconcilia resultados inciertos o compensaciones fallidas. No mantiene una transacción abierta durante una llamada de red.
- Las pruebas de contrato verifican compatibilidad; las pruebas de integración cubren concurrencia, respuestas perdidas y compensación.
