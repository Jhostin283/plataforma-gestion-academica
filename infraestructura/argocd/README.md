# Despliegue GitOps propuesto

Este archivo es una propuesta sin aplicar. Requiere un clúster EKS autorizado, Argo CD instalado en ese clúster, acceso de solo lectura al repositorio, imágenes ECR verificadas y los requisitos de [Kubernetes](../kubernetes/README.md) resueltos. No crea EKS, ECR, roles, bases ni otros recursos AWS.

Antes de sincronizar, actualizar por PR las referencias de imagen de Kustomize y del Job de migración con la versión verificada, configurar Ingress y secretos externos, y coordinar la migración de estudiantes. El manifiesto apunta a `developer`, el mismo clúster en el que se instale Argo CD y el namespace `academia`; revisar proyecto, destino y permisos para el entorno. El manifiesto actual contiene marcadores de imagen y no debe sincronizarse sin preparar un entorno válido.

Tras la aprobación y configuración del entorno, registrar la aplicación y sincronizar manualmente. Comprobar estado de sincronización y salud en Argo CD, disponibilidad de pods y rutas, acceso a ambas API desde el frontend y persistencia. Registrar los resultados reales; no se han realizado estas verificaciones.

Para revertir, proponer en Git el retorno a referencias de imagen y manifiestos previamente verificados y sincronizar esa revisión. Las migraciones de base pueden no ser reversibles: planificar respaldo y recuperación antes de aplicarlas. EKS, cómputo, ECR, balanceadores y bases pueden generar costos; definir responsables y un plan de eliminación del entorno antes de crearlo. No habilitar poda automática sin una decisión explícita.
