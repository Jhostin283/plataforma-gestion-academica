"""Reintenta operaciones pendientes usando su clave original; ejecutar periódicamente."""
import logging
import os

from app.adapters.out.persistence.database import SessionLocal
from app.adapters.out.persistence.matricula_repository import SqlAlchemyMatriculaRepository
from app.adapters.out.reservas_cursos_http import ReservasCursosHttp
from app.application.usecase.crear_matricula import CrearMatricula

logger = logging.getLogger(__name__)


def reconciliar():
    cliente = ReservasCursosHttp(os.getenv('CURSOS_BASE_URL', 'http://cursos:8080'))
    cursor = None
    while True:
        with SessionLocal() as session:
            pendientes = SqlAlchemyMatriculaRepository(session).pendientes(despues_de=cursor)
        if not pendientes:
            break
        for pendiente in pendientes:
            try:
                with SessionLocal() as session:
                    CrearMatricula(SqlAlchemyMatriculaRepository(session), cliente).ejecutar(
                        pendiente.operacion_id, pendiente.estudiante_id, pendiente.curso_id)
            except Exception:
                logger.error('No se pudo reconciliar la operación %s', pendiente.operacion_id)
        cursor = pendientes[-1].id


if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)
    reconciliar()
