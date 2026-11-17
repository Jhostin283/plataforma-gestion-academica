package pe.academica.cursos.application;

public class ParametrosConsultaInvalidosException extends RuntimeException {
    public ParametrosConsultaInvalidosException() { super("Los parámetros de paginación no son válidos"); }
}
