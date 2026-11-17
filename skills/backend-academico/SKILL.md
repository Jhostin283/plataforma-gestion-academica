---
name: backend-academico
description: Implementar o modificar los servicios backend de cursos, estudiantes y matrículas de la plataforma académica.
---

# Backend académico

## Stack tecnológico

**Servicio de cursos**

- Lenguaje: Java. Framework HTTP: Spring Boot y Spring Web.
- Validación: Bean Validation. Contrato REST: OpenAPI con springdoc-openapi.
- Base de datos: PostgreSQL independiente. Persistencia propuesta: Spring Data JPA.
- Migraciones propuestas: Flyway o Liquibase; elegir una al implementar.

**Servicio de estudiantes y matrículas**

- Lenguaje: Python. Framework HTTP: FastAPI.
- Validación y contratos: Pydantic y generación OpenAPI integrada en FastAPI.
- Base de datos: PostgreSQL independiente. Persistencia propuesta: SQLAlchemy; migraciones propuestas: Alembic.
- Comunicación: cliente HTTP con tiempos de espera e idempotencia; biblioteca concreta pendiente de elegir.

**Común:** arquitectura hexagonal, API REST bajo `/api/v1/`, contenedores y configuración externa. Las versiones, gestor de construcción Java y bibliotecas no fijadas se decidirán durante la implementación.

Lee el README y los specs y contratos pertinentes antes de implementar. Si aún no existen, identifícalos como pendientes y define con el equipo los criterios necesarios sin presentar trabajo pendiente como completado.

- Mantén cursos en Java con Spring Boot y estudiantes y matrículas en Python con FastAPI; cada servicio es dueño de su base PostgreSQL.
- Expón APIs HTTP/REST y contratos OpenAPI por servicio; usa springdoc-openapi para cursos y la generación integrada de FastAPI para estudiantes. Verifica compatibilidad cuando cambie un contrato.
- Usa el prefijo `/api/v1/` en las rutas públicas e internas según `specs/convenciones-api.md`. Planifica una nueva versión mayor ante cambios incompatibles.
- Implementa reserva de plazas atómica y operaciones idempotentes. Para matrículas y cancelaciones contempla fallos entre servicios, reintentos, compensación y reconciliación; no presupongas una transacción entre bases.
- Valida reglas de negocio y errores en el límite de la API. Añade pruebas unitarias con dependencias simuladas y documenta los criterios que no se puedan comprobar así.
- Conserva la trazabilidad entre requisito, código, prueba y evidencia. Cada servicio debe superar el 80 % de cobertura de líneas por separado; informa el resultado real.

## Diseño

- Aplica **arquitectura hexagonal** en cada servicio; consulta `specs/arquitectura.md`.
- **Dominio:** contiene modelos y reglas de negocio. No importa frameworks, ORM ni clientes HTTP.
- **Aplicación:** coordina casos de uso y define los puertos necesarios para interactuar con el exterior.
- **Adaptadores de entrada:** controladores Spring o rutas FastAPI convierten HTTP y DTO en llamadas a casos de uso.
- **Adaptadores de salida:** implementan los puertos para PostgreSQL y llamadas al otro servicio.
- **Composición:** configura la inyección de dependencias en el borde de la aplicación. Las dependencias del código apuntan hacia el núcleo.
- Cada servicio administra su propio esquema y sus migraciones versionadas. Las restricciones de base de datos refuerzan las reglas de unicidad.
- Identifica los recursos mediante IDs estables. Documenta los cambios incompatibles de API antes de implementarlos.

## Patrones

- **Repositorio:** encapsula el acceso a los datos. El caso de uso solicita operaciones al repositorio sin construir consultas directamente.
- **DTO:** define los datos de entrada y salida de la API. Evita exponer las entidades de persistencia al consumidor.
- **Inyección de dependencias:** proporciona repositorios y clientes HTTP a los componentes que los necesitan. Facilita sustituir implementaciones durante las pruebas.
- **Compensación:** si una plaza fue reservada pero la matrícula no se pudo registrar, inicia la liberación dla plaza y deja evidencia para reconciliar cualquier fallo pendiente.
- Usa cada patrón cuando resuelva una necesidad concreta. Evita crear abstracciones que no aporten claridad.

## Java y Spring Boot

- Usa Spring Web y Bean Validation para validar entradas; centraliza el manejo de excepciones y devuelve errores consistentes sin exponer detalles internos.
- Define límites transaccionales en la capa de aplicación. Para plazas usa una actualización condicional o bloqueo de base de datos adecuado y comprueba el resultado bajo concurrencia.
- Utiliza Spring Data JPA cuando simplifique la persistencia y Flyway o Liquibase para migraciones. Incluye pruebas de las consultas y restricciones reales, preferiblemente con PostgreSQL.
- Expón endpoints de salud para Kubernetes y configura puertos, conexiones y credenciales mediante configuración externa.

## Python y FastAPI

- Define modelos Pydantic para entrada y salida y utiliza dependencias de FastAPI para gestionar servicios y sesiones de base de datos. No compartas sesiones entre solicitudes.
- Usa SQLAlchemy y Alembic o herramientas equivalentes coherentes con el proyecto; controla transacciones explícitamente. Si usas funciones `async`, evita llamadas bloqueantes en el ciclo de eventos.
- Implementa un cliente HTTP con tiempos de espera, errores tipados y reintentos limitados solo cuando la operación sea segura de repetir. Propaga un identificador de correlación sin registrar datos sensibles.
- Persiste el estado de las operaciones distribuidas y sus claves de idempotencia. Diseña la recuperación también para el caso en que una reserva se haya realizado pero su respuesta se pierda; prueba reintentos y reconciliación.

## Buenas prácticas: SOLID

1. **Responsabilidad única:** cada clase debe tener una función clara. Por ejemplo, el controlador recibe HTTP; el caso de uso aplica las reglas de matrícula.
2. **Abierto a extensión:** cuando exista una variación real, permite agregar comportamiento sin cambiar reglas estables. Evita anticipar variantes que aún no se necesitan.
3. **Sustitución de Liskov:** una implementación debe respetar las promesas de su contrato. Por ejemplo, un repositorio de prueba debe comportarse de acuerdo con la misma interfaz que el repositorio real.
4. **Segregación de interfaces:** ofrece contratos pequeños. Un caso de uso que solo consulta cursos no necesita depender de operaciones administrativas que no utiliza.
5. **Inversión de dependencias:** el dominio depende de contratos, no de detalles concretos de PostgreSQL o del cliente HTTP. Proporciona las implementaciones mediante inyección de dependencias.

## Otras buenas prácticas

- **DRY:** evita duplicar una misma regla de negocio; mantenla en un lugar identificable.
- **KISS:** elige la solución más sencilla que cumpla el requisito. Añade patrones solo si resuelven una necesidad concreta.
- **Bajo acoplamiento:** cada servicio controla sus datos; los demás se comunican mediante su API.

## Impedimentos

- No accedas a la base de datos del otro servicio.
- No importes adaptadores, frameworks ni detalles de persistencia en el dominio.
- No expongas entidades de persistencia como respuestas de la API.
- No asumas una transacción atómica entre ambas bases.
- No construyas SQL mediante concatenación de entradas del usuario.
- No guardes secretos en el código ni en los registros.
- No expongas trazas internas ni datos personales innecesarios.
- No ocultes fallos ni devuelvas respuestas exitosas falsas.
- No reintentes escrituras sin garantizar idempotencia.
- No mantengas transacciones de base de datos abiertas durante llamadas HTTP externas.
- No cambies un contrato incompatible sin actualizar consumidores y pruebas.
- No consideres suficiente una cobertura igual al 80 %: debe ser superior.
