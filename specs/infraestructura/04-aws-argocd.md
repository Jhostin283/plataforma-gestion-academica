# INF-CD-01: Desplegar en EKS con Argo CD

## Objetivo
Desplegar en EKS con Argo CD para la plataforma de gestión académica.

## Precondiciones
- Existe cuenta AWS autorizada, ECR, EKS, Argo CD y acceso al repositorio.

## Flujo principal
1. Actualizar las referencias de imagen en Git.
2. Sincronizar los manifiestos mediante Argo CD.
3. Verificar pods, frontend y API.

## Casos alternativos
- Sin credenciales o recursos: registrar bloqueo.
- Sincronización fallida: investigar y no declarar éxito.

## Criterios de aceptación
- El cambio en Git llega a EKS.
- Ambos servicios responden desde el frontend.
- Se documentan reversión, costos y limpieza.

## Trazabilidad
- Requisito: INF-CD-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: Sincronización, flujo completo y reversión.
- Issue: pendiente.
- Pull request: pendiente.
- Estado: pendiente.
