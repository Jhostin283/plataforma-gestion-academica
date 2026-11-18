import io
import json
from urllib.error import HTTPError, URLError
from uuid import uuid4

import pytest

from app.adapters.out.reservas_cursos_http import ReservasCursosHttp
from app.application.port.out.reservas_cursos import ReservaRechazada, ResultadoReservaIncierto


def test_reserva_usa_clave_y_traduce_respuesta(monkeypatch):
    curso, operacion = uuid4(), uuid4()
    cuerpo = json.dumps({'cursoId': str(curso), 'operacionId': str(operacion), 'estado': 'RESERVADA'}).encode()
    def abrir(solicitud, timeout):
        assert solicitud.get_header('Idempotency-key') == str(operacion)
        assert solicitud.full_url.endswith(f'/cursos/{curso}/reservas')
        assert timeout == 5.0
        return io.BytesIO(cuerpo)
    monkeypatch.setattr('app.adapters.out.reservas_cursos_http.urlopen', abrir)
    assert ReservasCursosHttp('http://cursos').reservar(curso, operacion).estado == 'RESERVADA'


def test_reserva_distingue_rechazo_y_resultado_incierto(monkeypatch):
    def rechazo(*args, **kwargs):
        raise HTTPError('http://cursos', 409, 'conflicto', {}, None)
    monkeypatch.setattr('app.adapters.out.reservas_cursos_http.urlopen', rechazo)
    with pytest.raises(ReservaRechazada):
        ReservasCursosHttp('http://cursos').reservar(uuid4(), uuid4())
    def desconectado(*args, **kwargs):
        raise URLError('sin respuesta')
    monkeypatch.setattr('app.adapters.out.reservas_cursos_http.urlopen', desconectado)
    with pytest.raises(ResultadoReservaIncierto):
        ReservasCursosHttp('http://cursos').reservar(uuid4(), uuid4())
