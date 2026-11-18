import os
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

DATABASE_URL = os.getenv("ESTUDIANTES_DB_URL", "postgresql+psycopg://estudiantes@localhost:5432/estudiantes")
engine = create_engine(DATABASE_URL, pool_pre_ping=True)
SessionLocal = sessionmaker(bind=engine, expire_on_commit=False)


def obtener_sesion():
    with SessionLocal() as session:
        yield session
