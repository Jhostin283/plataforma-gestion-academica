import os
from typing import Annotated, Literal
from uuid import UUID

from fastapi import APIRouter, Depends, Header, Query, Response
from pydantic import BaseModel, Field
from sqlalchemy.orm import Session

from app.adapters.out.persistence.database import obtener_sesion
from app.adapters.out.persistence.matricula_repository import SqlAlchemyMatriculaRepository
from app.adapters.out.reservas_cursos_http import ReservasCursosHttp
from app.application.exception.errores import MatriculaNoEncontrada
from app.application.usecase.crear_matricula import CrearMatricula
from app.application.usecase.cancelar_matricula import CancelarMatricula
from app.application.usecase.listar_matriculas import ListarMatriculas

router = APIRouter(prefix='/api/v1/matriculas', tags=['matrículas'])


class CrearMatriculaSolicitud(BaseModel):
    estudiante_id: UUID = Field(alias='estudianteId')
    curso_id: UUID = Field(alias='cursoId')


class MatriculaRespuesta(BaseModel):
    id: UUID
    estudianteId: UUID
    cursoId: UUID
    estado: str


class PaginaMatriculas(BaseModel):
    items: list[MatriculaRespuesta]
    page: int
    size: int
    total: int


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


@router.get('', response_model=PaginaMatriculas,
            responses={400: {'description': 'Filtro o paginación inválidos'}, 503: {'description': 'Persistencia no disponible'}})
def listar(estudiante_id: UUID | None = Query(default=None, alias='estudianteId'),
           curso_id: UUID | None = Query(default=None, alias='cursoId'),
           estado: Literal['PENDIENTE', 'ACTIVA', 'FALLIDA', 'CANCELACION_PENDIENTE', 'CANCELADA'] | None = Query(default=None),
           page: int = Query(default=0, ge=0), size: int = Query(default=20, ge=1, le=100), repo=Depends(repositorio)):
    items, total = ListarMatriculas(repo).ejecutar(estudiante_id, curso_id, estado, page, size)
    return {'items': [respuesta(item) for item in items], 'page': page, 'size': size, 'total': total}


@router.get('/{matricula_id}', response_model=MatriculaRespuesta,
            responses={400: {'description': 'UUID inválido'}, 404: {'description': 'Matrícula inexistente'}})
def consultar(matricula_id: UUID, repo=Depends(repositorio)):
    matricula = repo.obtener(matricula_id)
    if matricula is None:
        raise MatriculaNoEncontrada()
    return respuesta(matricula)


@router.post('/{matricula_id}/cancelacion', response_model=MatriculaRespuesta,
             responses={202: {'description': 'Cancelación pendiente'}, 400: {'description': 'UUID inválido'},
                        404: {'description': 'Matrícula inexistente'}, 409: {'description': 'Estado incompatible'},
                        503: {'description': 'Persistencia no disponible'}})
def cancelar(matricula_id: UUID, response: Response, repo=Depends(repositorio), cliente=Depends(reservas)):
    matricula = CancelarMatricula(repo, cliente).ejecutar(matricula_id)
    response.status_code = 200 if matricula.estado == 'CANCELADA' else 202
    response.headers['Location'] = f'/api/v1/matriculas/{matricula.id}'
    return respuesta(matricula)
