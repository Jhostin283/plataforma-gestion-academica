# Ejecución local propuesta

Esta configuración requiere que los cambios de cursos y de estudiantes/frontend se integren en la misma revisión. En esta rama el código de cursos puede no estar presente; no se ha construido ni ejecutado este entorno.

Con Docker Compose disponible, define en el entorno local contraseñas de desarrollo independientes `CURSOS_DB_PASSWORD` y `ESTUDIANTES_DB_PASSWORD`, y la URL `ESTUDIANTES_DB_URL` con el formato `postgresql+psycopg://estudiantes:<contraseña-codificada>@db-estudiantes:5432/estudiantes`. La contraseña de la URL debe corresponder a `ESTUDIANTES_DB_PASSWORD`; codifica los caracteres especiales para una URL. No guardes valores reales en Git.

Ejecuta `docker compose up --build` desde la raíz. El servicio de migración de estudiantes se ejecuta una vez antes del backend; cursos aplica sus migraciones Flyway al arrancar. El frontend y las rutas de API se sirven en `http://127.0.0.1:8080`. Los datos quedan en volúmenes separados. Para detener los procesos sin borrar datos utiliza `docker compose down`; la eliminación de volúmenes borra los datos y requiere una decisión explícita.

Pendiente de validación final: construcción de las tres imágenes, migraciones, salud, proxy, persistencia tras reinicio y ausencia de secretos en capas. No se ha confirmado que el runtime local de Docker esté disponible.
