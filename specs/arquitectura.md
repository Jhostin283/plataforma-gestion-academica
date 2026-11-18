# Arquitectura propuesta

**Estado:** diseño inicial; componentes y recursos pendientes de implementación. Los contratos definitivos y las decisiones pendientes se validarán en los issues correspondientes.

## Contexto

La plataforma apoya al administrador académico en la gestión de cursos, estudiantes y matrículas. Se plantea un monorepositorio que agrupa frontend, servicios, contratos, infraestructura y harness. La autenticación y los roles quedan fuera del alcance inicial; la exposición pública definitiva requiere definirlos.

## Componentes

```text
Administrador → Navegador → Ingress / balanceador AWS
                               ├─ / → Frontend React
                               ├─ /api/v1/cursos → Servicio de cursos (Java / Spring Boot)
                               │                         └─ PostgreSQL de cursos
                               └─ /api/v1/estudiantes, /api/v1/matriculas
                                  → Servicio de estudiantes (Python / FastAPI)
                                           ├─ PostgreSQL de estudiantes
                                           └─ HTTP interno → Servicio de cursos
```

- **Frontend:** presenta panel, cursos, estudiantes y matrículas; consume ambas API mediante el mismo origen público.
- **Cursos:** administra cursos y reservas de plazas; es el único que escribe en su base de datos.
- **Estudiantes:** administra estudiantes y matrículas; guarda los estados de las operaciones distribuidas en su base de datos.
- **Comunicación:** HTTP/REST con contratos OpenAPI. Kubernetes `Service` proporciona DNS interno para llamadas entre servicios. Cada API define sus errores y tiempos de espera.
- **Datos:** dos bases PostgreSQL separadas, con usuarios y credenciales independientes. La ubicación definitiva será RDS o Kubernetes con volúmenes persistentes, según el presupuesto.

## Organización interna de los servicios

Cada backend seguirá **arquitectura hexagonal**, también llamada puertos y adaptadores. La finalidad es mantener las reglas de negocio independientes de HTTP, PostgreSQL y los frameworks, y poder probar los casos de uso sin depender de esos detalles.

```text
Adaptadores de entrada       Núcleo de la aplicación       Adaptadores de salida
HTTP / controladores   →   Casos de uso → Dominio   →   PostgreSQL
                                │                       API de cursos
                                └─ puertos de salida ──→
```

- **Dominio:** modelos y reglas del negocio, como las condiciones de matrícula o la capacidad disponible. No depende de Spring, FastAPI, ORM ni clientes HTTP.
- **Aplicación:** coordina los casos de uso y define los puertos necesarios para acceder a datos o servicios externos. Invoca las reglas del dominio.
- **Adaptadores de entrada:** controladores Spring o rutas FastAPI traducen HTTP y DTO a llamadas a los casos de uso; convierten los resultados y errores en respuestas HTTP.
- **Adaptadores de salida:** implementan los puertos de persistencia y comunicación. Por ejemplo, un repositorio PostgreSQL o el cliente HTTP del servicio de cursos.
- **Configuración:** ensambla las dependencias mediante los mecanismos de cada framework. Las dependencias del código apuntan hacia el núcleo: este no importa adaptadores concretos.

### Estructura de paquetes

Cada servicio organiza su código con esta estructura conceptual; la sintaxis concreta de paquetes se adapta al lenguaje:

```text
servicio/
├── domain/
│   └── entity/                # Entidades del dominio
├── application/
│   ├── usecase/               # Implementaciones de casos de uso
│   ├── model/                 # Resultados usados por la aplicación
│   ├── exception/             # Errores expresados por la aplicación
│   ├── port/
│   │   ├── in/                # Contratos de casos de uso
│   │   └── out/               # Contratos para persistencia y servicios externos
└── adapters/
    ├── in/
    │   └── web/               # Controladores HTTP y DTO de entrada
    └── out/
        ├── persistence/       # Implementación de los puertos de datos
        └── ...                # Otros adaptadores, como clientes HTTP
```

Por ejemplo, el controlador de cursos invoca el puerto de entrada `RegistrarCursoUseCase`. El caso de uso implementa ese puerto y depende del puerto de salida `CursoRepository`; el adaptador de persistencia implementa este último. No todos los servicios necesitan los mismos adaptadores: se crean según sus dependencias reales.

En el servicio Java, `Curso` y `Reserva` pertenecen al dominio. `CursoEntity` y `ReservaEntity` son representaciones de persistencia JPA ubicadas en el adaptador de salida; no deben confundirse con las entidades del dominio. Los casos de uso se ubican en `application/usecase` y se acceden mediante `application/port/in`.

Las transacciones locales se delimitan al coordinar cada caso de uso. Las operaciones distribuidas conservan sus estados y mecanismos de recuperación; la arquitectura hexagonal no crea una transacción entre servicios. Usa puertos y adaptadores donde exista una dependencia real, sin añadir abstracciones innecesarias. No se comparten entidades ni bases entre servicios.

## Flujo de matrícula

1. El frontend solicita la matrícula al servicio de estudiantes.
2. Estudiantes valida el estado del alumno y registra de forma durable la operación con una clave única.
3. Estudiantes solicita una reserva al servicio de cursos mediante HTTP. Cursos reserva la plaza de forma atómica.
4. Estudiantes registra la matrícula tras confirmar la reserva. Si la respuesta se pierde, consulta o reintenta con la misma clave.
5. Si no puede completar la matrícula, gestiona la liberación. Las operaciones inciertas o compensaciones fallidas quedan registradas para reconciliación. La cancelación también libera la reserva de forma idempotente.

La clave de operación no sustituye la restricción única de matrícula activa por estudiante y curso. No se mantiene abierta una transacción local mientras se espera una llamada externa. Los estados exactos, política de reconciliación y contrato HTTP se concretarán en sus specs y PR.

## Despliegue

```text
Código → GitHub Actions (validación y construcción) → ECR
                     │                                 │
                     └→ cambio de referencia en Git    │
                                   ↓                   │
                              Argo CD → EKS ← imágenes ┘
```

GitHub Actions ejecuta pruebas y exige cobertura de líneas superior al 80 % en cada backend. Una vez validados los cambios de la rama de despliegue, publica imágenes versionadas y propone actualizar sus referencias en Git. Argo CD sincroniza los manifiestos con EKS. Subir imágenes a ECR no actualiza Git por sí solo. Los nodos requieren permisos y conectividad para descargarlas.

Kubernetes ejecutará cada componente de aplicación mediante `Deployment` y `Service`; un Ingress con controlador adecuado expondrá la interfaz y dirigirá las rutas. Los secretos no se guardan en Git. Los servicios incluyen sondas y configuración externa. La alternativa de almacenamiento elegida debe incluir persistencia, respaldo y recuperación.

## Decisiones pendientes

- Definir autenticación y control de acceso antes de un uso real o exposición pública con datos personales.
- Elegir ubicación de PostgreSQL, gestión de secretos y mecanismos concretos de reconciliación.
- Concretar esquemas OpenAPI y política de reintentos a partir de las [convenciones y respuestas propuestas](convenciones-api.md). La primera versión usa `/api/v1/`.
- Definir cuenta AWS, región, dominio, TLS y presupuesto antes del despliegue.
