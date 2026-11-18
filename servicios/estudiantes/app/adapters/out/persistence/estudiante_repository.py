from uuid import UUID
from sqlalchemy.exc import IntegrityError
from sqlalchemy import func, or_, select, update
from sqlalchemy.orm import Session
from app.adapters.out.persistence.modelos import EstudianteModel
from app.application.exception.errores import DocumentoDuplicado, EstudianteNoEncontrado
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

    def actualizar(self, estudiante: Estudiante) -> Estudiante:
        modelo = self.session.get(EstudianteModel, estudiante.id)
        if modelo is None:
            raise EstudianteNoEncontrado()
        modelo.documento, modelo.nombres = estudiante.documento, estudiante.nombres
        modelo.apellidos, modelo.correo = estudiante.apellidos, estudiante.correo
        try:
            self.session.commit()
        except IntegrityError as exc:
            self.session.rollback()
            if getattr(getattr(exc.orig, "diag", None), "constraint_name", None) == "uq_estudiantes_documento":
                raise DocumentoDuplicado() from exc
            raise
        return estudiante

    def cambiar_estado(self, estudiante_id: UUID, estado: str) -> Estudiante:
        sentencia = (update(EstudianteModel).where(EstudianteModel.id == estudiante_id)
                     .values(estado=estado).returning(EstudianteModel))
        modelo = self.session.execute(sentencia).scalar_one_or_none()
        if modelo is None:
            raise EstudianteNoEncontrado()
        estudiante = _dominio(modelo)
        self.session.commit()
        return estudiante

    def listar(self, buscar: str | None, page: int, size: int) -> tuple[list[Estudiante], int]:
        filtros = []
        if buscar and buscar.strip():
            texto = buscar.strip().replace('\\', '\\\\').replace('%', '\\%').replace('_', '\\_')
            patron = f'%{texto}%'
            filtros.append(or_(*(col.ilike(patron, escape='\\') for col in
                                (EstudianteModel.nombres, EstudianteModel.apellidos, EstudianteModel.documento))))
        total = self.session.scalar(select(func.count()).select_from(EstudianteModel).where(*filtros))
        consulta = (select(EstudianteModel).where(*filtros)
                    .order_by(EstudianteModel.apellidos, EstudianteModel.nombres, EstudianteModel.id)
                    .offset(page * size).limit(size))
        return [_dominio(modelo) for modelo in self.session.scalars(consulta)], total
