from unittest.mock import Mock
from uuid import uuid4

from app.application.port.out.reservas_cursos import Reserva, ReservaRechazada, ResultadoReservaIncierto
from app.application.usecase.crear_matricula import CrearMatricula
from app.domain.entity.matricula import Matricula


def preparar():
    estudiante, curso, clave, matricula_id = uuid4(), uuid4(), uuid4(), uuid4()
    pendiente = Matricula(matricula_id, clave, estudiante, curso, 'PENDIENTE')
    repositorio, reservas = Mock(), Mock()
    repositorio.iniciar.return_value = pendiente
    return estudiante, curso, clave, pendiente, repositorio, reservas


def test_reserva_confirmada_activa_matricula():
    estudiante, curso, clave, pendiente, repositorio, reservas = preparar()
    reservas.reservar.return_value = Reserva(clave, curso, 'RESERVADA')
    CrearMatricula(repositorio, reservas).ejecutar(clave, estudiante, curso)
    repositorio.cambiar_estado.assert_called_once_with(pendiente.id, 'ACTIVA')


def test_resultado_incierto_conserva_operacion_pendiente():
    estudiante, curso, clave, pendiente, repositorio, reservas = preparar()
    reservas.reservar.side_effect = ResultadoReservaIncierto()
    assert CrearMatricula(repositorio, reservas).ejecutar(clave, estudiante, curso) == pendiente
    repositorio.cambiar_estado.assert_not_called()


def test_rechazo_se_registra():
    estudiante, curso, clave, pendiente, repositorio, reservas = preparar()
    reservas.reservar.side_effect = ReservaRechazada(409)
    CrearMatricula(repositorio, reservas).ejecutar(clave, estudiante, curso)
    repositorio.cambiar_estado.assert_called_once_with(pendiente.id, 'FALLIDA', 409)
