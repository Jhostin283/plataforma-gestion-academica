from uuid import UUID

from app.application.exception.errores import ResultadoPersistenciaIncierto
from app.application.port.out.matricula_repository import MatriculaRepository
from app.application.port.out.reservas_cursos import ReservasCursos, ReservaRechazada, ResultadoReservaIncierto


class CrearMatricula:
    def __init__(self, matriculas: MatriculaRepository, reservas: ReservasCursos):
        self.matriculas = matriculas
        self.reservas = reservas

    def ejecutar(self, operacion_id: UUID, estudiante_id: UUID, curso_id: UUID):
        matricula = self.matriculas.iniciar(operacion_id, estudiante_id, curso_id)
        if matricula.estado == 'COMPENSACION_PENDIENTE':
            return self._compensar(matricula)
        if matricula.estado != 'PENDIENTE':
            return matricula
        try:
            reserva = self.reservas.reservar(curso_id, operacion_id)
        except ResultadoReservaIncierto:
            return matricula
        except ReservaRechazada as exc:
            try:
                return self.matriculas.cambiar_estado(matricula.id, 'FALLIDA', exc.status)
            except ResultadoPersistenciaIncierto:
                return matricula
        if reserva.estado != 'RESERVADA':
            return matricula
        try:
            return self.matriculas.cambiar_estado(matricula.id, 'ACTIVA')
        except ResultadoPersistenciaIncierto:
            try:
                compensacion = self.matriculas.cambiar_estado(matricula.id, 'COMPENSACION_PENDIENTE')
            except ResultadoPersistenciaIncierto:
                return matricula
            if compensacion.estado == 'COMPENSACION_PENDIENTE':
                return self._compensar(compensacion)
            return compensacion

    def _compensar(self, matricula):
        try:
            reserva = self.reservas.liberar(matricula.operacion_id)
            if reserva.curso_id != matricula.curso_id or reserva.estado != 'LIBERADA':
                return matricula
            return self.matriculas.cambiar_estado(matricula.id, 'FALLIDA', 503,
                                                  estado_actual='COMPENSACION_PENDIENTE')
        except (ReservaRechazada, ResultadoReservaIncierto, ResultadoPersistenciaIncierto):
            return matricula
