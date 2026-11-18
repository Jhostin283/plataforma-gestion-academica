from uuid import UUID

from app.application.exception.errores import ConflictoMatricula, MatriculaNoEncontrada, ResultadoPersistenciaIncierto
from app.application.port.out.matricula_repository import MatriculaRepository
from app.application.port.out.reservas_cursos import ReservasCursos, ReservaRechazada, ResultadoReservaIncierto


class CancelarMatricula:
    def __init__(self, matriculas: MatriculaRepository, reservas: ReservasCursos):
        self.matriculas = matriculas
        self.reservas = reservas

    def ejecutar(self, matricula_id: UUID):
        matricula = self.matriculas.obtener(matricula_id)
        if matricula is None:
            raise MatriculaNoEncontrada()
        if matricula.estado == 'CANCELADA':
            return matricula
        if matricula.estado == 'ACTIVA':
            matricula = self.matriculas.cambiar_estado(matricula.id, 'CANCELACION_PENDIENTE', estado_actual='ACTIVA')
        if matricula.estado == 'CANCELADA':
            return matricula
        if matricula.estado != 'CANCELACION_PENDIENTE':
            raise ConflictoMatricula()
        try:
            reserva = self.reservas.liberar(matricula.operacion_id)
            if reserva.curso_id != matricula.curso_id or reserva.estado != 'LIBERADA':
                return matricula
            return self.matriculas.cambiar_estado(matricula.id, 'CANCELADA', estado_actual='CANCELACION_PENDIENTE')
        except (ReservaRechazada, ResultadoReservaIncierto, ResultadoPersistenciaIncierto):
            return matricula
