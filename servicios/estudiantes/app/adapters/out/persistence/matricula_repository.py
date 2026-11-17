from uuid import UUID, uuid4

from sqlalchemy import select, update
from sqlalchemy.exc import IntegrityError, SQLAlchemyError
from sqlalchemy.orm import Session

from app.adapters.out.persistence.modelos import EstudianteModel, MatriculaModel
from app.application.exception.errores import ConflictoMatricula, EstudianteNoEncontrado, ResultadoPersistenciaIncierto
from app.domain.entity.matricula import Matricula


def _dominio(modelo: MatriculaModel) -> Matricula:
    return Matricula(modelo.id, modelo.operacion_id, modelo.estudiante_id, modelo.curso_id,
                     modelo.estado, modelo.codigo_rechazo)


class SqlAlchemyMatriculaRepository:
    def __init__(self, session: Session):
        self.session = session

    def _por_operacion(self, operacion_id: UUID):
        return self.session.scalar(select(MatriculaModel).where(MatriculaModel.operacion_id == operacion_id))

    @staticmethod
    def _repeticion(modelo, estudiante_id, curso_id):
        if modelo.estudiante_id != estudiante_id or modelo.curso_id != curso_id:
            raise ConflictoMatricula()
        return _dominio(modelo)

    def iniciar(self, operacion_id: UUID, estudiante_id: UUID, curso_id: UUID) -> Matricula:
        existente = self._por_operacion(operacion_id)
        if existente:
            resultado = self._repeticion(existente, estudiante_id, curso_id)
            self.session.rollback()
            return resultado
        estudiante = self.session.scalar(select(EstudianteModel).where(EstudianteModel.id == estudiante_id).with_for_update())
        if estudiante is None:
            self.session.rollback()
            raise EstudianteNoEncontrado()
        if estudiante.estado != 'ACTIVO':
            self.session.rollback()
            raise ConflictoMatricula()
        modelo = MatriculaModel(id=uuid4(), operacion_id=operacion_id, estudiante_id=estudiante_id,
                                curso_id=curso_id, estado='PENDIENTE')
        self.session.add(modelo)
        try:
            self.session.commit()
        except IntegrityError as exc:
            self.session.rollback()
            existente = self._por_operacion(operacion_id)
            if existente:
                resultado = self._repeticion(existente, estudiante_id, curso_id)
                self.session.rollback()
                return resultado
            self.session.rollback()
            if getattr(getattr(exc.orig, 'diag', None), 'constraint_name', None) == 'uq_matriculas_estudiante_curso_vigente':
                raise ConflictoMatricula() from exc
            raise
        return _dominio(modelo)

    def obtener(self, matricula_id: UUID) -> Matricula | None:
        modelo = self.session.get(MatriculaModel, matricula_id)
        resultado = _dominio(modelo) if modelo else None
        self.session.rollback()
        return resultado

    def pendientes(self, limite: int = 100, despues_de: UUID | None = None) -> list[Matricula]:
        consulta = select(MatriculaModel).where(MatriculaModel.estado == 'PENDIENTE')
        if despues_de is not None:
            consulta = consulta.where(MatriculaModel.id > despues_de)
        modelos = self.session.scalars(consulta.order_by(MatriculaModel.id).limit(limite)).all()
        resultado = [_dominio(modelo) for modelo in modelos]
        self.session.rollback()
        return resultado

    def cambiar_estado(self, matricula_id: UUID, estado: str, codigo_rechazo: int | None = None) -> Matricula:
        sentencia = (update(MatriculaModel).where(MatriculaModel.id == matricula_id,
                                                  MatriculaModel.estado == 'PENDIENTE')
                     .values(estado=estado, codigo_rechazo=codigo_rechazo).returning(MatriculaModel))
        try:
            modelo = self.session.execute(sentencia).scalar_one_or_none()
            if modelo is None:
                modelo = self.session.get(MatriculaModel, matricula_id)
            if modelo is None:
                self.session.rollback()
                raise LookupError('Matrícula no encontrada')
            resultado = _dominio(modelo)
            self.session.commit()
            return resultado
        except SQLAlchemyError as exc:
            self.session.rollback()
            raise ResultadoPersistenciaIncierto() from exc
