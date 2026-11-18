# CI y publicación propuestas

Los workflows están preparados para ejecución manual y no se han ejecutado. `Validar aplicaciones` ejecuta las pruebas de ambos backends y frontend, guarda los reportes de backend y exige cobertura de líneas estrictamente superior al 80 % por servicio. Para habilitar su ejecución automática en PR después de la validación acordada, será necesario configurar explícitamente el disparador correspondiente.

`Publicar imágenes en ECR` solo contempla una ejecución manual sobre `developer`, con confirmación explícita y la validación de ese mismo commit completada. Utiliza el entorno de GitHub `aws-ecr` y OIDC; se deben configurar externamente las variables `AWS_ECR_ROLE_ARN`, `AWS_REGION`, `ECR_REPO_CURSOS`, `ECR_REPO_ESTUDIANTES` y `ECR_REPO_FRONTEND`, además del rol IAM con confianza limitada al repositorio y entorno autorizados. Los repositorios ECR deben existir. Cada imagen usa el SHA completo del commit como etiqueta.

La publicación en ECR no despliega ni modifica manifiestos. Queda pendiente el mecanismo para proponer mediante PR las referencias verificadas de imagen en Git y la validación del flujo con AWS autorizado. No se han creado recursos, configurado credenciales ni publicado imágenes.
