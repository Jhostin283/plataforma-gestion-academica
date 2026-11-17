from uuid import UUID
from sqlalchemy import String, UniqueConstraint
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
