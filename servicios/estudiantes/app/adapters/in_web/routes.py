from typing import Annotated
from uuid import UUID
from fastapi import APIRouter, Depends, Response, status
from pydantic import BaseModel, EmailStr, Field, field_validator
from sqlalchemy.orm import Session
from app.adapters.out.persistence.database import obtener_sesion
from app.adapters.out.persistence.estudiante_repository import SqlAlchemyEstudianteRepository
from app.application.usecase.consultar_estudiante import ConsultarEstudiante
from app.application.usecase.registrar_estudiante import RegistrarEstudiante

router = APIRouter(prefix="/api/v1/estudiantes", tags=["estudiantes"])


class CrearEstudiante(BaseModel):
    documento: str = Field(min_length=1, max_length=50)
    nombres: str = Field(min_length=1, max_length=150)
    apellidos: str = Field(min_length=1, max_length=150)
    correo: EmailStr

    @field_validator("documento", "nombres", "apellidos", mode="before")
    @classmethod
    def normalizar(cls, valor):
        return valor.strip() if isinstance(valor, str) else valor


class EstudianteRespuesta(BaseModel):
    id: UUID
    documento: str
    nombres: str
    apellidos: str
    correo: EmailStr
    estado: str


def repository(session: Annotated[Session, Depends(obtener_sesion)]):
    return SqlAlchemyEstudianteRepository(session)


@router.post("", status_code=status.HTTP_201_CREATED, response_model=EstudianteRespuesta,
             responses={400: {"description": "JSON inválido"}, 409: {"description": "Documento duplicado"},
                        422: {"description": "Datos inválidos"}, 503: {"description": "Persistencia no disponible"}})
def registrar(datos: CrearEstudiante, response: Response, repo=Depends(repository)):
    estudiante = RegistrarEstudiante(repo).ejecutar(datos.documento, datos.nombres, datos.apellidos, str(datos.correo))
    response.headers["Location"] = f"/api/v1/estudiantes/{estudiante.id}"
    return estudiante


@router.get("/{estudiante_id}", response_model=EstudianteRespuesta, responses={404: {"description": "Estudiante no encontrado"}})
def consultar(estudiante_id: UUID, repo=Depends(repository)):
    return ConsultarEstudiante(repo).ejecutar(estudiante_id)
