package pe.academica.cursos.application.exception;

public class ReservaNoEncontradaException extends RuntimeException {
    public ReservaNoEncontradaException() { super("La reserva no existe"); }
}
