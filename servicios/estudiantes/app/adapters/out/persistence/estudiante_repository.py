from uuid import UUID
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session
from app.adapters.out.persistence.modelos import EstudianteModel
from app.application.exception.errores import DocumentoDuplicado
from app.domain.entity.estudiante import Estudiante


def _dominio(modelo: EstudianteModel) -> Estudiante:
    return Estudiante(modelo.id, modelo.documento, modelo.nombres, modelo.apellidos, modelo.correo, modelo.estado)


class SqlAlchemyEstudianteRepository:
    def __init__(self, session: Session):
        self.session = session

    def guardar(self, estudiante: Estudiante) -> Estudiante:
        modelo = EstudianteModel(id=estudiante.id, documento=estudiante.documento, nombres=estudiante.nombres,
                                 apellidos=estudiante.apellidos, correo=estudiante.correo, estado=estudiante.estado)
        try:
            self.session.add(modelo)
            self.session.commit()
        except IntegrityError as exc:
            self.session.rollback()
            if getattr(getattr(exc.orig, "diag", None), "constraint_name", None) == "uq_estudiantes_documento":
                raise DocumentoDuplicado() from exc
            raise
        return estudiante

    def buscar_por_id(self, estudiante_id: UUID) -> Estudiante | None:
        modelo = self.session.get(EstudianteModel, estudiante_id)
        return _dominio(modelo) if modelo else None
