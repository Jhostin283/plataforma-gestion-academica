package pe.academica.cursos.application.exception;

public class CursoNoEncontradoException extends RuntimeException {
    public CursoNoEncontradoException() { super("El curso solicitado no existe"); }
}
