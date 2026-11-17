# INF-CI-01: Configurar CI y publicación en ECR

## Objetivo
Configurar CI y publicación en ECR para la plataforma de gestión académica.

## Precondiciones
- Existe repositorio GitHub, código comprobable y configuración AWS autorizada.

## Flujo principal
1. Ejecutar pruebas y cobertura por servicio.
2. Construir imágenes tras validar la rama acordada.
3. Publicar en ECR y proponer cambios de imagen en Git.

## Casos alternativos
- Pruebas o cobertura insuficiente: detener publicación.
- AWS no disponible: registrar bloqueo.

## Criterios de aceptación
- CI falla con cobertura de líneas menor o igual al 80 % por backend.
- Los reportes se conservan.
- Las imágenes se vinculan con su commit y se usa OIDC.

## Trazabilidad
- Requisito: INF-CI-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: Workflow, umbral, OIDC y publicación.
- Issue: pendiente.
- Pull request: pendiente.
- Estado: pendiente.
