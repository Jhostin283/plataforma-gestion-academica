# INF-K8S-01: Definir recursos Kubernetes

## Objetivo
Definir recursos Kubernetes para la plataforma de gestión académica.

## Precondiciones
- Existen imágenes y configuración del entorno.

## Flujo principal
1. Definir Deployments, Services, Ingress y sondas.
2. Configurar acceso a datos y secretos.
3. Aplicar en un entorno de prueba y verificar.

## Casos alternativos
- Imagen no disponible o sonda fallida: registrar error.
- Sin clúster: conservar como no verificado.

## Criterios de aceptación
- Las rutas llegan a los componentes correctos.
- Los pods están saludables y los datos persisten.
- Los secretos no se guardan en Git.

## Trazabilidad
- Requisito: INF-K8S-01
- Contrato relacionado: OpenAPI del servicio correspondiente, cuando aplique
- Pruebas: Validación de manifiestos, salud y enrutamiento.
- Issue: pendiente.
- Pull request: pendiente.
- Estado: pendiente.
