import json
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen
from uuid import UUID

from app.application.port.out.reservas_cursos import Reserva, ReservaRechazada, ResultadoReservaIncierto


class ReservasCursosHttp:
    def __init__(self, base_url: str, timeout: float = 5.0):
        self.base_url = base_url.rstrip('/')
        self.timeout = timeout

    def reservar(self, curso_id: UUID, operacion_id: UUID) -> Reserva:
        reserva = self._solicitar('POST', f'/api/v1/cursos/{curso_id}/reservas', operacion_id)
        if reserva.curso_id != curso_id:
            raise ResultadoReservaIncierto()
        return reserva

    def consultar(self, operacion_id: UUID) -> Reserva:
        return self._solicitar('GET', f'/api/v1/reservas/{operacion_id}', operacion_id)

    def liberar(self, operacion_id: UUID) -> Reserva:
        return self._solicitar('POST', f'/api/v1/reservas/{operacion_id}/liberacion', operacion_id)

    def _solicitar(self, metodo: str, ruta: str, operacion_id: UUID) -> Reserva:
        solicitud = Request(self.base_url + ruta, method=metodo,
                            headers={'Idempotency-Key': str(operacion_id), 'Accept': 'application/json'})
        try:
            with urlopen(solicitud, timeout=self.timeout) as respuesta:
                datos = json.load(respuesta)
            reserva = Reserva(UUID(datos['operacionId']), UUID(datos['cursoId']), datos['estado'])
            if reserva.operacion_id != operacion_id or reserva.estado not in ('RESERVADA', 'LIBERADA'):
                raise ValueError('Respuesta de reserva incompatible')
            return reserva
        except HTTPError as exc:
            if exc.code in (404, 409):
                raise ReservaRechazada(exc.code) from exc
            raise ResultadoReservaIncierto() from exc
        except (URLError, TimeoutError, OSError, ValueError, KeyError, TypeError) as exc:
            raise ResultadoReservaIncierto() from exc
