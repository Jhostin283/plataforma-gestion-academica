from uuid import UUID
from sqlalchemy import CheckConstraint, ForeignKey, Index, String, UniqueConstraint, text
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column


class Base(DeclarativeBase):
    pass


class EstudianteModel(Base):
    __tablename__ = "estudiantes"
    __table_args__ = (UniqueConstraint("documento", name="uq_estudiantes_documento"),)
    id: Mapped[UUID] = mapped_column(primary_key=True)
    documento: Mapped[str] = mapped_column(String(50), nullable=False)
    nombres: Mapped[str] = mapped_column(String(150), nullable=False)
    apellidos: Mapped[str] = mapped_column(String(150), nullable=False)
    correo: Mapped[str] = mapped_column(String(254), nullable=False)
    estado: Mapped[str] = mapped_column(String(20), nullable=False)


class MatriculaModel(Base):
    __tablename__ = 'matriculas'
    __table_args__ = (
        UniqueConstraint('operacion_id', name='uq_matriculas_operacion'),
        CheckConstraint("estado IN ('PENDIENTE', 'ACTIVA', 'FALLIDA', 'COMPENSACION_PENDIENTE', 'CANCELACION_PENDIENTE', 'CANCELADA')", name='ck_matriculas_estado'),
        Index('uq_matriculas_estudiante_curso_vigente', 'estudiante_id', 'curso_id', unique=True,
              postgresql_where=text("estado IN ('PENDIENTE', 'ACTIVA', 'COMPENSACION_PENDIENTE', 'CANCELACION_PENDIENTE')")),
    )
    id: Mapped[UUID] = mapped_column(primary_key=True)
    operacion_id: Mapped[UUID] = mapped_column(nullable=False)
    estudiante_id: Mapped[UUID] = mapped_column(ForeignKey('estudiantes.id'), nullable=False)
    curso_id: Mapped[UUID] = mapped_column(nullable=False)
    estado: Mapped[str] = mapped_column(String(30), nullable=False)
    codigo_rechazo: Mapped[int | None] = mapped_column(nullable=True)
