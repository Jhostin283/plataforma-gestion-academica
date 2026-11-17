import os
from typing import Annotated
from uuid import UUID

from fastapi import APIRouter, Depends, Header, Response
from pydantic import BaseModel, Field
from sqlalchemy.orm import Session

from app.adapters.out.persistence.database import obtener_sesion
from app.adapters.out.persistence.matricula_repository import SqlAlchemyMatriculaRepository
from app.adapters.out.reservas_cursos_http import ReservasCursosHttp
from app.application.exception.errores import MatriculaNoEncontrada
from app.application.usecase.crear_matricula import CrearMatricula

router = APIRouter(prefix='/api/v1/matriculas', tags=['matrículas'])


class CrearMatriculaSolicitud(BaseModel):
    estudiante_id: UUID = Field(alias='estudianteId')
    curso_id: UUID = Field(alias='cursoId')


class MatriculaRespuesta(BaseModel):
    id: UUID
    estudianteId: UUID
    cursoId: UUID
    estado: str


def repositorio(session: Annotated[Session, Depends(obtener_sesion)]):
    return SqlAlchemyMatriculaRepository(session)


def reservas():
    return ReservasCursosHttp(os.getenv('CURSOS_BASE_URL', 'http://cursos:8080'))


def respuesta(matricula):
    return {'id': matricula.id, 'estudianteId': matricula.estudiante_id,
            'cursoId': matricula.curso_id,
            'estado': 'PENDIENTE' if matricula.estado == 'COMPENSACION_PENDIENTE' else matricula.estado}


@router.post('', response_model=MatriculaRespuesta, status_code=201,
             responses={202: {'description': 'Operación pendiente'}, 400: {'description': 'Formato inválido'},
                        404: {'description': 'Estudiante o curso inexistente'}, 409: {'description': 'Conflicto'},
                        422: {'description': 'Datos incompletos'},
                        500: {'description': 'No se completó la matrícula y la reserva fue liberada'},
                        503: {'description': 'Persistencia no disponible'}})
def crear(datos: CrearMatriculaSolicitud, response: Response,
          clave: Annotated[UUID, Header(alias='Idempotency-Key')],
          repo=Depends(repositorio), cliente=Depends(reservas)):
    matricula = CrearMatricula(repo, cliente).ejecutar(clave, datos.estudiante_id, datos.curso_id)
    if matricula.estado == 'FALLIDA':
        from app.application.exception.errores import CursoNoEncontrado, ConflictoMatricula
        if matricula.codigo_rechazo == 404:
            raise CursoNoEncontrado()
        if matricula.codigo_rechazo == 503:
            from app.application.exception.errores import MatriculaCompensada
            raise MatriculaCompensada()
        raise ConflictoMatricula()
    response.status_code = 201 if matricula.estado == 'ACTIVA' else 202
    response.headers['Location'] = f'/api/v1/matriculas/{matricula.id}'
    return respuesta(matricula)


@router.get('/{matricula_id}', response_model=MatriculaRespuesta,
            responses={400: {'description': 'UUID inválido'}, 404: {'description': 'Matrícula inexistente'}})
def consultar(matricula_id: UUID, repo=Depends(repositorio)):
    matricula = repo.obtener(matricula_id)
    if matricula is None:
        raise MatriculaNoEncontrada()
    return respuesta(matricula)
