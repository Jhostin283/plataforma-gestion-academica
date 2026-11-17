---
name: devops-academico
description: Preparar contenedores, CI con GitHub Actions y despliegue GitOps en AWS ECR y EKS con Argo CD para la plataforma académica.
---

# DevOps académico

## Stack tecnológico

- **Control de versiones:** Git y GitHub; issues y pull requests vinculados al harness.
- **CI:** GitHub Actions para pruebas, cobertura y construcción de imágenes.
- **Contenedores:** imágenes OCI y Dockerfiles; runtime y herramientas locales por confirmar.
- **Registro:** Amazon ECR para imágenes versionadas de frontend y servicios.
- **Orquestación:** Kubernetes en Amazon EKS, con Deployments, Services e Ingress.
- **CD:** Argo CD sincroniza desde Git los manifiestos con el clúster.
- **Acceso a AWS:** IAM y federación OIDC desde GitHub Actions; secretos fuera del repositorio.
- **Datos:** dos bases PostgreSQL; su ubicación en RDS o Kubernetes aún está por decidir.
- **Pendiente de elección:** versiones, controlador de Ingress, gestión de secretos y formato de manifiestos entre YAML, Helm o Kustomize.

Consulta el README y los specs de infraestructura aplicables; distingue configuración propuesta de recursos desplegados y verificados.

- Construye imágenes para ambos servicios y el frontend. En GitHub Actions ejecuta pruebas y cobertura individual superior al 80 % en los servicios antes de publicar imágenes versionadas en ECR.
- Usa referencias de imagen únicas vinculadas al commit, preferiblemente digest. Propón el cambio de manifiestos en Git; Argo CD sincroniza Git con EKS y publicar en ECR por sí solo no inicia el despliegue.
- Prepara Kubernetes, Service, Ingress y configuración de Argo CD según el entorno. Considera permisos IAM y conectividad de los nodos para descargar imágenes.
- Para acceso de GitHub Actions a AWS, prefiere OIDC y roles de permisos limitados. No incluyas credenciales ni secretos en el repositorio.
- Documenta dependencias, costos y limpieza de recursos; registra pruebas y evidencia. No des por desplegado algo que no se haya verificado.

## Contenedores y Kubernetes

- Utiliza compilación multietapa cuando corresponda, imágenes base mantenidas y versiones fijadas. Ejecuta como usuario sin privilegios cuando la imagen lo permita y excluye secretos, dependencias locales y artefactos innecesarios del contexto de compilación.
- Declara requests y limits razonables, probes de inicio, disponibilidad y salud coherentes con cada aplicación. La sonda de actividad no debe reiniciar el servicio solo por una dependencia externa temporalmente caída.
- Separa configuración de secretos, define namespaces y etiquetas consistentes y usa `ClusterIP` para servicios internos. Publica únicamente las rutas necesarias mediante Ingress y el controlador de AWS elegido.
- Ejecuta migraciones de forma controlada antes de depender de cambios de esquema; evita que múltiples réplicas apliquen migraciones simultáneamente sin coordinación.
- Si se usa PostgreSQL en Kubernetes, diseña almacenamiento persistente, copias de seguridad y recuperación; para RDS define conectividad privada y credenciales independientes.

## CI/CD y GitOps

- Separa validación de pull requests de publicación: ejecuta pruebas en las propuestas y publica imágenes solo desde la rama o evento de despliegue acordado tras validación exitosa.
- Usa autenticación OIDC de GitHub Actions con una política de confianza limitada al repositorio y rama o entorno autorizados. Otorga al rol IAM solo los permisos de ECR necesarios.
- Gestiona manifiestos con Kustomize, Helm o YAML coherente con el repositorio; fija las referencias de imagen y conserva el historial de cambios en Git. Sincronización automática y eliminación automática de recursos son opciones separadas que se deben configurar deliberadamente.
- Verifica estado de sincronización y salud en Argo CD, disponibilidad de pods y prueba funcional; define la reversión como un cambio de Git a una versión conocida. Evita desplegar manualmente versiones que Git no refleje.
- Registra las configuraciones y comandos reproducibles sin declarar recursos operativos hasta haberlos comprobado. Revisa costos de EKS, cómputo, ECR, balanceador y bases antes de crear recursos.

## Buenas prácticas

- **Infraestructura declarativa:** guarda la configuración de Kubernetes y Argo CD en Git para revisar y reproducir los cambios.
- **Responsabilidades claras:** GitHub Actions verifica y publica imágenes; Git registra la versión deseada; Argo CD sincroniza esa versión en Kubernetes.
- **Mínimo privilegio:** asigna a cada rol solo los permisos necesarios. Limita la confianza OIDC de GitHub a los repositorios y entornos autorizados.
- **Entornos separados:** mantén configuración y secretos diferenciados para desarrollo y otros entornos que se acuerden.
- **Imágenes identificables:** utiliza etiquetas únicas asociadas al commit o digests para poder comprobar y revertir una versión.
- **Observabilidad:** configura probes y revisa el estado de sincronización, los pods y los registros al validar un despliegue.
- **Recuperación:** documenta cómo volver a una versión anterior mediante Git y cómo respaldar o recuperar los datos persistentes.
- **Costos:** identifica los recursos de AWS que generan cargos y documenta su limpieza después del taller.

## Impedimentos
- No almacenes secretos, claves de acceso ni archivos de credenciales en Git, imágenes o logs. No otorgues permisos amplios cuando existan permisos específicos suficientes.
- No uses `latest` como referencia de despliegue ni sobrescribas versiones utilizadas para una entrega; usa etiquetas únicas o digests. No presupongas que subir una imagen activa Argo CD.
- No expongas bases de datos ni el panel de Argo CD públicamente por defecto. Limita acceso, configura autenticación adecuada y documenta la conectividad necesaria.
- No elimines recursos, habilites poda automática ni despliegues en un entorno compartido solo por ejecutar una validación local; aplica las instrucciones y autorizaciones del entorno.
- No afirmes que un despliegue pasó sin comprobar la sincronización, la salud y el funcionamiento de los componentes afectados.
