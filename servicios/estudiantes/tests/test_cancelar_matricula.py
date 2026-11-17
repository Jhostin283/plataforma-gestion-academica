from unittest.mock import Mock
from uuid import uuid4

import pytest

from app.application.exception.errores import ConflictoMatricula, MatriculaNoEncontrada
from app.application.port.out.reservas_cursos import Reserva, ResultadoReservaIncierto
from app.application.usecase.cancelar_matricula import CancelarMatricula
from app.domain.entity.matricula import Matricula


def test_cancela_y_libera_con_clave_original():
    clave, estudiante, curso, identificador = uuid4(), uuid4(), uuid4(), uuid4()
    activa = Matricula(identificador, clave, estudiante, curso, 'ACTIVA')
    pendiente = Matricula(identificador, clave, estudiante, curso, 'CANCELACION_PENDIENTE')
    cancelada = Matricula(identificador, clave, estudiante, curso, 'CANCELADA')
    repo, reservas = Mock(), Mock()
    repo.obtener.return_value = activa
    repo.cambiar_estado.side_effect = [pendiente, cancelada]
    reservas.liberar.return_value = Reserva(clave, curso, 'LIBERADA')
    assert CancelarMatricula(repo, reservas).ejecutar(identificador) == cancelada
    reservas.liberar.assert_called_once_with(clave)


def test_fallo_de_liberacion_permanece_pendiente():
    clave, estudiante, curso, identificador = uuid4(), uuid4(), uuid4(), uuid4()
    pendiente = Matricula(identificador, clave, estudiante, curso, 'CANCELACION_PENDIENTE')
    repo, reservas = Mock(), Mock()
    repo.obtener.return_value = pendiente
    reservas.liberar.side_effect = ResultadoReservaIncierto()
    assert CancelarMatricula(repo, reservas).ejecutar(identificador) == pendiente
    repo.cambiar_estado.assert_not_called()


def test_ausente_y_estado_incompatible():
    repo, reservas = Mock(), Mock()
    repo.obtener.return_value = None
    with pytest.raises(MatriculaNoEncontrada):
        CancelarMatricula(repo, reservas).ejecutar(uuid4())
    repo.obtener.return_value = Matricula(uuid4(), uuid4(), uuid4(), uuid4(), 'FALLIDA')
    with pytest.raises(ConflictoMatricula):
        CancelarMatricula(repo, reservas).ejecutar(uuid4())
