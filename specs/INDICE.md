# Índice de especificaciones

Cada spec tiene un identificador propio, una tarea en el harness y puede implementarse mediante su propio issue y pull request. Las [convenciones de API](convenciones-api.md) y el [flujo de comunicación entre servicios](comunicacion-entre-servicios.md) complementan los endpoints de backend; la evidencia se registra por spec.

| ID | Funcionalidad | Spec |
| --- | --- | --- |
| CUR-CUR-01 | Registrar curso | [Abrir](servicio-cursos/01-registrar-curso.md) |
| CUR-CUR-02 | Consultar cursos | [Abrir](servicio-cursos/02-consultar-cursos.md) |
| CUR-CUR-03 | Actualizar curso | [Abrir](servicio-cursos/03-actualizar-curso.md) |
| CUR-CUR-04 | Cambiar estado del curso | [Abrir](servicio-cursos/04-cambiar-estado.md) |
| CUR-PLZ-01 | Reservar y liberar plazas | [Abrir](servicio-cursos/05-reservar-liberar-plazas.md) |
| EST-EST-01 | Registrar estudiante | [Abrir](servicio-estudiantes/01-registrar-estudiante.md) |
| EST-EST-02 | Consultar estudiantes | [Abrir](servicio-estudiantes/02-consultar-estudiantes.md) |
| EST-EST-03 | Actualizar estudiante | [Abrir](servicio-estudiantes/03-actualizar-estudiante.md) |
| EST-EST-04 | Cambiar estado del estudiante | [Abrir](servicio-estudiantes/04-cambiar-estado.md) |
| EST-MAT-01 | Crear matrícula | [Abrir](servicio-estudiantes/05-crear-matricula.md) |
| EST-MAT-02 | Consultar matrículas | [Abrir](servicio-estudiantes/06-consultar-matriculas.md) |
| EST-MAT-03 | Cancelar matrícula | [Abrir](servicio-estudiantes/07-cancelar-matricula.md) |
| WEB-PAN-01 | Consultar panel principal | [Abrir](frontend/01-panel.md) |
| WEB-CUR-01 | Gestionar cursos desde el frontend | [Abrir](frontend/02-cursos.md) |
| WEB-EST-01 | Gestionar estudiantes desde el frontend | [Abrir](frontend/03-estudiantes.md) |
| WEB-MAT-01 | Gestionar matrículas desde el frontend | [Abrir](frontend/04-matriculas.md) |
| INF-LOC-01 | Preparar contenedores y ejecución local | [Abrir](infraestructura/01-contenedores.md) |
| INF-CI-01 | Configurar CI y publicación en ECR | [Abrir](infraestructura/02-github-actions-ecr.md) |
| INF-K8S-01 | Definir recursos Kubernetes | [Abrir](infraestructura/03-kubernetes.md) |
| INF-CD-01 | Desplegar en EKS con Argo CD | [Abrir](infraestructura/04-aws-argocd.md) |
