package pe.academica.cursos.application;

public class CursoNoEncontradoException extends RuntimeException {
    public CursoNoEncontradoException() { super("El curso solicitado no existe"); }
}
