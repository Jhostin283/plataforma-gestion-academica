# Frontend académico

Aplicación React con Vite. Configurar `VITE_API_BASE_URL` únicamente si las API no se sirven desde el mismo origen; el navegador utiliza `/api/v1/`. Instalar dependencias con `npm install` y ejecutar `npm run dev`. La compilación se genera con `npm run build`; los casos unitarios se ejecutan con `npm test`.

El panel consulta todas las páginas de cursos y estudiantes para contar los registros activos porque sus contratos actuales no incluyen filtro por estado. El total de matrículas activas se obtiene del filtro de la API. Los resultados pueden variar si los datos cambian durante la lectura paginada. Las pantallas de gestión y la validación de accesibilidad e integración están pendientes.
