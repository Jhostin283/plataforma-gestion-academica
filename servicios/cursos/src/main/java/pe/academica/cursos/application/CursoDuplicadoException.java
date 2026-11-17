package pe.academica.cursos.application;

public class CursoDuplicadoException extends RuntimeException {
    public CursoDuplicadoException() { super("Ya existe un curso con el código indicado"); }
}
