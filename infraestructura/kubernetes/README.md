# Manifiestos Kubernetes propuestos

La base Kustomize es una propuesta sin aplicar ni validar. Las imágenes `registry.invalid` son marcadores deliberadamente no utilizables: el entorno debe sustituirlas por imágenes ECR verificadas mediante digest o etiqueta asociada al commit, incluyendo la imagen del Job de migración.

Antes de desplegar, configurar un controlador de Ingress, `ingressClassName`, host y TLS, conectividad privada y dos bases PostgreSQL independientes con copias y recuperación. Crear fuera de Git los secretos `cursos-db` (claves `url`, `usuario`, `clave`) y `estudiantes-db` (clave `url`) en el namespace `academia`. No incluir sus valores en manifiestos o repositorio.

El Job `migracion-estudiantes.yaml` se ejecuta de forma controlada antes de actualizar el servicio, con la misma imagen que la aplicación; debe coordinarse una sola ejecución por migración. Cursos aplica Flyway al arrancar; la estrategia `Recreate` limita el solapamiento de pods de este despliegue, pero no coordina ejecuciones externas. Se debe planificar una estrategia de migraciones compatible con el entorno antes de escalar.

Las sondas de estudiantes usan `/health`, que actualmente solo indica que el proceso responde y no comprueba disponibilidad de la base. Debe añadirse una comprobación de disponibilidad separada antes de considerar verificada la preparación. Los límites y solicitudes, el enrutamiento, la política de red y el acceso de nodos a ECR requieren validación en un entorno autorizado. No se han creado recursos, aplicado manifiestos ni comprobado persistencia, sincronización o salud.
