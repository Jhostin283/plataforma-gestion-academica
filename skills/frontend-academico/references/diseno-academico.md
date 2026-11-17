# Diseño frontend académico

Utiliza esta skill para diseñar o revisar pantallas de la plataforma. Lee el README, los specs de frontend disponibles y la skill `frontend-academico` antes de implementar. Respeta la identidad existente si ya fue aprobada.

## Objetivo

Diseña una interfaz profesional, agradable y reconocible para personal administrativo que registra cursos, estudiantes y matrículas. Prioriza la rapidez para encontrar información, la lectura de tablas y la seguridad al completar operaciones.

## Dirección visual

- Antes de diseñar, identifica al usuario, sus tareas principales, la información necesaria y los dispositivos previstos. Define una dirección visual breve que responda al contexto educativo.
- Propón una identidad coherente mediante tipografía legible, colores con contraste, espaciado consistente y un detalle visual propio. Justifica las decisiones por su utilidad para el producto.
- Define variables de diseño para colores, tipografía, espacios, bordes, sombras y estados. Reutilízalas en las pantallas sin impedir ajustes cuando una tarea lo requiera.
- Evita copiar plantillas de paneles de forma automática. No recurras por defecto a degradados decorativos, tarjetas repetitivas o animaciones que no aporten información.
- Mantén la personalidad visual subordinada a la legibilidad y al uso frecuente de la aplicación.

## Sistema visual propuesto

Usa esta propuesta como punto de partida para la plataforma. Ajusta las decisiones si existe una identidad institucional aprobada. Centraliza los valores en variables CSS y verifica el contraste de las combinaciones en la implementación.

### Colores

| Uso | Color | Valor |
| --- | --- | --- |
| Fondo general | Gris azulado claro | `#F3F6F9` |
| Superficies y formularios | Blanco | `#FFFFFF` |
| Texto principal | Azul tinta | `#142C42` |
| Texto secundario | Gris azulado | `#475569` |
| Color principal y acciones | Azul académico | `#174A78` |
| Hover de acción principal | Azul profundo | `#10385D` |
| Acento | Verde azulado | `#087E78` |
| Bordes y divisiones | Gris claro | `#D5DEE7` |
| Éxito | Verde oscuro | `#166534` |
| Advertencia | Ámbar oscuro | `#92400E` |
| Error | Rojo oscuro | `#B91C1C` |

Usa texto blanco sobre botones azul académico. En estados informativos, acompaña el color con texto o iconos. Comprueba al menos contraste 4.5:1 para texto normal y 3:1 para texto grande o componentes relevantes, según corresponda.

### Tipografía

- Usa una fuente sans serif legible para toda la aplicación; propone `"Source Sans 3", sans-serif` si la fuente está disponible y su licencia permite el uso previsto.
- Título de pantalla: 28–32 px, peso 700. Título de sección: 20–22 px, peso 600.
- Texto general y formularios: 16 px. Texto secundario o ayuda: al menos 14 px, con contraste suficiente.
- Utiliza interlineado aproximado de 1.5 para párrafos. Permite que el texto se amplíe sin ocultar información.

### Espaciado y componentes

- Usa una escala de espacios de 4, 8, 12, 16, 24 y 32 px. Mantén alineaciones consistentes.
- Utiliza esquinas de 8 px en campos y botones; 12 px en paneles. Reserva sombras suaves para elementos que necesiten separación visual.
- Define un botón principal por contexto; usa botones secundarios para acciones menos frecuentes. Elimina o confirma acciones destructivas con mensajes claros.
- Utiliza tablas para datos comparables, etiquetas de estado legibles y formularios agrupados por tarea. En móvil, adapta tablas sin perder encabezados ni acciones.

### Formatos de información

- Interfaz en español. Usa nombres comprensibles para cursos, estudiantes y matrículas.
- Muestra fechas como `dd/mm/aaaa` y almacénalas o intercámbialas con la API en formato ISO 8601 según su contrato. No conviertas fechas sin hora a otra zona horaria.
- Muestra capacidad como números enteros y comunica los plazas con texto, por ejemplo: `12 de 20 plazas ocupados`.
- Usa estados descriptivos como `Activo`, `Inactivo`, `Confirmada` o `Cancelada`; define los valores internos en los contratos de API.
- Para errores, explica qué ocurrió y qué puede hacer la persona, sin mostrar detalles técnicos internos.

## Diseño de pantallas

- **Panel:** presenta indicadores reales de cursos, estudiantes y matrículas; destaca acciones frecuentes y situaciones que requieran atención. No inventes métricas.
- **Listados:** ofrece búsqueda, estados visibles y acciones comprensibles. Diseña tablas legibles con una alternativa utilizable en pantallas pequeñas.
- **Formularios:** agrupa los campos por propósito, indica cuáles son obligatorios y muestra errores junto al campo correspondiente. Conserva los datos introducidos ante un error recuperable.
- **Matrícula:** facilita elegir estudiante y curso, muestra plazas disponibles y comunica si la operación está pendiente, confirmada o falló.
- **Estados:** diseña carga, lista vacía, ausencia de resultados y fallos de servicio. Da al usuario una acción clara cuando pueda recuperarse.

## Accesibilidad

- Usa HTML semántico, etiquetas visibles, navegación mediante teclado y un indicador de foco claro. Comprueba el orden de foco y su retorno al cerrar diálogos.
- Verifica contraste suficiente para texto y componentes. No transmitas estados solo mediante color; acompáñalos con texto o iconos.
- Permite ampliar la página y adapta los controles a pantallas táctiles. Respeta la preferencia de movimiento reducido.

## Revisión visual

- Comprueba las pantallas en escritorio y móvil, con datos realistas y textos largos. Revisa desbordamientos, jerarquía, alineación, estados de carga y mensajes de error.
- Recorre con teclado al menos el registro de un estudiante y la creación de una matrícula cuando estén implementados. Registra qué se verificó y qué quedó pendiente.
- Si hay una implementación ejecutable y herramientas disponibles, inspecciona visualmente los resultados y corrige los problemas observados. No declares una revisión visual realizada si no pudo ejecutarse.

## Impedimentos

- No priorices efectos visuales que dificulten la lectura o ralenticen las tareas.
- No inventes datos, indicadores o funciones para completar una pantalla.
- No elimines el foco visible ni desactives el zoom del navegador.
- No muestres una operación como exitosa antes de recibir confirmación; contempla resultados inciertos.
- No impongas una paleta, biblioteca o tipografía específica sin relacionarla con el contexto y los requisitos.

## Referencias

- [Anthropic: frontend-design](https://github.com/anthropics/skills/tree/main/skills/frontend-design): dirección visual intencional y diseño adaptado al contexto.
- [Vercel: Web Interface Guidelines](https://vercel.com/design/guidelines): revisión de interacción, accesibilidad y comportamiento de interfaces.
- [UI UX Pro Max](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill): catálogo de ideas para explorar alternativas; evalúa sus recomendaciones antes de aplicarlas.
