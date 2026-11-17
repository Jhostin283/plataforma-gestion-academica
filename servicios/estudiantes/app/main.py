from uuid import uuid4
import asyncio
import logging
import os
from contextlib import asynccontextmanager, suppress
from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
from sqlalchemy.exc import SQLAlchemyError
from app.adapters.in_web.routes import router
from app.adapters.in_web.matriculas_routes import router as matriculas_router
from app.application.exception.errores import (DocumentoDuplicado, EstudianteNoEncontrado,
                                               ConflictoMatricula, CursoNoEncontrado, MatriculaNoEncontrada,
                                               MatriculaCompensada)

async def _reconciliacion_periodica():
    from app.reconciliar_matriculas import reconciliar
    intervalo = max(1, int(os.getenv('MATRICULAS_RECONCILIACION_SEGUNDOS', '60')))
    while True:
        try:
            await asyncio.to_thread(reconciliar)
        except Exception:
            logging.getLogger(__name__).error('Falló el ciclo de reconciliación de matrículas')
        await asyncio.sleep(intervalo)


@asynccontextmanager
async def lifespan(_app: FastAPI):
    tarea = asyncio.create_task(_reconciliacion_periodica())
    try:
        yield
    finally:
        tarea.cancel()
        with suppress(asyncio.CancelledError):
            await tarea


app = FastAPI(title="Servicio de estudiantes", version="1.0.0", lifespan=lifespan)
app.include_router(router)
app.include_router(matriculas_router)


def respuesta_error(status: int, codigo: str, mensaje: str, detalles=None):
    return JSONResponse(status_code=status, content={"codigo": codigo, "mensaje": mensaje,
                        "detalles": detalles or {}, "correlationId": str(uuid4())})


@app.exception_handler(RequestValidationError)
async def validacion(_request: Request, exc: RequestValidationError):
    errores = exc.errors()
    if any(error["type"] == "json_invalid" for error in errores):
        return respuesta_error(400, "JSON_INVALIDO", "El cuerpo no es válido")
    if any(error["loc"] and error["loc"][0] in ("path", "query", "header") for error in errores):
        return respuesta_error(400, "PARAMETRO_INVALIDO", "El parámetro no es válido")
    if any(error['type'] == 'uuid_parsing' for error in errores):
        return respuesta_error(400, 'PARAMETRO_INVALIDO', 'El identificador no es válido')
    detalles = {".".join(map(str, error["loc"])): error["msg"] for error in errores}
    return respuesta_error(422, "DATOS_INVALIDOS", "Los datos no son válidos", detalles)


@app.exception_handler(DocumentoDuplicado)
async def duplicado(_request: Request, _exc: DocumentoDuplicado):
    return respuesta_error(409, "DOCUMENTO_DUPLICADO", "El documento ya está registrado")


@app.exception_handler(EstudianteNoEncontrado)
async def no_encontrado(_request: Request, _exc: EstudianteNoEncontrado):
    return respuesta_error(404, "ESTUDIANTE_NO_ENCONTRADO", "El estudiante no existe")


@app.exception_handler(SQLAlchemyError)
async def persistencia(_request: Request, _exc: SQLAlchemyError):
    return respuesta_error(503, "PERSISTENCIA_NO_DISPONIBLE", "No se pudo procesar la solicitud")


@app.exception_handler(ConflictoMatricula)
async def conflicto_matricula(_request: Request, _exc: ConflictoMatricula):
    return respuesta_error(409, 'CONFLICTO_MATRICULA', 'No se puede crear la matrícula')


@app.exception_handler(MatriculaCompensada)
async def matricula_compensada(_request: Request, _exc: MatriculaCompensada):
    return respuesta_error(500, 'MATRICULA_COMPENSADA', 'La matrícula no se completó y la reserva fue liberada')


@app.exception_handler(CursoNoEncontrado)
@app.exception_handler(MatriculaNoEncontrada)
async def recurso_no_encontrado(_request: Request, _exc: Exception):
    return respuesta_error(404, 'RECURSO_NO_ENCONTRADO', 'El recurso no existe')


@app.get("/health", tags=["salud"])
def salud():
    return {"estado": "UP"}
