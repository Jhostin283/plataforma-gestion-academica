---
name: frontend-academico
description: Implementar o modificar las pantallas y la integración React de la plataforma académica.
---

# Frontend académico

## Stack tecnológico

- **Framework:** React con componentes funcionales y hooks.
- **Interfaz:** HTML semántico y CSS adaptable; variables CSS para los tokens visuales definidos en `references/diseno-academico.md`.
- **Comunicación:** HTTP/REST mediante las API OpenAPI versionadas bajo `/api/v1/`, enrutadas por Ingress.
- **Herramientas por definir:** JavaScript o TypeScript, compilador, enrutador y bibliotecas de formularios o estado. Elige y documenta las decisiones al implementar; no presupongas que están instaladas.

Lee el README y los specs de frontend y contratos OpenAPI relacionados. Implementa las pantallas solicitadas para cursos, estudiantes, matrículas y panel respetando el alcance aprobado.

Para decisiones de identidad visual o diseño de pantallas, consulta [diseño académico](references/diseno-academico.md) y la referencia original [Frontend Design de Anthropic](references/anthropic-frontend-design.md). Para revisar la interfaz y su accesibilidad, consulta [Web Design Guidelines de Vercel](references/vercel-web-design-guidelines.md); su guía indica consultar las reglas actualizadas en línea. Aplica estas referencias en la medida compatible con los requisitos y las instrucciones del proyecto.

La copia de Anthropic conserva su [licencia Apache 2.0 original](https://github.com/anthropics/skills/blob/main/skills/frontend-design/LICENSE.txt). Al compartir el material, revisa los términos aplicables a su redistribución.

- Consume las API mediante las rutas del Ingress y utiliza los contratos documentados; no accedas a las bases de datos.
- Maneja carga, ausencia de datos, errores de validación y servicios no disponibles. Evita afirmar que una matrícula se completó si su resultado es incierto.
- Usa formularios accesibles y una interfaz adaptable; verifica los flujos afectados y documenta las limitaciones observadas.
- Relaciona cada funcionalidad con su spec y la evidencia de verificación en el seguimiento del proyecto.

## React y diseño de interfaz

- Organiza por funcionalidades y separa componentes de presentación, lógica de formularios y acceso a API. Mantén un cliente HTTP común con URL configurable para cada entorno.
- Prefiere estado local para información local; incorpora estado compartido o bibliotecas adicionales solo cuando el flujo lo justifique. No dupliques datos que puedan derivarse del estado existente.
- Usa componentes funcionales y hooks respetando sus reglas. Cancela o ignora solicitudes obsoletas al cambiar de pantalla y evita efectos que actualicen el estado con respuestas desfasadas.
- Valida entradas para dar respuesta inmediata, pero considera al backend la autoridad de las reglas. Prevén envíos repetidos y muestra el estado de operaciones pendientes o inciertas.
- Utiliza HTML semántico, etiquetas asociadas a campos, navegación por teclado y mensajes de error accesibles. Verifica vistas móviles y de escritorio.
- Nunca guardes credenciales ni secretos en variables expuestas al navegador. Evita insertar HTML no confiable; no dependas de CORS como mecanismo de autorización.
- Prueba comportamientos observables: navegación, formularios, carga, errores y consumo de API. Utiliza dobles de API donde convenga y verifica al menos el flujo integrado relevante.

## Buenas prácticas

- **Responsabilidad única:** cada componente debe tener una función clara. Por ejemplo, separa el formulario de matrícula de la tabla de resultados.
- **Composición:** construye pantallas con componentes pequeños. Reutiliza un componente cuando varias pantallas necesiten el mismo comportamiento.
- **DRY:** evita duplicar la validación de un mismo formulario o la configuración de las llamadas a la API.
- **KISS:** usa estado local y componentes sencillos cuando sean suficientes. Añade bibliotecas o abstracciones si resuelven una necesidad real.
- **Estado coherente:** conserva una fuente de verdad para cada dato. Actualiza la vista después de confirmar el resultado del servidor.
- **Accesibilidad:** usa etiquetas descriptivas, controles utilizables con teclado y mensajes de error asociados a cada campo.
- **Experiencia de usuario:** muestra carga, resultados vacíos y fallos. Confirma las operaciones cuyo resultado fue recibido del servidor.
- **Contratos:** consulta los contratos OpenAPI antes de integrar una API y mantén su URL configurable por entorno.

## Impedimentos
- No accedas directamente a bases de datos ni incluyas contraseñas, tokens privados o credenciales AWS en el código del navegador o en variables públicas.
- No confíes solo en las validaciones del cliente ni muestres información sensible o errores internos. No uses HTML no confiable mediante `dangerouslySetInnerHTML` sin una sanitización adecuada.
- No declares exitosa una operación por una respuesta incierta, no ignores errores y no permitas que solicitudes antiguas sobrescriban información más reciente.
