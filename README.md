# Plataforma de gestión académica

> **Estado:** propuesta para el taller. Las funcionalidades, especificaciones, automatizaciones y recursos de AWS descritos aquí están pendientes de implementación y validación.

## Inicio del harness

El seguimiento inicial está disponible en [harness/WORKFLOW.md](harness/WORKFLOW.md). Consulta el [catálogo de tareas](harness/features.json), la [matriz de trazabilidad](harness/TRACEABILITY.md) y el [progreso](harness/PROGRESS.md). Los specs están separados por servicio en `specs/`; existen plantillas de issue y pull request en `.github/`.

La [arquitectura propuesta](specs/arquitectura.md) describe los componentes, la comunicación entre servicios, el flujo de matrícula y el despliegue. El [índice de specs](specs/INDICE.md) relaciona cada funcionalidad con su tarea del harness.

Con Python 3 instalado, ejecuta `python harness/init.py` para verificar el harness sin instalar dependencias ni crear infraestructura. El workflow `harness.yml` valida el catálogo en GitHub Actions mediante ejecución manual (`workflow_dispatch`); aún no implementa el CI de las aplicaciones ni publica imágenes. Crea cada issue a partir de una tarea y vincúlalo a su pull request siguiendo el flujo del harness.

## Problemática

Un centro de formación necesita administrar su oferta de cursos, los datos de sus estudiantes y sus matrículas. Cuando esta información se mantiene en hojas de cálculo o registros separados, resulta difícil conocer los plazas disponibles, evitar matrículas duplicadas y mantener la información actualizada. Además, consultar el historial y coordinar cambios entre registros requiere trabajo manual y puede producir inconsistencias.

El taller plantea también un reto técnico: construir dos servicios independientes, con tecnologías y bases de datos propias, integrarlos en una interfaz común y desplegarlos en Kubernetes sobre AWS. Es necesario poder seguir cada requisito desde su especificación hasta su implementación y sus pruebas.

## Solución propuesta

Desarrollar una plataforma web para un administrador académico que permita gestionar cursos, estudiantes y matrículas, y visualizar información consolidada desde un único frontend. La solución estará compuesta por dos servicios con responsabilidades y bases de datos separadas:

| Componente | Tecnología propuesta | Responsabilidad |
| --- | --- | --- |
| Servicio de cursos | Java y Spring Boot | Cursos, estado, capacidad y reserva de plazas. |
| Servicio de estudiantes | Python y FastAPI | Estudiantes y matrículas. |
| Frontend | React | Consultar ambos servicios y presentar formularios y paneles. |
| Bases de datos | Dos bases PostgreSQL independientes | Cada servicio administra sus propios datos y credenciales. |

Para el alcance inicial se considera un administrador; la autenticación y los roles quedan como ampliación futura. Ningún servicio accederá directamente a la base de datos del otro.

Cada backend usará **arquitectura hexagonal (puertos y adaptadores)**: el dominio y los casos de uso formarán el núcleo, mientras que HTTP, PostgreSQL y la comunicación con el otro servicio serán adaptadores. Las dependencias y ejemplos están detallados en la [arquitectura](specs/arquitectura.md).

## Funcionalidades previstas

### Cursos

- Registrar y actualizar cursos con código, nombre, descripción, duración y capacidad.
- Listar y buscar cursos, consultar su detalle y cambiar su estado.
- Consultar plazas disponibles y reservar o liberar plazas de manera controlada.
- Impedir nuevas matrículas en cursos inactivos o sin capacidad.

### Estudiantes y matrículas

- Registrar, consultar, buscar y actualizar estudiantes, y cambiar su estado.
- Matricular estudiantes activos en cursos activos y evitar matrículas activas duplicadas.
- Consultar las matrículas de un estudiante y los estudiantes matriculados en un curso.
- Cancelar matrículas conservando su historial y liberar la plaza correspondiente.

### Frontend

- Mostrar indicadores de cursos activos, estudiantes activos y matrículas activas.
- Gestionar cursos, estudiantes y matrículas desde pantallas específicas.
- Mostrar capacidad y plazas disponibles, errores de validación y fallos de comunicación.

## Arquitectura de aplicación

```text
Navegador → Frontend React → Ingress / balanceador
                                  ├─ /api/v1/cursos → Servicio de cursos → BD de cursos
                                  └─ /api/v1/estudiantes y /api/v1/matriculas
                                     → Servicio de estudiantes → BD de estudiantes
                                              │
                                              └─ API del servicio de cursos
```

Al crear una matrícula, el servicio de estudiantes valida sus reglas y solicita al servicio de cursos una reserva de plaza. La reserva debe ser atómica para evitar sobrepasar la capacidad ante solicitudes simultáneas y debe aceptar reintentos sin duplicarse. Si una reserva se confirma pero falla el registro de la matrícula, se debe intentar compensarla y registrar las operaciones pendientes para su reconciliación. La cancelación también debe liberar la plaza de forma idempotente. Los detalles, fallos y pruebas de este flujo se definirán en los contratos y specs; no hay una transacción única entre ambas bases.

### Comunicación y contratos OpenAPI

Los servicios se comunicarán de forma síncrona mediante **HTTP y APIs REST**. Dentro del clúster, el servicio de estudiantes localizará al servicio de cursos mediante el DNS interno de su recurso Kubernetes `Service`. El frontend accederá a las API a través del Ingress. Cada servicio accederá únicamente a su propia base de datos.

Cada API tendrá un contrato **OpenAPI** que describa sus rutas, parámetros, cuerpos, respuestas y errores. El contrato documenta la comunicación; las solicitudes y respuestas se transportan por HTTP. Por ejemplo, una operación de reserva de plaza especificará el curso, un identificador único de operación y las respuestas posibles, como reserva confirmada, curso inactivo o capacidad agotada.

- **Servicio de cursos:** se propone usar `springdoc-openapi` con Spring Boot para generar su documentación OpenAPI.
- **Servicio de estudiantes:** se aprovechará la generación OpenAPI integrada en FastAPI.
- Los contratos se conservarán o publicarán por servicio y se vincularán con sus specs. Pruebas de contrato e integración comprobarán que las solicitudes y respuestas respeten lo definido.
- Las llamadas entre servicios tendrán tiempos de espera y reintentos controlados. Las operaciones de reserva y liberación deberán ser idempotentes y contemplar compensación y reconciliación ante fallos.

## Infraestructura y despliegue: ECR, EKS y Argo CD

Se recomienda utilizar **GitHub Actions para integración continua (CI)** y **Argo CD para despliegue continuo (CD)**, bajo un flujo GitOps. Se asume que el repositorio estará alojado en GitHub. Amazon ECR almacena las imágenes, Amazon EKS ejecuta Kubernetes y Argo CD aplica el estado deseado declarado en Git. Argo CD debe instalarse y configurarse previamente en el clúster con acceso al repositorio de manifiestos.

```text
Código → GitHub Actions: pruebas y construcción → imágenes versionadas en Amazon ECR
                                         │
GitHub Actions propone actualizaciones de imagen en manifiestos Git
                                         │
                         Git → Argo CD → Amazon EKS
                                              │
                          Los nodos descargan las imágenes de ECR
```

Flujo recomendado:

1. Ante cambios en el código, **GitHub Actions** ejecuta los workflows de pruebas y medición de cobertura. El pipeline debe fallar si cualquiera de los dos servicios no supera el 80 % de cobertura de líneas o si fallan las pruebas; publicará los reportes como artefactos del workflow.
2. Después de validar la rama de despliegue, GitHub Actions construye y publica en ECR una imagen por componente: cursos, estudiantes y frontend. Cada imagen se identifica con una etiqueta única asociada al commit; preferiblemente se registra también su digest.
3. GitHub Actions propone, mediante un pull request, la nueva referencia de imagen en los manifiestos Kubernetes, Helm o Kustomize alojados en Git. Para el taller se puede actualizar y revisar esta referencia manualmente al principio.
4. Argo CD detecta el cambio en Git y sincroniza el estado deseado con EKS, automáticamente si se habilita la sincronización automática o manualmente si se prefiere revisar cada despliegue.
5. Kubernetes despliega las cargas y los nodos descargan las imágenes desde ECR. Un Ingress con un controlador de balanceo adecuado publica el frontend y enruta las API.

**Subir una imagen a ECR no despliega automáticamente una nueva versión:** también debe cambiar en Git la referencia de imagen que observa Argo CD. Argo CD sincroniza manifiestos; no construye ni publica imágenes. Las referencias versionadas permiten relacionar código, imagen y despliegue, y revertir mediante un cambio en Git.

GitHub Actions necesitará permisos para publicar en ECR; se recomienda configurar la federación OIDC de GitHub con AWS para asumir un rol IAM de permisos limitados, sin guardar claves permanentes de AWS. El workflow que proponga cambios en Git necesitará permisos de escritura limitados al repositorio y a la creación de pull requests. Los nodos de EKS necesitarán permisos IAM y conectividad de red para descargar imágenes. Las credenciales de las bases deben gestionarse como secretos, no guardarse en Git. Para las bases se podrá elegir entre dos PostgreSQL independientes en RDS o dos instancias en Kubernetes con almacenamiento persistente, según presupuesto y objetivos del taller. EKS, ECR, balanceadores, bases y otros recursos pueden generar costos; se debe planificar su eliminación al finalizar.

## Especificaciones y trazabilidad

Las especificaciones están organizadas por servicio y funcionalidad. El índice contiene los enlaces a cada documento:

```text
specs/
├── INDICE.md
├── arquitectura.md
├── comunicacion-entre-servicios.md
├── servicio-cursos/
├── servicio-estudiantes/
├── frontend/
└── infraestructura/
```

Cada spec definirá objetivo, reglas, casos de error, criterios de aceptación y pruebas. El harness, inspirado en el modelo de Anthropic para agentes de trabajo prolongado, mantendrá una lista de funcionalidades verificables y un registro de progreso entre sesiones. Cada tarea incluirá un identificador, spec asociado, estado, dependencias y evidencia. Solo se marcará como completada cuando se verifiquen sus criterios de aceptación.

### Cobertura de pruebas

Cada servicio debe alcanzar una **cobertura de líneas superior al 80 %** de su propio código: una cobertura de exactamente 80 % no cumple el requisito. La medición se hará por separado para evitar que el resultado de un servicio oculte deficiencias en el otro. Los reportes de cobertura y los resultados de las pruebas se conservarán como evidencia del pipeline y se vincularán con el seguimiento del harness. Además de esta métrica, se verificarán los flujos críticos de matrículas, concurrencia y compensación mediante pruebas apropiadas.

| ID de ejemplo | Requisito | Evidencia esperada | Estado |
| --- | --- | --- | --- |
| CUR-CUR-01 | Registrar cursos | Pruebas de API y persistencia | Pendiente |
| CUR-PLZ-01 | Reservar y liberar plazas | Pruebas de concurrencia e idempotencia | Pendiente |
| EST-EST-01 | Registrar estudiantes | Pruebas de API y persistencia | Pendiente |
| EST-MAT-01 | Crear matrículas | Pruebas de integración, fallos y compensación | Pendiente |
| INF-CI-01 | Automatizar CI y publicar en ECR | Pruebas, cobertura y publicación | Pendiente |

## Fuentes de referencia

- [Anthropic: Effective harnesses for long-running agents](https://www.anthropic.com/engineering/effective-harnesses-for-long-running-agents).
- [Argo CD: sincronización automática desde Git](https://argo-cd.readthedocs.io/en/stable/user-guide/auto_sync/).
- [AWS: uso de imágenes de Amazon ECR con Amazon EKS](https://docs.aws.amazon.com/AmazonECR/latest/userguide/ECR_on_EKS.html).
- [GitHub: configuración de OpenID Connect en Amazon Web Services](https://docs.github.com/en/actions/how-tos/secure-your-work/security-harden-deployments/oidc-in-aws).
